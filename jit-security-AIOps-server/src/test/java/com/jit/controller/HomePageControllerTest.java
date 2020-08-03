package com.jit.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.controller.HomePageController;
import com.jit.server.controller.LoginController;
import com.jit.server.service.ZabbixAuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/16 19:20
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class HomePageControllerTest {

    @Autowired
    private LoginController loginController;

    @Autowired
    private HomePageController homePageController;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    private MockMvc mvc;

    private String access_token;
    private String auth;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(loginController, homePageController).build();
    }

    @Before
    public void getAuthTest() throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.post("/login").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE).param("username", "frank").param("password", "frank");
        MvcResult result = mvc.perform(builder).andReturn();
        JSONObject jsonObject = JSONObject.parseObject(result.getResponse().getContentAsString());
        String access_token = jsonObject.getJSONObject("data").getString("access_token");
        this.access_token = access_token;
        this.auth = zabbixAuthService.getAuth();

        /*RequestBuilder builder2 = MockMvcRequestBuilders.post("/api/getAuth").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE).header("authorization", access_token).param("username", "test").param("password", "111111");
        MvcResult result2 = mvc.perform(builder2).andReturn();
        System.out.println(result2.getResponse().getContentAsString());
        JSONObject jsonObject2 = JSONObject.parseObject(result2.getResponse().getContentAsString());
        String auth = jsonObject2.getJSONObject("data").getString("auth");
        System.out.println("auth: " + auth);
        this.auth = auth;*/
    }

    @Test
    public void getMonitorTypeUsedInfoTest() throws Exception {

        RequestBuilder builder2 = MockMvcRequestBuilders.post("/homePage/getMonitorTypeUsedInfo").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE).header("authorization", this.access_token);
        MvcResult result2 = mvc.perform(builder2).andReturn();
        System.out.println(result2.getResponse().getContentAsString());
        JSONObject jsonObject2 = JSONObject.parseObject(result2.getResponse().getContentAsString());
        System.out.println(jsonObject2.toJSONString());
    }

    @Test
    public void getTimeTop5ItemInfoTest() throws Exception {

        RequestBuilder builder2 = MockMvcRequestBuilders.post("/homePage/getTimeTop5ItemInfo/"+"cup").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE).header("authorization", this.access_token);
        MvcResult result2 = mvc.perform(builder2).andReturn();
        System.out.println(result2.getResponse().getContentAsString());
        JSONObject jsonObject2 = JSONObject.parseObject(result2.getResponse().getContentAsString());
        System.out.println(jsonObject2.toJSONString());
    }


}
