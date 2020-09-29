package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.MediaTypeParams;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
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

import javax.servlet.http.HttpServletRequest;
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
    public Result getMediaTypes(@RequestBody MediaTypeParams params , HttpServletRequest req) {

        try {
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
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
    public Result updateStatus(@RequestParam String mediatypeid, @RequestParam("status") boolean status , HttpServletRequest req) {
        try {
            if (StringUtils.isNotBlank(mediatypeid)) {
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
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
                    zabbixUpdateMediaTypeDTO.setMaxsessions(zabbixGetMediaTypeDTOList.get(0).getMaxsessions());
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
    public Result addMediaType(@RequestBody MediaTypeParams params, HttpServletRequest req) {
        try {
            if (params != null) {
                ZabbixCreateMediaTypeDTO zabbixCreateMediaTypeDTO = new ZabbixCreateMediaTypeDTO();
                BeanUtils.copyProperties(params, zabbixCreateMediaTypeDTO);
                zabbixCreateMediaTypeDTO.setType(MediaTypeType.fromValue(params.getType()));
                zabbixCreateMediaTypeDTO.setSmtpSecurity(MediaTypeSmtpSecurity.fromValue(params.getSmtpSecurity()));
                zabbixCreateMediaTypeDTO.setSmtpAuthentication(params.getSmtpAuthentication() == 0 ? false : true);
                zabbixCreateMediaTypeDTO.setContentType(params.getContentType() == 0 ? false : true);
                zabbixCreateMediaTypeDTO.setStatus(!params.isStatus());
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
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
    public Result deleteMediaType(@PathVariable String id , HttpServletRequest req) {
        try {
            if (StringUtils.isNotBlank(id)) {
                List<String> mediaTypeIds = new ArrayList<>(1);
                mediaTypeIds.add(id);
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
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

    @ResponseBody
    @PostMapping(value = "/findByMediaTypeId/{id}")
    public Result findByMediaTypeId(@PathVariable String id , HttpServletRequest req) {
        if (StringUtils.isNotBlank(id)) {
            try {
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                ZabbixGetMediaTypeParams zabbixGetMediaTypeParams = new ZabbixGetMediaTypeParams();
                List<String> mediatypeIds = new ArrayList<>(1);
                mediatypeIds.add(id);
                zabbixGetMediaTypeParams.setMediatypeIds(mediatypeIds);
                zabbixGetMediaTypeParams.setOutput("extend");
                List<ZabbixGetMediaTypeDTO> zabbixGetMediaTypeDTOList = zabbixMediaTypeService.get(zabbixGetMediaTypeParams, auth);
                if (zabbixGetMediaTypeDTOList != null && !zabbixGetMediaTypeDTOList.isEmpty()) {
                    return Result.SUCCESS(zabbixGetMediaTypeDTOList.get(0));
                    //I just want serialized json data not unserialize json,so use JSONObject to change json data
                    //return Result.SUCCESS(JSONObject.toJSON(zabbixGetMediaTypeDTOList.get(0)));
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
            }
        } else {
            return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
        }
    }

    @PutMapping("/updateMediaType/{id}")
    public Result updateMediaType(@PathVariable String id, @RequestBody MediaTypeParams params, HttpServletRequest req) {
        try {
            if (params != null && StringUtils.isNotBlank(id)) {
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                ZabbixGetMediaTypeParams zabbixGetMediaTypeParams = new ZabbixGetMediaTypeParams();
                List<String> mediatypeIds = new ArrayList<>(1);
                mediatypeIds.add(id);
                zabbixGetMediaTypeParams.setMediatypeIds(mediatypeIds);
                zabbixGetMediaTypeParams.setOutput("extend");
                List<ZabbixGetMediaTypeDTO> zabbixGetMediaTypeDTOList = zabbixMediaTypeService.get(zabbixGetMediaTypeParams, auth);
                if (zabbixGetMediaTypeDTOList != null && !zabbixGetMediaTypeDTOList.isEmpty()) {
                    ZabbixUpdateMediaTypeDTO zabbixUpdateMediaTypeDTO = new ZabbixUpdateMediaTypeDTO();
                    BeanUtils.copyProperties(params, zabbixUpdateMediaTypeDTO);
                    zabbixUpdateMediaTypeDTO.setId(id);
                    zabbixUpdateMediaTypeDTO.setType(zabbixGetMediaTypeDTOList.get(0).getType());
                    zabbixUpdateMediaTypeDTO.setSmtpSecurity(MediaTypeSmtpSecurity.fromValue(params.getSmtpSecurity()));
                    zabbixUpdateMediaTypeDTO.setSmtpAuthentication(params.getSmtpAuthentication() == 0 ? false : true);
                    zabbixUpdateMediaTypeDTO.setContentType(params.getContentType() == 0 ? false : true);
                    zabbixUpdateMediaTypeDTO.setStatus(!params.isStatus());
                    String mediatypeid = zabbixMediaTypeService.update(zabbixUpdateMediaTypeDTO, auth);
                    if (StringUtils.isNotBlank(mediatypeid)) {
                        return Result.SUCCESS(mediatypeid);
                    } else {
                        return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                    }
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}