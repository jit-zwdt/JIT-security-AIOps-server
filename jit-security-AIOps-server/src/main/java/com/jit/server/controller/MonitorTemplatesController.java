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
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixGetTemplateDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.request.ZabbixGetItemParams;
import com.jit.zabbix.client.request.ZabbixGetTemplateParams;
import com.jit.zabbix.client.service.ZabbixItemService;
import com.jit.zabbix.client.service.ZabbixTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

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

    @Autowired
    private ZabbixItemService zabbixItemService;

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
    public Result bindTemplates(@RequestParam String id, @RequestParam String templates, @RequestParam String templateIds) {
        try {
            if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(templates) && StringUtils.isNotBlank(templateIds)) {
                MonitorTemplatesEntity monitorTemplatesEntity = monitorTemplatesService.getMonitorTemplate(id);
                if (monitorTemplatesEntity != null) {
                    monitorTemplatesEntity.setTemplateIds(templateIds);
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

    @ResponseBody
    @PostMapping(value = "/checkItems")
    public Result checkItems(@RequestParam String templates) {
        try {
            if (StringUtils.isNotBlank(templates)) {
                //error msg
                StringBuffer msg = new StringBuffer(1024);
                Map<String, String> titleMap = new HashMap<>();
                String[] temps = templates.split(",");
                List<String> tempList;
                String auth = zabbixAuthService.getAuth();
                List<List<ZabbixGetItemDTO>> list = new ArrayList<>(temps.length);
                ZabbixGetItemParams zabbixGetItemParams = new ZabbixGetItemParams();
                zabbixGetItemParams.setOutput("extend");
                //set error group title
                Map<String, String> templateidMap = templateidMap(templates, auth);
                for (int m = 0, ll = temps.length; m < ll; m++) {
                    for (int n = m + 1; n < ll; n++) {
                        titleMap.put(temps[m] + "&&" + temps[n], "模版：" + templateidMap.get(temps[m]) + " 与模版：" + templateidMap.get(temps[n]) + " 有监控项冲突，监控项：");
                    }
                }
                //get itemList
                for (String t : temps) {
                    tempList = new ArrayList<>();
                    tempList.add(t);
                    zabbixGetItemParams.setTemplateIds(tempList);
                    List<ZabbixGetItemDTO> zabbixGetItemDTOList = zabbixItemService.get(zabbixGetItemParams, auth);
                    list.add(zabbixGetItemDTOList);
                }
                // do check
                List<String> bodyList;
                List<ZabbixGetItemDTO> listA;
                List<ZabbixGetItemDTO> listB;
                boolean flag;
                String title = "";
                for (int i = 0, len = list.size(); i < len; i++) {
                    listA = list.get(i);
                    for (int j = i + 1; j < len; j++) {
                        listB = list.get(j);
                        flag = false;
                        bodyList = new ArrayList<>();
                        for (ZabbixGetItemDTO dtoA : listA) {
                            for (ZabbixGetItemDTO dtoB : listB) {
                                if (dtoA.getKey_().equals(dtoB.getKey_())) {
                                    title = dtoA.getHostId() + "&&" + dtoB.getHostId();
                                    flag = true;
                                    bodyList.add(dtoA.getKey_());
                                }
                            }
                        }
                        if (flag) {
                            msg.append(titleMap.get(title));
                            msg.append(bodyList.toString() + " ");
                        }
                    }
                }
                if (msg.length() > 0) {
                    return Result.SUCCESS(msg.toString());
                } else {
                    return Result.SUCCESS("success");
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    private Map<String, String> templateidMap(String templateids, String auth) throws ZabbixApiException {
        Map<String, String> res = new HashMap<>(templateids.split(",").length);
        ZabbixGetTemplateParams params = new ZabbixGetTemplateParams();
        params.setOutput("extend");
        params.setTemplateIds(Arrays.asList(templateids.split(",")));
        List<ZabbixGetTemplateDTO> zabbixGetTemplateDTOList = zabbixTemplateService.get(params, auth);
        for (ZabbixGetTemplateDTO zabbixGetTemplateDTO : zabbixGetTemplateDTOList) {
            res.put(zabbixGetTemplateDTO.getId(), zabbixGetTemplateDTO.getName());
        }
        return res;
    }

}