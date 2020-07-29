package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.MediaTypeParams;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixCreateMediaTypeDTO;
import com.jit.zabbix.client.dto.ZabbixGetMediaTypeDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateMediaTypeDTO;
import com.jit.zabbix.client.model.mediaType.MediaTypeSmtpSecurity;
import com.jit.zabbix.client.model.mediaType.MediaTypeType;
import com.jit.zabbix.client.request.ZabbixGetMediaTypeParams;
import com.jit.zabbix.client.service.ZabbixMediaTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
                String status = params.isStatus() == true ? "1" : "0";
                boolean flag = params.isFlag();
                if (StringUtils.isNotBlank(name)) {
                    Map<String, Object> searchMap = new HashMap<>();
                    searchMap.put("name", name);
                    zabbixGetMediaTypeParams.setSearch(searchMap);
                    zabbixGetMediaTypeParams.setStartSearch(false);
                }
                if (StringUtils.isNotBlank(status) && !flag) {
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
    public Result updateHostEnableMonitor(@RequestParam String mediatypeid, @RequestParam("status") boolean status) {
        try {
            if (StringUtils.isNotBlank(mediatypeid)) {
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
                    zabbixUpdateMediaTypeDTO.setStatus(status);
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

    @PostMapping("/addMediaType")
    public Result addMediaType(@RequestBody MediaTypeParams params) {
        try {
            if (params != null) {
                ZabbixCreateMediaTypeDTO zabbixCreateMediaTypeDTO = new ZabbixCreateMediaTypeDTO();
                BeanUtils.copyProperties(params, zabbixCreateMediaTypeDTO);
                zabbixCreateMediaTypeDTO.setType(MediaTypeType.fromValue(params.getType()));
                zabbixCreateMediaTypeDTO.setSmtpSecurity(MediaTypeSmtpSecurity.fromValue(params.getSmtpSecurity()));
                zabbixCreateMediaTypeDTO.setSmtpAuthentication(params.getSmtpAuthentication() == 0 ? false : true);
                zabbixCreateMediaTypeDTO.setContentType(params.getContentType() == 0 ? false : true);
                zabbixCreateMediaTypeDTO.setStatus(!params.isStatus());
                String auth = zabbixAuthService.getAuth();
                String mediaTypeId = zabbixMediaTypeService.create(zabbixCreateMediaTypeDTO, auth);
                if (StringUtils.isNotBlank(mediaTypeId)) {
                    return Result.SUCCESS(mediaTypeId);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteMediaType/{id}")
    public Result deleteMediaType(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                List<String> mediaTypeIds = new ArrayList<>(1);
                mediaTypeIds.add(id);
                String auth = zabbixAuthService.getAuth();
                List<String> ids = zabbixMediaTypeService.delete(mediaTypeIds, auth);
                if (ids != null && !ids.isEmpty()) {
                    return Result.SUCCESS(ids);
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