package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorTemplates;
import com.jit.server.service.MonitorTemplatesService;
import com.jit.server.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.06.06
 */

@Api(tags = "MonitorTemplatesController")
@RestController
@RequestMapping("/monitorTemplates")
public class MonitorTemplatesController {

    @Autowired
    private MonitorTemplatesService monitorTemplatesService;

    @ApiOperation(value = "getMonitorTemplates", notes = "get monitor Templates list")
    @ResponseBody
    @PostMapping(value = "/getMonitorTemplates")
    public Result getMonitorTemplates(@RequestHeader String authorization) {
        List<MonitorTemplates> monitorTemplatesList = null;
        try {
            monitorTemplatesList = monitorTemplatesService.getMonitorTemplates();
            return Result.SUCCESS(monitorTemplatesList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

}