package com.jit.server.service.impl;


import com.jit.server.request.GraphItemParams;
import com.jit.server.request.GraphPrototypeParams;
import com.jit.server.service.GraphItemService;
import com.jit.server.service.GraphPrototypeService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetGraphItemDTO;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;
import com.jit.zabbix.client.request.ZabbixGetGraphItemParams;
import com.jit.zabbix.client.request.ZabbixGetGraphPrototypeParams;
import com.jit.zabbix.client.service.ZabbixGraphItemService;
import com.jit.zabbix.client.service.ZabbixGraphPrototypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphItemServiceImpl implements GraphItemService {

    @Autowired
    private ZabbixGraphItemService zabbixGraphItemService;
    @Autowired
    private ZabbixAuthService zabbixAuthService;
    public static final String GITEM_STATUS = "status";
    public static final String GITEM_NAME = "name";

    @Override
    public List<ZabbixGetGraphItemDTO> getGItemList(GraphItemParams graphItemParams) throws Exception {
        if (graphItemParams == null) {
            return null;
        }
        //获得token
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }
        ZabbixGetGraphItemParams params = new ZabbixGetGraphItemParams();
        List<String> gItemIds = graphItemParams.getGitemids();
        if (gItemIds != null && !CollectionUtils.isEmpty(gItemIds)) {
            params.setGItemIds(gItemIds);
        }
        List<String> graphIds = graphItemParams.getGraphids();
        if (graphIds != null && !CollectionUtils.isEmpty(graphIds)) {
            params.setGraphIds(graphIds);
        }
        List<String> itemIds = graphItemParams.getItemids();
        if (itemIds != null && !CollectionUtils.isEmpty(itemIds)) {
            params.setItemIds(itemIds);
        }
        Integer type = graphItemParams.getType();
        if(type != null){
            params.setType(type);
        }
        if (StringUtils.isNotEmpty(graphItemParams.getName())) {
            Map mapSearch = new HashMap();
            mapSearch.put(GITEM_NAME, graphItemParams.getName());
            params.setSearch(mapSearch);
        }
        if (StringUtils.isNotEmpty(graphItemParams.getStatus())) {

            Map mapFilter = new HashMap();
            mapFilter.put(GITEM_STATUS, graphItemParams.getStatus());
            params.setFilter(mapFilter);
        }
        return zabbixGraphItemService.get(params, authToken);
    }
}
