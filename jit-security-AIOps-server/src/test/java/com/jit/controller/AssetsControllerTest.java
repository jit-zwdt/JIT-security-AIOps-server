package com.jit.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.controller.AssetsController;
import com.jit.server.controller.LoginController;
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
 * @author Mamadou Lamine NIANG
 **/
//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
@RunWith(SpringRunner.class)
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
@SpringBootTest
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
//@WebAppConfiguration
public class AssetsControllerTest {

    @Autowired
    private LoginController loginController;
    @Autowired
    private AssetsController assetsController;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(loginController, assetsController).build();
    }

    @Test
    public void findByConditionTest() throws Exception {
        /*
         * 1、mockMvc.perform执行一个请求。
         * 2、MockMvcRequestBuilders.get("XXX")构造一个请求。
         * 3、ResultActions.param添加请求传值
         * 4、ResultActions.accept(MediaType.TEXT_HTML_VALUE))设置返回类型
         * 5、ResultActions.andExpect添加执行完成后的断言。
         * 6、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情
         *   比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
         * 7、ResultActions.andReturn表示执行完成后返回相应的结果。
         */
        RequestBuilder builder = MockMvcRequestBuilders.post("/login").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE).param("username", "frank").param("password", "frank");
        MvcResult result = mvc.perform(builder).andReturn();
        JSONObject jsonObject = JSONObject.parseObject(result.getResponse().getContentAsString());
        String access_token = jsonObject.getJSONObject("data").getString("access_token");
        System.out.println("access_token: " + access_token);

        JSONObject param = new JSONObject();
        param.put("name","1");
        param.put("registerStartDate","2020-06-16 00:00:00");
        param.put("registerEndDate","2020-06-17 00:00:00");
        JSONObject params = new JSONObject();
        params.put("page",1);
        params.put("size",10);
        params.put("param",param);

        RequestBuilder builder2 = MockMvcRequestBuilders.post("/assets/findByCondition").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).header("authorization", access_token).content(params.toJSONString());
        MvcResult result2 = mvc.perform(builder2).andReturn();
        System.out.println("result2: " + result2.getResponse().getContentAsString());

        /*JSONObject jsonObject2 = JSONObject.parseObject(result2.getResponse().getContentAsString());
        String auth = jsonObject2.getJSONObject("data").getString("auth");
        System.out.println("auth: " + auth);*/
    }

    public void getTokenTest() throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.post("/login").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE).param("username", "frank").param("password", "frank");
        MvcResult result = mvc.perform(builder).andReturn();
        JSONObject jsonObject = JSONObject.parseObject(result.getResponse().getContentAsString());
        String access_token = jsonObject.getJSONObject("data").getString("access_token");
        System.out.println("access_token: " + access_token);
    }
}
