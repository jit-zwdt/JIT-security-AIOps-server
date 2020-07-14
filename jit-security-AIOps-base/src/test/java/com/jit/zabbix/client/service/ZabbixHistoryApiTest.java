package com.jit.zabbix.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixHistoryDTO;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.request.ZabbixGetHistoryParams;
import com.jit.zabbix.client.request.ZabbixGetItemParams;
import com.jit.zabbix.client.request.ZabbixGetTriggerParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author Mamadou Lamine NIANG
 **/
//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
@RunWith(SpringRunner.class)
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
@SpringBootTest
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
//@WebAppConfiguration
public class ZabbixHistoryApiTest {

    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixItemService zabbixItemService;
    @Autowired
    private ZabbixHistoryService zabbixHistoryService;

    @Test
    public void testGetTrigger() throws Exception {
        String authToken = zabbixApiService.authenticate("Admin", "zabbix");
        System.out.println("authToken: " + authToken);

        ZabbixGetItemParams itemParams = new ZabbixGetItemParams();
        itemParams.setOutput(Arrays.asList(new String[]{"itemid","hostid","key_"}));
        itemParams.setHostIds(Arrays.asList(new String[]{"10407","10392"}));
        Map<String, Object> filter = new HashMap<>();
        filter.put("key_","mysql.com_select.rate");
        itemParams.setFilter(filter);

        List<ZabbixGetItemDTO> itemList = zabbixItemService.get(itemParams, authToken);

        List<String> itemIds = new ArrayList<>();
        for(ZabbixGetItemDTO dto : itemList){
            itemIds.add(dto.getId());
        }

        ZabbixGetHistoryParams historyParams = new ZabbixGetHistoryParams();
        historyParams.setHistory(0);
        historyParams.setItemIds(itemIds);
        historyParams.setSortFields(Arrays.asList(new String[]{"clock","itemid"}));
        historyParams.setSortOrder(Arrays.asList(new String[]{"DESC","ASC"}));
        historyParams.setLimit(2);

        List<ZabbixHistoryDTO> historyList = zabbixHistoryService.get(historyParams, authToken);

        try {
            System.out.println("testGetItem:"+new ObjectMapper().writeValueAsString(itemList));
            System.out.println("testGetHistory:"+new ObjectMapper().writeValueAsString(historyList));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
