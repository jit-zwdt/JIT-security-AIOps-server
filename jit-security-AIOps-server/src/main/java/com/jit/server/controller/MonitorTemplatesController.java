package com.jit.server.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorTemplatesEntity;
import com.jit.server.pojo.MonitorTypeEntity;
import com.jit.server.request.MonitorTemplatesParams;
import com.jit.server.service.MonitorTemplatesService;
import com.jit.server.service.MonitorTypeService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixGetTemplateDTO;
import com.jit.zabbix.client.request.ZabbixGetTemplateParams;
import com.jit.zabbix.client.service.ZabbixTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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

    @Autowired
    private MonitorTypeService monitorTypeService;

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
    @PostMapping(value = "/getZabbixTemplates")
    public Result getZabbixTemplates() {
        try {
            String auth = zabbixAuthService.getAuth();
            ZabbixGetTemplateParams params = new ZabbixGetTemplateParams();
            params.setOutput("extend");
            List<ZabbixGetTemplateDTO> zabbixGetTemplateDTOList = zabbixTemplateService.get(params, auth);
            return Result.SUCCESS(zabbixGetTemplateDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PostMapping(value = "/bindTemplates")
    public Result bindTemplates(@RequestParam String id, @RequestParam String templates) {
        try {
            if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(templates)) {
                MonitorTemplatesEntity monitorTemplatesEntity = monitorTemplatesService.getMonitorTemplate(id);
                if (monitorTemplatesEntity != null) {
                    monitorTemplatesEntity.setTemplates(templates);
                    monitorTemplatesEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                    monitorTemplatesService.updateMonitorTemplate(monitorTemplatesEntity);
                    return Result.SUCCESS("success");
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    /**
     * get templates
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getTemplates")
    public Result getTemplates(@RequestParam String keyword, @RequestParam String type) {
        try {
            JSONArray jsonArray = new JSONArray();
            List<MonitorTypeEntity> monitorTypeEntityList;
            if (StringUtils.isNotBlank(type)) {
                MonitorTypeEntity monitorTypeEntity = monitorTypeService.getMonitorTypesById(type);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", monitorTypeEntity.getType());
                String typeId = monitorTypeEntity.getId();
                List<MonitorTemplatesEntity> monitorTemplatesEntityList;
                if (StringUtils.isNotBlank(keyword)) {
                    monitorTemplatesEntityList = monitorTemplatesService.getMonitorTemplatesByTypeIdAndNameLike(typeId, keyword);
                } else {
                    monitorTemplatesEntityList = monitorTemplatesService.getMonitorTemplatesByTypeId(typeId);
                }
                jsonObject.put("templates", monitorTemplatesEntityList);
                jsonArray.add(jsonObject);
            } else {
                monitorTypeEntityList = monitorTypeService.getMonitorTypes();
                if (monitorTypeEntityList != null && !monitorTypeEntityList.isEmpty()) {
                    for (MonitorTypeEntity monitorTypeEntity : monitorTypeEntityList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", monitorTypeEntity.getType());
                        String typeId = monitorTypeEntity.getId();
                        List<MonitorTemplatesEntity> monitorTemplatesEntityList;
                        if (StringUtils.isNotBlank(keyword)) {
                            monitorTemplatesEntityList = monitorTemplatesService.getMonitorTemplatesByTypeIdAndNameLike(typeId, keyword);
                        } else {
                            monitorTemplatesEntityList = monitorTemplatesService.getMonitorTemplatesByTypeId(typeId);
                        }
                        jsonObject.put("templates", monitorTemplatesEntityList);
                        jsonArray.add(jsonObject);
                    }
                }
            }
            return Result.SUCCESS(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

}