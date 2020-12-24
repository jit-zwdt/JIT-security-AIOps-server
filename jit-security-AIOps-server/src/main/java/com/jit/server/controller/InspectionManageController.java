package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.ChannelSftp;
import com.jit.server.annotation.AutoLog;
import com.jit.server.config.FtpConfig;
import com.jit.server.config.SFtpConfig;
import com.jit.server.dto.MonitorSchemeTimerTaskEntityDto;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorSchemeTimerTaskEntity;
import com.jit.server.pojo.SysScheduleTaskEntity;
import com.jit.server.pojo.SysUserEntity;
import com.jit.server.request.ScheduleTaskParams;
import com.jit.server.service.*;
import com.jit.server.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/inspection")
public class InspectionManageController {

    @Autowired
    private InspectionManageService inspectionManageService;

    @Autowired
    private SysScheduleTaskService sysScheduleTaskService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private UserService userService;

    @Autowired
    private FtpConfig ftpConfig;

    @Autowired
    private SFtpConfig sFtpConfig;

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/getHostInfo")
    public Result getHostInfo(@RequestParam("id") String id) {
        try {
            List<HostEntity> bean = inspectionManageService.getHostInfo(id);
            if (bean!=null) {
                return Result.SUCCESS(bean);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addTimerTaskInfo")
    @AutoLog(value = "巡检计划管理-添加", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addTimerTaskInfo(@RequestParam("param") String param , String id) {
        try {
            if (param == null) {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
            //进行数据的添加
            String sysScheduleId = saveTimerTaskInfo(param, null);
            if (sysScheduleId != null && !sysScheduleId.isEmpty()) {
                return Result.SUCCESS(sysScheduleId);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateTimerTaskInfo")
    @AutoLog(value = "巡检计划管理-编辑", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateTimerTaskInfo(@RequestParam("param") String param , String id){
        try {
            if (param == null) {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
            //进行数据的添加
            String sysScheduleId = saveTimerTaskInfo(param, id);
            if (sysScheduleId != null && !sysScheduleId.isEmpty()) {
                return Result.SUCCESS(sysScheduleId);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/makePdf")
    public void makePdf(String ftpFilePath, HttpServletResponse response) {
        // 声明输出流对象
        OutputStream os = null;
        // 声明 FTP 工具类对象
        FtpClientUtil ftpClientUtil = null;
        // 声明 FTP 客户端对象
        FTPClient ftpClient = null;
        try {
            //创建 FtpUtil 对象
            ftpClientUtil = new FtpClientUtil();
            //获取 FTP 连接对象
            ftpClient = ftpClientUtil.getConnectionFTP(ftpConfig.getHostName(), ftpConfig.getPort(), ftpConfig.getUserName(), ftpConfig.getPassWord());
            //输出流构建
            os = response.getOutputStream();
            //设置返回值属性
            response.setCharacterEncoding("utf-8");
            //设置返回的文件是 pdf 文件
            response.setContentType("application/pdf");
            //获取文件并打入流中
            ftpClient.retrieveFile(ftpFilePath, os);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(os != null){
                try {
                    // 关闭流对象
                    os.close();
                    // 关闭 FTP 对象
                    ftpClientUtil.closeFTP(ftpClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @PostMapping("/downloadSftpPdf")
    @AutoLog(value = "巡检报告中心-预览", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public void downloadSftpPdf(String ftpFilePath, HttpServletResponse response) {
        SFTPClientUtil sftp = new SFTPClientUtil(3, 6000);
        InputStream input = null;
        try {
            SftpConfig sftpConfig = new SftpConfig(sFtpConfig.getHostName(), sFtpConfig.getPort(), sFtpConfig.getUserName(), sFtpConfig.getPassWord(), sFtpConfig.getTimeOut(), sFtpConfig.getRemoteRootPath());
            ChannelSftp csftp = sftp.connect(sftpConfig);
            csftp.cd(sftpConfig.getRemoteRootPath());
            input = csftp.get(ftpFilePath);
            IOUtils.copy(input,response.getOutputStream());
            //设置返回值属性
            response.setCharacterEncoding("utf-8");
            //设置返回的文件是 pdf 文件
            response.setContentType("application/pdf");
            response.getOutputStream().close();
            response.flushBuffer();
            sftp.disConnect(csftp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(input != null){
                try {
                    // 关闭流对象
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据传递的参数进行查询 巡检信息
     * 默认情况下查询所有的父级别数据
     * 如果需要查询所有的子数据需要传入 parentId 字段值不为 null 就可以
     * @param params 参数对象
     * @return Result 返回值对象
     */
    @PostMapping(value = "/getMonitorSchemeTimerTasks")
    @AutoLog(value = "巡检计划管理/巡检报告中心-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getMonitorSchemeTimerTasks(@RequestBody PageRequest<Map<String, Object>> params) {
        try {
            Page<MonitorSchemeTimerTaskEntityDto> sysScheduleTaskEntities = inspectionManageService.getMonitorSchemeTimerTasks(params);
            Map<String, Object> result = new HashMap<>(5);
            result.put("page", params.getPage());
            result.put("size", params.getSize());
            result.put("totalRow", sysScheduleTaskEntities.getTotalElements());
            result.put("totalPage", sysScheduleTaskEntities.getTotalPages());
            result.put("dataList", sysScheduleTaskEntities.getContent());
            return Result.SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    /**
     * 根据 id 删除数据数据 如果有子数据也会自动删除子数据
     * @param id monitor_scheme_timer_task 表的 id
     * @param scheduleId sys_schedule_task 表的 id
     * @return 统一返回对象
     */
    @DeleteMapping("/deleteMonitorSchemeTimerTask/{id}/{scheduleId}")
    @AutoLog(value = "巡检计划管理-删除", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteMonitorSchemeTimerTask(@PathVariable String id , @PathVariable String scheduleId){
        //非空校验
        if (id == null) {
            return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
        }
        try {
            //删除 monitor_scheme_timer_task 的数据
            inspectionManageService.deleteMonitorSchemeTimerTask(id);
            //删除 sys_schedule_task 的数据
            sysScheduleTaskService.delScheduleTask(scheduleId);
        } catch (Exception e) {
            e.printStackTrace();
            Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
        return Result.SUCCESS(null);
    }

    /**
     * 更新 , 添加巡检计划管理的方法
     * @param param 添加信息参数
     * @param id 进行修改的 id 如果添加则传入 null 即可
     * @return sysScheduleId 添加的数据 ID 信息
     */
    private String saveTimerTaskInfo(String param , String id) throws Exception {
        String auth = zabbixAuthService.getAuth();
        String username = userService.findNamebyUsername();
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 在 monitor_scheme_timer_task 表中添加一条主数据 其他的数据都是跟着这条数据进行添加的
        MonitorSchemeTimerTaskEntity monitorSchemeTimerTaskEntity = null;
        if (id == null) {
            monitorSchemeTimerTaskEntity = inspectionManageService.addMonitorSchemeTimerTask(jsonObject.toString());
            jsonObject.put("parentId" , monitorSchemeTimerTaskEntity.getId());
            jsonObject.put("createTime",monitorSchemeTimerTaskEntity.getGmtCreate());
        }
        String userId = userService.findIdByUsername();
        Optional<SysUserEntity> bean = sysUserService.findById(userId);
        SysUserEntity sysDictionaryEntity = new SysUserEntity();
        String mobile = "";
        if (bean.isPresent()) {
            sysDictionaryEntity = bean.get();
            mobile = sysDictionaryEntity.getMobile() != "" ? sysDictionaryEntity.getMobile() : "无";
        }
        jsonObject.put("auth",auth);
        jsonObject.put("username",username);
        jsonObject.put("userId",userId);
        jsonObject.put("mobile", mobile);
        ScheduleTaskParams st = new ScheduleTaskParams();
        st.setId(id);
        st.setCronExpression(jsonObject.get("timerTask")+"");
        st.setJobClassName("com.jit.server.job.TimerTask");
        st.setJobMethodName("taskWithParams");
        // 设置初始状态为关闭
        st.setStatus(1);
        st.setJsonParam(jsonObject.toString());
        //进行正常的添加 quartz 操作
        String sysScheduleId = sysScheduleTaskService.addScheduleTask(st);
        //调用保存方法进行 monitor_scheme_timer_task 表数据的更新操作
        if (id == null) {
            monitorSchemeTimerTaskEntity.setScheduleId(sysScheduleId);
            inspectionManageService.addMonitorSchemeTimerTask(monitorSchemeTimerTaskEntity);
        }
        // 在定时器添加之前进行查询当前添加成功的表的数据的操作
        SysScheduleTaskEntity scheduleTask = sysScheduleTaskService.getSysScheduleTaskById(sysScheduleId);
        // 获取传递的参数
        JSONObject JsonParam = JSONObject.parseObject(scheduleTask.getJsonParam());
        // 设置 scheduleId 值
        JsonParam.put("scheduleId" , sysScheduleId);
        // 设置 st 对象的 sysScheduleId 设置完成后可以进行更新操作
        st.setId(sysScheduleId);
        // 设置传入参数对象
        st.setJsonParam(JsonParam.toJSONString());
        // 调用添加方法会自动的进行更新操作
        sysScheduleTaskService.addScheduleTask(st);
        // 调用方法进行状态的修改
        sysScheduleTaskService.changeStatus(sysScheduleId);
        // 添加定时任务
        //返回添加巡检计划数据的 ScheduleId
        if (sysScheduleId == null) {
            // 模拟数据的回滚操作
            inspectionManageService.deleteMonitorSchemeTimerTask(monitorSchemeTimerTaskEntity.getId());
        }
        return sysScheduleId;
    }
}
