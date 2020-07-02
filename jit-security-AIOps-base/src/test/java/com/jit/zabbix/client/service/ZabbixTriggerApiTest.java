package com.jit.zabbix.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.model.host.HostMacro;
import com.jit.zabbix.client.model.host.InterfaceType;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.model.template.ZabbixTemplate;
import com.jit.zabbix.client.model.trigger.ZabbixTrigger;
import com.jit.zabbix.client.request.ZabbixGetHostInterfaceParams;
import com.jit.zabbix.client.request.ZabbixGetHostParams;
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
public class ZabbixTriggerApiTest {

    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixTriggerService zabbixTriggerService;

    @Test
    public void testGetTrigger() throws Exception {


        //zabbixHostService.get()
        ZabbixGetTriggerParams params = new ZabbixGetTriggerParams();

        String authToken = zabbixApiService.authenticate("Admin", "zabbix");
        System.out.println("authToken: " + authToken);
        params.setHostIds(Arrays.asList(new String[]{"10366"}));
        Map<String, Object> serch = new HashMap<>();
        serch.put("description","Configured");
        params.setSearch(serch);
        Map<String, Object> filter = new HashMap<>();
        filter.put("status",0);
        params.setFilter(filter);

        List<ZabbixTrigger> list = zabbixTriggerService.get(params, authToken);

        try {
            System.out.println("testGetTrigger:"+new ObjectMapper().writeValueAsString(list));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
