package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.MediaTypeParams;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixGetMediaTypeDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateMediaTypeDTO;
import com.jit.zabbix.client.request.ZabbixGetMediaTypeParams;
import com.jit.zabbix.client.service.ZabbixMediaTypeService;
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

    @PutMapping("/updateStatus")
    public Result updateHostEnableMonitor(@RequestParam String mediatypeid, @RequestParam("status") String status) {
        try {
            if (StringUtils.isNotBlank(mediatypeid) && StringUtils.isNotBlank(status)) {
                String auth = zabbixAuthService.getAuth();
                ZabbixGetMediaTypeParams zabbixGetMediaTypeParams = new ZabbixGetMediaTypeParams();
                List<String> mediatypeIds = new ArrayList<>(1);
                mediatypeIds.add(mediatypeid);
                zabbixGetMediaTypeParams.setMediatypeIds(mediatypeIds);
                zabbixGetMediaTypeParams.setOutput("extend");
                List<ZabbixGetMediaTypeDTO> zabbixGetMediaTypeDTOList = zabbixMediaTypeService.get(zabbixGetMediaTypeParams, auth);
                if (zabbixGetMediaTypeDTOList != null && !zabbixGetMediaTypeDTOList.isEmpty()) {
                    ZabbixUpdateMediaTypeDTO zabbixUpdateMediaTypeDTO = new ZabbixUpdateMediaTypeDTO();
                    zabbixUpdateMediaTypeDTO.setId(mediatypeid);
                    zabbixUpdateMediaTypeDTO.setStatus("0".equals(status) ? false : true);
                    zabbixUpdateMediaTypeDTO.setMaxattempts(zabbixGetMediaTypeDTOList.get(0).getMaxattempts());
                    mediatypeid = zabbixMediaTypeService.update(zabbixUpdateMediaTypeDTO, auth);
                    return Result.SUCCESS(mediatypeid);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

}