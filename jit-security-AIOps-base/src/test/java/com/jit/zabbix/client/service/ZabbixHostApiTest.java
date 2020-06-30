package com.jit.zabbix.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.model.GlobalMacro;
import com.jit.zabbix.client.model.host.*;
import com.jit.zabbix.client.model.template.ZabbixTemplate;
import com.jit.zabbix.client.request.ZabbixGetHostInterfaceParams;
import com.jit.zabbix.client.request.ZabbixGetHostParams;
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
public class ZabbixHostApiTest {

    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixHostService zabbixHostService;
    @Autowired
    private ZabbixHostInterfaceService zabbixHostInterfaceService;

    @Test
    public void testHostCreat() throws Exception {


        ZabbixHostDTO dto = new ZabbixHostDTO();
        dto.setTechnicalName("test8");
        dto.setName("测试主机8");
        dto.setDescription("测试主机7说明");
        dto.setUnmonitored(false);

        List<ZabbixHostGroup> groups = new ArrayList<ZabbixHostGroup>();
        ZabbixHostGroup group = new ZabbixHostGroup();
        group.setId("15");
        /*group.setName("主机群组1");
        group.setFlags(OriginFlag.PLAIN);
        group.setInternal(false);*/
        groups.add(group);

        List<ZabbixHostInterface> interfaces = new ArrayList<ZabbixHostInterface>();
        ZabbixHostInterface _interface = new ZabbixHostInterface();
        _interface.setDns("127.0.0.1");
        //_interface.setHostId("");
        _interface.setIp("127.0.0.1");
        _interface.setMain(true);
        _interface.setPort("10051");
        _interface.setType(InterfaceType.AGENT);
        _interface.setUseIp(true);
        //_interface.isBulk();
        interfaces.add(_interface);

        List<ZabbixTemplate> templates = new ArrayList<ZabbixTemplate>();
        ZabbixTemplate template = new ZabbixTemplate();
        template.setId("10001");
        templates.add(template);
        template = new ZabbixTemplate();
        template.setId("10048");
        templates.add(template);

        List<HostMacro> macros = new ArrayList<HostMacro>();
        HostMacro macro = new HostMacro();
        macro.setMacro("{$USER_ID}");
        macro.setValue("123321");
        macros.add(macro);
        macro = new HostMacro();
        macro.setMacro("{$USER_LOCATION}");
        macro.setValue("0:0:0");
        macros.add(macro);

        dto.setGroups(groups);
        dto.setInterfaces(interfaces);
        dto.setTemplates(templates);
        dto.setMacros(macros);

        String authToken = zabbixApiService.authenticate("Admin", "zabbix");
        System.out.println("authToken: " + authToken);
        String hostid = zabbixHostService.create(dto, authToken);

        //List<String> results = zabbixHostService.update(dto, authToken);

        System.out.println("hostid: " + hostid);
    }

    @Test
    public void testGetHostInterface() throws Exception {


        //zabbixHostInterfaceService.get()
        ZabbixGetHostInterfaceParams params = new ZabbixGetHostInterfaceParams();

        String authToken = zabbixApiService.authenticate("Admin", "zabbix");
        System.out.println("authToken: " + authToken);
        params.setHostIds(Arrays.asList(new String[]{"10354"}));

        List<ZabbixHostInterface> list = zabbixHostInterfaceService.get(params, authToken);

        try {
            System.out.println("testGetHostInterface:"+new ObjectMapper().writeValueAsString(list));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetHost() throws Exception {


        //zabbixHostService.get()
        ZabbixGetHostParams params = new ZabbixGetHostParams();

        String authToken = zabbixApiService.authenticate("Admin", "zabbix");
        System.out.println("authToken: " + authToken);
        params.setHostIds(Arrays.asList(new String[]{"10366","10367"}));

        List<ZabbixHostDTO> list = zabbixHostService.get(params, authToken);

        try {
            System.out.println("testGetHost:"+new ObjectMapper().writeValueAsString(list));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
