package com.jit.server.service.impl;


import com.jit.server.request.ProblemParams;
import com.jit.server.request.TriggerConditionParams;
import com.jit.server.request.TriggerParams;
import com.jit.server.service.ProblemService;
import com.jit.server.service.TriggerService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateTriggerDTO;
import com.jit.zabbix.client.model.trigger.TriggerPriority;
import com.jit.zabbix.client.request.ZabbixGetTriggerParams;
import com.jit.zabbix.client.service.ZabbixTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class TriggerServiceImpl implements TriggerService {

    @Autowired
    private ZabbixTriggerService zabbixTriggerService;
    @Autowired
    private ZabbixAuthService zabbixAuthService;

    public static final String TRIGGER_EXTEND = "extend";

    @Override
    public List<ZabbixTriggerDTO> findByCondition(TriggerParams params, String auth) throws Exception {
        if (params == null) {
            return null;
        }
        ZabbixGetTriggerParams _params = new ZabbixGetTriggerParams();
        _params.setHostIds(Arrays.asList(new String[]{params.getHostId()}));
        if (params.getDescription() != null && !"".equals(params.getDescription().trim())) {
            Map<String, Object> search = new HashMap<>();
            search.put("description", params.getDescription().trim());
            _params.setSearch(search);
        }
        if (params.getStatus() != null && ("0".equals(params.getStatus().trim()) || "1".equals(params.getStatus().trim()))) {
            Map<String, Object> filter = new HashMap<>();
            filter.put("status", Integer.parseInt(params.getStatus().trim()));
            _params.setFilter(filter);
        }

        return zabbixTriggerService.get(_params, auth);
    }

    @Override
    public String updateTriggerStatus(String triggerId, String status,String auth) throws Exception {
        if (StringUtils.isEmpty(triggerId) || StringUtils.isEmpty(status)) {
            return null;
        }
        if (!"0".equals(status.trim()) && !"1".equals(status.trim())) {
            return null;
        }
        //主机信息
        ZabbixUpdateTriggerDTO dto = new ZabbixUpdateTriggerDTO();
        dto.setId(triggerId.trim());
        dto.setStatus("1".equals(status.trim()) ? true : false);
        return zabbixTriggerService.update(dto, auth);
    }

    @Override
    public String updateTriggerPriority(String triggerId, String priority,String auth) throws Exception {
        if (StringUtils.isEmpty(priority) || StringUtils.isEmpty(priority)) {
            return null;
        }
        //主机信息
        ZabbixUpdateTriggerDTO dto = new ZabbixUpdateTriggerDTO();
        dto.setId(triggerId.trim());
        dto.setPriority(TriggerPriority.fromValue(Integer.parseInt(priority)));
        return zabbixTriggerService.update(dto, auth);
    }

    @Override
    public List<ZabbixTriggerDTO> findTriggerAll(TriggerParams params,String auth) throws Exception {

        ZabbixGetTriggerParams paramsTrigger = new ZabbixGetTriggerParams();
        if (params.getDescription() != null && !"".equals(params.getDescription().trim())) {
            Map<String, Object> search = new HashMap<>();
            search.put("description", params.getDescription().trim());
            paramsTrigger.setSearch(search);
        }
        if (params.getStatus() != null && ("0".equals(params.getStatus().trim()) || "1".equals(params.getStatus().trim()))) {
            Map<String, Object> filter = new HashMap<>();
            filter.put("status", Integer.parseInt(params.getStatus().trim()));
            paramsTrigger.setFilter(filter);
        }
        paramsTrigger.setOutput(TRIGGER_EXTEND);
        paramsTrigger.setSelectFunctions(TRIGGER_EXTEND);

        return zabbixTriggerService.get(paramsTrigger, auth);
    }

    @Override
    public List<ZabbixTriggerDTO> findTriggerById(String[] params,String auth) throws Exception {

        ZabbixGetTriggerParams paramsTrigger = new ZabbixGetTriggerParams();
        if (params != null) {
            Map<String, Object> filter = new HashMap<>();
            filter.put("triggerid", params);
            paramsTrigger.setFilter(filter);
        }
        paramsTrigger.setOutput(TRIGGER_EXTEND);
        paramsTrigger.setSelectFunctions(TRIGGER_EXTEND);

        return zabbixTriggerService.get(paramsTrigger, auth);
    }

    @Override
    public List<ZabbixTriggerDTO> getStateList(List<ZabbixTriggerDTO> result) {
        int i = 0;
        while(i < result.size()){
            ZabbixTriggerDTO z = result.get(i);
            if(z.isState() == true){
                result.remove(i);
            }else{
                i++;
            }
        }
        return result;
    }
}
