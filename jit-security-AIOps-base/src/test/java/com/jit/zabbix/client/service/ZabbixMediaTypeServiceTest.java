package com.jit.zabbix.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jit.Application;
import com.jit.zabbix.client.dto.ZabbixCreateMediaTypeDTO;
import com.jit.zabbix.client.dto.ZabbixGetMediaTypeDTO;
import com.jit.zabbix.client.model.mediaType.MediaTypeSmtpSecurity;
import com.jit.zabbix.client.model.mediaType.MediaTypeType;
import com.jit.zabbix.client.request.ZabbixGetMediaTypeParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mamadou Lamine NIANG
 **/
@SpringBootTest(classes = {Application.class})
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class ZabbixMediaTypeServiceTest {

    @Autowired
    private ZabbixApiService zabbixApiService;

    @Autowired
    private ZabbixMediaTypeService zabbixMediaTypeService;


    @Test
    public void mediaTypeGetTest() throws Exception {
        String token = zabbixApiService.authenticate("Admin", "zabbix");

        ZabbixGetMediaTypeParams params = new ZabbixGetMediaTypeParams();
        params.setOutput("extend");
        List<ZabbixGetMediaTypeDTO> list = zabbixMediaTypeService.get(params, token);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        System.out.println(json);
    }

    @Test
    public void mediaTypeGetTest2() throws Exception {
        String token = zabbixApiService.authenticate("Admin", "zabbix");

        ZabbixGetMediaTypeParams params = new ZabbixGetMediaTypeParams();
        //params.setOutput("extend");
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("name", "O");
        params.setSearch(searchMap);
        params.setStartSearch(false);
        // params.setSearchWildcardsEnabled(true);
        // params.setSearchByAny(true);
        List<ZabbixGetMediaTypeDTO> list = zabbixMediaTypeService.get(params, token);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        System.out.println(json);
    }

    @Test
    public void mediaTypeCreateTest() throws Exception {
        String token = zabbixApiService.authenticate("Admin", "zabbix");
        ZabbixCreateMediaTypeDTO zabbixCreateMediaTypeDTO = new ZabbixCreateMediaTypeDTO();
        //create email
        zabbixCreateMediaTypeDTO.setName("测试创建email4");
        zabbixCreateMediaTypeDTO.setType(MediaTypeType.EMAIL);
        zabbixCreateMediaTypeDTO.setSmtpServer("mail.example.com");
        zabbixCreateMediaTypeDTO.setSmtpPort(25);
        zabbixCreateMediaTypeDTO.setSmtpHelo("example.com");
        zabbixCreateMediaTypeDTO.setSmtpEmail("zabbix@example.com");
        zabbixCreateMediaTypeDTO.setSmtpSecurity(MediaTypeSmtpSecurity.STARTTLS);
        zabbixCreateMediaTypeDTO.setSmtpAuthentication(false);
        zabbixCreateMediaTypeDTO.setContentType(true);
        zabbixCreateMediaTypeDTO.setDescription("描述");
        zabbixCreateMediaTypeDTO.setStatus(false);
        zabbixCreateMediaTypeDTO.setMaxattempts(10);
        zabbixCreateMediaTypeDTO.setMaxsessions(1);
        zabbixCreateMediaTypeDTO.setTimeout("10s");
        String mediaTypeid = zabbixMediaTypeService.create(zabbixCreateMediaTypeDTO, token);
        System.out.println(mediaTypeid);
    }

}
