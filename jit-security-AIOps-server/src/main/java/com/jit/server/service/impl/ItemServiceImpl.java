package com.jit.server.service.impl;


import com.jit.server.request.ItemParams;
import com.jit.server.service.ItemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateItemDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateTriggerDTO;
import com.jit.zabbix.client.request.ZabbixGetItemParams;
import com.jit.zabbix.client.service.ZabbixItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ZabbixItemService zabbixItemService;
    @Autowired
    private ZabbixAuthService zabbixAuthService;
    public static final String ITEM_EXTEND = "extend";
    public static final String ITEM_STATUS = "status";
    public static final String ITEM_NAME = "name";
    public static final List<String> ITEM_ARRAY_NAME = Collections.singletonList("name");

    @Override
    public List<ZabbixGetItemDTO> getItemInfoList(ItemParams itemParams) throws Exception {
        if (itemParams == null) {
            return null;
        }
        //获得token
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }
        ZabbixGetItemParams params = new ZabbixGetItemParams();
        List<String> hostids = itemParams.getHostids();
        if (hostids != null && !CollectionUtils.isEmpty(hostids)) {
            params.setHostIds(hostids);
            params.setOutput(ITEM_EXTEND);
            params.setSortFields(ITEM_ARRAY_NAME);
            String name = itemParams.getName();
            String status = itemParams.getStatus();
            if (StringUtils.isNotEmpty(name)) {
                Map mapSearch = new HashMap();
                mapSearch.put(ITEM_NAME, itemParams.getName());
                params.setSearch(mapSearch);
            }
            if (StringUtils.isNotEmpty(status)) {

                Map mapFilter = new HashMap();
                mapFilter.put(ITEM_STATUS, itemParams.getStatus());
                params.setFilter(mapFilter);
            }
        }
        return zabbixItemService.get(params, authToken);
    }

    @Override
    public String updateItemStatus(String itemId, String status) throws Exception {
        if(StringUtils.isEmpty(itemId) || StringUtils.isEmpty(status)){
            return null;
        }
        if(!"0".equals(status.trim())&&!"1".equals(status.trim())){
            return null;
        }
        //主机信息
        ZabbixUpdateItemDTO dto = new ZabbixUpdateItemDTO();
        dto.setId(itemId.trim());
        dto.setStatus("1".equals(status.trim())?true:false);
        //获得token
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)){
            return null;
        }
        return zabbixItemService.update(dto, authToken);
    }
}
