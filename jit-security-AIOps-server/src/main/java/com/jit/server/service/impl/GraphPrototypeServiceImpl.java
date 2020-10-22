package com.jit.server.service.impl;


import com.jit.server.request.CreateGraphPrototypeParams;
import com.jit.server.request.GraphPrototypeParams;
import com.jit.server.request.ItemParams;
import com.jit.server.service.GraphPrototypeService;
import com.jit.server.service.ItemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateItemDTO;
import com.jit.zabbix.client.model.graph.GraphItem;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;
import com.jit.zabbix.client.request.ZabbixGetGraphPrototypeParams;
import com.jit.zabbix.client.request.ZabbixGetItemParams;
import com.jit.zabbix.client.service.ZabbixGraphPrototypeService;
import com.jit.zabbix.client.service.ZabbixItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphPrototypeServiceImpl implements GraphPrototypeService {

    @Autowired
    private ZabbixGraphPrototypeService zabbixGraphPrototypeService;
    @Autowired
    private ZabbixAuthService zabbixAuthService;
    public static final String GPRO_STATUS = "status";
    public static final String GPRO_NAME = "name";

    @Override
    public List<ZabbixGetGraphPrototypeDTO> getGProList(GraphPrototypeParams graphPrototypeParams , String auth) throws Exception {
        if (graphPrototypeParams == null) {
            return null;
        }
        ZabbixGetGraphPrototypeParams params = new ZabbixGetGraphPrototypeParams();
        List<String> discoveryIds = graphPrototypeParams.getDiscoveryids();
        if (discoveryIds != null && !CollectionUtils.isEmpty(discoveryIds)) {
            params.setDiscoveryIds(discoveryIds);
        }
        if (StringUtils.isNotEmpty(graphPrototypeParams.getName())) {
            Map mapSearch = new HashMap();
            mapSearch.put(GPRO_NAME, graphPrototypeParams.getName());
            params.setSearch(mapSearch);
        }
        if (StringUtils.isNotEmpty(graphPrototypeParams.getStatus())) {

            Map mapFilter = new HashMap();
            mapFilter.put(GPRO_STATUS, graphPrototypeParams.getStatus());
            params.setFilter(mapFilter);
        }
        List<String> hostIds = graphPrototypeParams.getHostids();
        if (hostIds != null && !CollectionUtils.isEmpty(hostIds)) {
            params.setHostIds(hostIds);
        }
        List<String> graphIds = graphPrototypeParams.getGraphids();
        if (graphIds != null && !CollectionUtils.isEmpty(graphIds)) {
            params.setGraphIds(graphIds);
        }
        List<String> groupIds = graphPrototypeParams.getGroupids();
        if (groupIds != null && !CollectionUtils.isEmpty(groupIds)) {
            params.setGroupIds(groupIds);
        }
        List<String> templateIds = graphPrototypeParams.getTemplateids();
        if (templateIds != null && !CollectionUtils.isEmpty(templateIds)) {
            params.setTemplateIds(templateIds);
        }
        List<String> itemIds = graphPrototypeParams.getItemids();
        if (itemIds != null && !CollectionUtils.isEmpty(itemIds)) {
            params.setItemIds(itemIds);
        }
        return zabbixGraphPrototypeService.get(params, auth);
    }

    @Override
    public List<String> createGPro(ZabbixCreateGraphPrototypeParams zabbixCreateGraphPrototypeParams,String auth) throws Exception {
        if(CollectionUtils.isEmpty(zabbixCreateGraphPrototypeParams.getGitems())){
            return null;
        }
        List<GraphItem> list = zabbixCreateGraphPrototypeParams.getGitems();
        for(GraphItem g:list){
            g.setColor(g.getColor().substring(1));
        }

        return zabbixGraphPrototypeService.create(zabbixCreateGraphPrototypeParams,auth);
    }

    @Override
    public List<String> deleteGPro(String graphid, String auth) throws Exception {
        if(graphid.isEmpty()){
            return null;
        }

        return zabbixGraphPrototypeService.delete(graphid,auth);
    }
}
