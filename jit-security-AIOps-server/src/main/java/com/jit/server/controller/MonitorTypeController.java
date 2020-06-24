package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorTypeEntity;
import com.jit.server.service.MonitorTypeService;
import com.jit.server.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ResponseBody
    @PostMapping(value = "/getJsonTypes")
    public Result getJsonTypes(@RequestParam String ids) {
        try {
            List<Map<String, String>> res = null;
            if (StringUtils.isNotBlank(ids)) {
                res = new ArrayList<>();
                String[] arrId = ids.split(",");
                Map<String, String> map;
                for (String id : arrId) {
                    Object type = monitorTypeService.getTypeById(id);
                    if (type != null) {
                        map = new HashMap<>(1);
                        map.put("id", id);
                        map.put("type", type.toString());
                        res.add(map);
                    }
                }
            }
            return Result.SUCCESS(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }


}