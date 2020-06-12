package com.jit.zabbix.client.service;

import com.jit.Application;
import com.jit.zabbix.client.utils.JsonMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @Description: Test Host Api
 * @Author: zengxin_miao
 * @Date: 2020/06/08 09:49
 */
@SpringBootTest(classes = {Application.class})
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class HostApiTest {

    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixHostService zabbixHostService;

    @Autowired
    private JsonMapper jsonMapper;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testHostCreat() throws Exception {
        String mockJsonResponse = "{\"jsonrpc\":\"2.0\",\"result\":\"0424bd59b807674191e7d77572075f33\",\"id\":1}";
        mockServer.expect(ExpectedCount.once(), requestTo("http://172.16.15.10/zabbix/api_jsonrpc.php"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON));
        String authToken = zabbixApiService.authenticate("Admin", "zabbix");
        mockServer.verify();
        assertThat(authToken).isEqualTo("0424bd59b807674191e7d77572075f33");
    }


}
