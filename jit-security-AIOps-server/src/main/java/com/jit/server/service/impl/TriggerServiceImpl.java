package com.jit.server.service.impl;


import com.jit.server.request.TriggerParams;
import com.jit.server.service.TriggerService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateTriggerDTO;
import com.jit.zabbix.client.request.ZabbixGetTriggerParams;
import com.jit.zabbix.client.service.ZabbixTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TriggerServiceImpl implements TriggerService {

    @Autowired
    private ZabbixTriggerService zabbixTriggerService;
    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Override
    public List<ZabbixTriggerDTO> findByCondition(TriggerParams params) throws Exception {
        if (params == null) {
            return null;
        }
        //获得token
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)){
            return null;
        }
        ZabbixGetTriggerParams _params = new ZabbixGetTriggerParams();
        _params.setHostIds(Arrays.asList(new String[]{params.getHostId()}));
        if(params.getDescription()!=null&&!"".equals(params.getDescription().trim())){
            Map<String, Object> search = new HashMap<>();
            search.put("description",params.getDescription().trim());
            _params.setSearch(search);
        }
        if(params.getStatus()!=null&&("0".equals(params.getStatus().trim())||"1".equals(params.getStatus().trim()))){
            Map<String, Object> filter = new HashMap<>();
            filter.put("status",Integer.parseInt(params.getStatus().trim()));
            _params.setFilter(filter);
        }

        return zabbixTriggerService.get(_params, authToken);
    }

    @Override
    public String updateTriggerStatus(String triggerId, String status) throws Exception {
        if(StringUtils.isEmpty(triggerId) || StringUtils.isEmpty(status)){
            return null;
        }
        if(!"0".equals(status.trim())&&!"1".equals(status.trim())){
            return null;
        }
        //主机信息
        ZabbixUpdateTriggerDTO dto = new ZabbixUpdateTriggerDTO();
        dto.setId(triggerId.trim());
        dto.setStatus("1".equals(status.trim())?true:false);
        //获得token
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)){
            return null;
        }
        return zabbixTriggerService.update(dto, authToken);
    }
}
