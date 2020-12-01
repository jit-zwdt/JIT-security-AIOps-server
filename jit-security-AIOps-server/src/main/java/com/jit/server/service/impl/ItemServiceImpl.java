package com.jit.server.service.impl;


import com.jit.server.request.ItemParams;
import com.jit.server.service.ItemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateItemDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateTriggerDTO;
import com.jit.zabbix.client.request.ZabbixGetItemParams;
import com.jit.zabbix.client.service.ZabbixItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ZabbixItemService zabbixItemService;
    @Autowired
    private ZabbixAuthService zabbixAuthService;
    public static final String ITEM_EXTEND = "extend";
    public static final String ITEM_STATUS = "status";
    public static final String ITEM_KEY_ = "key_";
    public static final String ITEM_NAME = "name";
    public static final List<String> ITEM_ARRAY_NAME = Collections.singletonList("name");

    @Override
    public List<ZabbixGetItemDTO> getItemInfoList(ItemParams itemParams,String auth) throws Exception {
        if (itemParams == null) {
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
            String key_ = itemParams.getKey_();
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
            if (StringUtils.isNotEmpty(key_)) {

                Map mapFilter = new HashMap();
                mapFilter.put(ITEM_KEY_, itemParams.getKey_());
                params.setFilter(mapFilter);
            }
        }
        List<String> itemids = itemParams.getItemids();
        if (itemids != null && !CollectionUtils.isEmpty(itemids)) {
            params.setItemIds(itemids);

        }
        return zabbixItemService.get(params, auth);
    }

    @Override
    public String updateItemStatus(String itemId, String status ,String auth) throws Exception {
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
        return zabbixItemService.update(dto, auth);
    }
    @Override
    public List<ZabbixGetItemDTO> getStateList(List<ZabbixGetItemDTO> result) {
        int i = 0;
        while(i < result.size()){
            ZabbixGetItemDTO z = result.get(i);
            if(z.isState() == true){
                result.remove(i);
            }else{
                i++;
            }
        }
        return result;
    }

}
