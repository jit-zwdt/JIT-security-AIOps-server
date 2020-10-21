package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorSchemeTimerTaskEntity;
import com.jit.server.request.ScheduleTaskParams;
import com.jit.server.service.InspectionManageService;
import com.jit.server.service.SysScheduleTaskService;
import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public Result addTimerTaskInfo(@RequestParam("param") String param) {
        try {
            if (param == null) {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
            String auth = zabbixAuthService.getAuth();
            String username = userService.findIdByUsername();
            JSONObject jsonObject = JSONObject.parseObject(param);
            // 在 monitor_scheme_timer_task 表中添加一条主数据 其他的数据都是跟着这条数据进行添加的
            MonitorSchemeTimerTaskEntity monitorSchemeTimerTaskEntity = inspectionManageService.addMonitorSchemeTimerTask(jsonObject.toString());
            jsonObject.put("parentId" , monitorSchemeTimerTaskEntity.getId());
            jsonObject.put("auth",auth);
            jsonObject.put("username",username);
            ScheduleTaskParams st = new ScheduleTaskParams();
            st.setCronExpression(jsonObject.get("timerTask")+"");
            st.setJobClassName("com.jit.server.job.TimerTask");
            st.setJobMethodName("taskWithParams");
            st.setJsonParam(jsonObject.toString());
            //进行正常的添加 quartz 操作
            String info = sysScheduleTaskService.addScheduleTask(st);
            if (info!=null) {
                return Result.SUCCESS(info);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/makePdf")
    public void makePdf(HttpServletResponse response, HttpServletRequest request) {
        try {
//            String filename = inspectionManageService.createPDF();
//            if (filename == null) {
//                return;
//            }
//            File file = new File(filename);
//            if (file.exists()) {
//                FileInputStream input = null;
//                try {
//                    input = new FileInputStream(file);
//                    byte[] buffer = new byte[1024 * 10];
//                    ServletOutputStream out = null;
//                    int len = 0;
//                    out = response.getOutputStream();
//                    while ((len = input.read(buffer)) != -1) {
//                        out.write(buffer, 0, len);
//                    }
//                    response.setCharacterEncoding("utf-8");
//                    response.setContentType("application/pdf");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (input != null) {
//                        input.close();
//                    }
//                    if (file != null) {
//                        file.delete();
//                    }
//                }
//            } else {
//                return;
//            }
        } catch (Exception e) {
            e.printStackTrace();
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
            Page<MonitorSchemeTimerTaskEntity> sysScheduleTaskEntities = inspectionManageService.getMonitorSchemeTimerTasks(params);
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
     * @return 统一返回对象
     */
    @DeleteMapping("/deleteMonitorSchemeTimerTask/{id}")
    public Result deleteMonitorSchemeTimerTask(@PathVariable String id){
        inspectionManageService.deleteMonitorSchemeTimerTask(id);
        return Result.SUCCESS(null);
    }
}
