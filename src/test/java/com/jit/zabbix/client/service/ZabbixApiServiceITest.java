package com.jit.zabbix.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jit.zabbix.client.autoconfigure.ZabbixClientAutoConfiguration;
import com.jit.zabbix.client.model.host.HostInventoryProperty;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.request.ZabbixGetHostParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Mamadou Lamine NIANG
 **/
@SpringBootTest(classes = {ZabbixClientAutoConfiguration.class})
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

    @Test
    void callShouldReturnAResult() throws Exception {
        String auth = zabbixApiService.authenticate("Admin", "zabbix");
        ZabbixGetHostParams params = new    ZabbixGetHostParams();
        params.setOutput("extend");
        List<ZabbixHostDTO> zabbixHostDTOList = zabbixHostService.get(params,auth);
        System.out.println(objectMapper.writeValueAsString(zabbixHostDTOList));
    }
    @Test
    public void testAuth()throws Exception{
        String token = zabbixApiService.authenticate("Admin","zabbix");
        List<String> keys = new ArrayList<>();
        keys.add("type");
        Map<String,List<String>> filter = new HashMap<>();
        filter.put("hostid", Arrays.asList("10318"));

        Map<String,String> map = new HashMap<>();
        map.put(HostInventoryProperty.TYPE.toString(),"服务器");

        ZabbixGetHostParams params = ZabbixGetHostParams.builder().output("extend").selectInventory(keys).build();
        List<ZabbixHostDTO> list = zabbixHostService.get(params,token);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        System.out.println(json);

    }

}
