package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorTemplatesEntity;
import com.jit.server.request.MonitorTemplatesParams;
import com.jit.server.service.MonitorTemplatesService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.request.ZabbixGetTemplateParams;
import com.jit.zabbix.client.service.ZabbixTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ZabbixTemplateService zabbixTemplateService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @ResponseBody
    @PostMapping(value = "/getMonitorTemplates")
    public Result getMonitorTemplates(@RequestBody PageRequest<MonitorTemplatesParams> params) {

        try {
            Page<MonitorTemplatesEntity> monitorTemplatesList = monitorTemplatesService.getMonitorTemplates(params);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("page", params.getPage());
            result.put("size", params.getSize());
            result.put("totalRow", monitorTemplatesList.getTotalElements());
            result.put("totalPage", monitorTemplatesList.getTotalPages());
            result.put("dataList", monitorTemplatesList.getContent());
            return Result.SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    /**
     * get zabbix template
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getTemplates")
    public Result getMonitorTemplates(@RequestHeader String authorization) {
        try {
            String auth = zabbixAuthService.getAuth();
            ZabbixGetTemplateParams params = new ZabbixGetTemplateParams();
            params.setOutput("extend");
            List<ZabbixHostDTO> zabbixHostDTOList = zabbixTemplateService.get(params, auth);
            return Result.SUCCESS(zabbixHostDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }


}