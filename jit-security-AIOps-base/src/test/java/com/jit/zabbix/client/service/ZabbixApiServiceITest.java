package com.jit.zabbix.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jit.Application;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.model.host.HostInventoryProperty;
import com.jit.zabbix.client.request.JsonRPCRequest;
import com.jit.zabbix.client.request.ZabbixGetHostParams;
import com.jit.zabbix.client.response.JsonRPCResponse;
import com.jit.zabbix.client.utils.JsonMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Mamadou Lamine NIANG
 **/
@SpringBootTest(classes = {Application.class})
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class ZabbixApiServiceITest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JsonMapper jsonMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixHostService zabbixHostService;

    /**
     * 获取 Zabbix API 版本。
     *
     * @throws Exception
     */
    @Test
    public void test_apiinfo_version() throws Exception {
        JsonRPCRequest jsonRPCRequest = new JsonRPCRequest();
        jsonRPCRequest.setMethod("apiinfo.version");
        jsonRPCRequest.setParams(new Object[]{});
        JsonRPCResponse jsonRPCResponse = zabbixApiService.call(jsonRPCRequest);
        System.out.println(jsonRPCResponse.getResult());
    }

    @Test
    public void callShouldReturnAResult() throws Exception {
        String auth = zabbixApiService.authenticate("Admin", "zabbix");
        ZabbixGetHostParams params = new ZabbixGetHostParams();
        params.setOutput("extend");
        List<ZabbixHostDTO> zabbixHostDTOList = zabbixHostService.get(params, auth);
        System.out.println(objectMapper.writeValueAsString(zabbixHostDTOList));
    }

    @Test
    public void testAuth() throws Exception {
        String token = zabbixApiService.authenticate("Admin", "zabbix");
        List<String> keys = new ArrayList<>();
        keys.add("type");
        Map<String, List<String>> filter = new HashMap<>();
        filter.put("hostid", Arrays.asList("10318"));

        Map<String, String> map = new HashMap<>();
        map.put(HostInventoryProperty.TYPE.toString(), "服务器");

        ZabbixGetHostParams params = ZabbixGetHostParams.builder().output("extend").selectInventory(keys).build();
        List<ZabbixHostDTO> list = zabbixHostService.get(params, token);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        System.out.println(json);

    }

}
