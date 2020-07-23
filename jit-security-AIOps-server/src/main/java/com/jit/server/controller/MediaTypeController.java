package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.MediaTypeParams;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixGetMediaTypeDTO;
import com.jit.zabbix.client.request.ZabbixGetMediaTypeParams;
import com.jit.zabbix.client.service.ZabbixMediaTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.07.22
 */

@RestController
@RequestMapping("/mediaType")
public class MediaTypeController {

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ZabbixMediaTypeService zabbixMediaTypeService;


    @ResponseBody
    @PostMapping(value = "/getMediaTypes")
    public Result getMediaTypes(@RequestBody MediaTypeParams params) {

        try {
            String auth = zabbixAuthService.getAuth();
            ZabbixGetMediaTypeParams zabbixGetMediaTypeParams = new ZabbixGetMediaTypeParams();
            if (params != null) {
                String name = params.getName();
                String status = params.getStatus();
                if (StringUtils.isNotBlank(name)) {
                    Map<String, Object> searchMap = new HashMap<>();
                    searchMap.put("name", name);
                    zabbixGetMediaTypeParams.setSearch(searchMap);
                    zabbixGetMediaTypeParams.setStartSearch(false);
                }
                if (StringUtils.isNotBlank(status)) {
                    Map<String, Object> filterMap = new HashMap<>();
                    filterMap.put("status", status);
                    zabbixGetMediaTypeParams.setFilter(filterMap);
                }
            }
            zabbixGetMediaTypeParams.setOutput("extend");
            List<ZabbixGetMediaTypeDTO> zabbixGetMediaTypeDTOList = zabbixMediaTypeService.get(zabbixGetMediaTypeParams, auth);
            return Result.SUCCESS(zabbixGetMediaTypeDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }


}