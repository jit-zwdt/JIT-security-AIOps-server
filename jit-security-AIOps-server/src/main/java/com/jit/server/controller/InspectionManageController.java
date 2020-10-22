package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.dto.MonitorSchemeTimerTaskEntityDto;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorSchemeTimerTaskEntity;
import com.jit.server.request.ScheduleTaskParams;
import com.jit.server.service.InspectionManageService;
import com.jit.server.service.SysScheduleTaskService;
import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.FtpClientUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sun.net.ftp.FtpClient;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Result addTimerTaskInfo(@RequestParam("param") String param , String id) {
        try {
            if (param == null) {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
            String auth = zabbixAuthService.getAuth();
            String username = userService.findIdByUsername();
            JSONObject jsonObject = JSONObject.parseObject(param);
            // 在 monitor_scheme_timer_task 表中添加一条主数据 其他的数据都是跟着这条数据进行添加的
            MonitorSchemeTimerTaskEntity monitorSchemeTimerTaskEntity = null;
            if (id == null) {
                monitorSchemeTimerTaskEntity = inspectionManageService.addMonitorSchemeTimerTask(jsonObject.toString());
                jsonObject.put("parentId" , monitorSchemeTimerTaskEntity.getId());
            }
            jsonObject.put("auth",auth);
            jsonObject.put("username",username);
            ScheduleTaskParams st = new ScheduleTaskParams();
            st.setId(id);
            st.setCronExpression(jsonObject.get("timerTask")+"");
            st.setJobClassName("com.jit.server.job.TimerTask");
            st.setJobMethodName("taskWithParams");
            st.setJsonParam(jsonObject.toString());
            //进行正常的添加 quartz 操作
            String sysScheduleId = sysScheduleTaskService.addScheduleTask(st);
            //调用保存方法进行 monitor_scheme_timer_task 表数据的更新操作
            if (id == null) {
                monitorSchemeTimerTaskEntity.setScheduleId(sysScheduleId);
                inspectionManageService.addMonitorSchemeTimerTask(monitorSchemeTimerTaskEntity);
            }
            if (sysScheduleId!=null) {
                return Result.SUCCESS(sysScheduleId);
            }else{
                // 模拟数据的回滚操作
                inspectionManageService.deleteMonitorSchemeTimerTask(monitorSchemeTimerTaskEntity.getId());
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
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
            ftpClient = ftpClientUtil.getConnectionFTP("172.16.15.10", 21, "jitutil", "dota&csjit3368");
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

    /**
     * 根据传递的参数进行查询 巡检信息
     * 默认情况下查询所有的父级别数据
     * 如果需要查询所有的子数据需要传入 parentId 字段值不为 null 就可以
     * @param params 参数对象
     * @return Result 返回值对象
     */
    @PostMapping(value = "/getMonitorSchemeTimerTasks")
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
}
