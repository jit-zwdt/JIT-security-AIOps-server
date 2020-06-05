package com.jit.zabbix.client.autoconfigure;

import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.service.ZabbixApiService;
import com.jit.zabbix.client.service.ZabbixHostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke Tests for {@link ZabbixClientAutoConfiguration}.
 *
 * @author Mamadou Lamine NIANG
 **/
@SpringBootTest(classes = {ZabbixClientAutoConfiguration.class})
@EnableAutoConfiguration
public class ZabbixClientAutoConfigurationST {

    @Autowired
    private JsonMapper jsonMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixHostService zabbixHostService;


    @Test
    public void contextLoads() throws Exception {
        assertThat(jsonMapper).isNotNull();
        assertThat(restTemplate).isNotNull();
        assertThat(zabbixApiService).isNotNull();
        assertThat(zabbixHostService).isNotNull();
    }
}
