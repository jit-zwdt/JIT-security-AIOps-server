package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorTypeEntity;
import com.jit.server.service.MonitorTypeService;
import com.jit.server.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.06.06
 */

@RestController
@RequestMapping("/monitorType")
public class MonitorTypeController {

    @Autowired
    private MonitorTypeService monitorTypeService;

    @ResponseBody
    @PostMapping(value = "/getMonitorTypes")
    public Result getMonitorTemplates() {
        try {
            List<MonitorTypeEntity> monitorTemplatesList = monitorTypeService.getMonitorTypes();
            return Result.SUCCESS(monitorTemplatesList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

}