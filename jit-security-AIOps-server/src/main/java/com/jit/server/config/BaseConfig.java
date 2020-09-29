package com.jit.server.config;

import com.jit.server.util.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/08 10:12
 */
@Configuration
public class BaseConfig {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Bean
    public void authMapBean() {
        ConcurrentHashMap<String, String> authMap = (ConcurrentHashMap<String, String>) webApplicationContext.getServletContext().getAttribute(ConstUtil.AUTH_MAP);
        if (authMap == null) {
            webApplicationContext.getServletContext().setAttribute(ConstUtil.AUTH_MAP, new ConcurrentHashMap<>(16));
        }
    }
}
