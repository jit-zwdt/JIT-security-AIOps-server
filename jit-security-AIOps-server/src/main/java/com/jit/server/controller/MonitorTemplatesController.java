package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorTemplates;
import com.jit.server.service.MonitorTemplatesService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.06.06
 */

@RestController
@RequestMapping("/monitorTemplates")
public class MonitorTemplatesController {

    @Autowired
    private MonitorTemplatesService monitorTemplatesService;

    @ResponseBody
    @PostMapping(value = "/getMonitorTemplates")
    public Result getMonitorTemplates(@RequestHeader String authorization, @RequestBody PageRequest params) {
        List<MonitorTemplates> monitorTemplatesList = null;
        try {
            System.out.println(params);
            monitorTemplatesList = monitorTemplatesService.getMonitorTemplates();
            return Result.SUCCESS(monitorTemplatesList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

}