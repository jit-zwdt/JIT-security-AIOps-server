package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorHostDetailBindGraphs;
import com.jit.server.request.ScheduleTaskParams;
import com.jit.server.service.InspectionManageService;
import com.jit.server.service.SysScheduleTaskService;
import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
            jsonObject.put("auth",auth);
            jsonObject.put("username",username);
            ScheduleTaskParams st = new ScheduleTaskParams();
            st.setCronExpression(jsonObject.get("timerTask")+"");
            st.setJobClassName("com.jit.server.job.TimerTask");
            st.setJobMethodName("taskWithParams");
            st.setJsonParam(jsonObject.toString());
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

}
