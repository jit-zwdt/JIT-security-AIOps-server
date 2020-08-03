package com.jit.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/07/17 17:17
 */
@Configuration
@PropertySource("classpath:params.properties")
@ConfigurationProperties(prefix = "param")
public class ParamsConfig {

    private Map<String, String> monitor1 = new HashMap<>();

    private Map<String, String> monitor2 = new HashMap<>();

    private Map<String, String> monitor3 = new HashMap<>();

    private Map<String, String> monitor4 = new HashMap<>();

    private Map<String, String> item = new HashMap<>();

    public Map<String, String> getMonitor1() {
        return monitor1;
    }

    public void setMonitor1(Map<String, String> monitor1) {
        this.monitor1 = monitor1;
    }

    public Map<String, String> getMonitor2() {
        return monitor2;
    }

    public void setMonitor2(Map<String, String> monitor2) {
        this.monitor2 = monitor2;
    }

    public Map<String, String> getMonitor3() {
        return monitor3;
    }

    public void setMonitor3(Map<String, String> monitor3) {
        this.monitor3 = monitor3;
    }

    public Map<String, String> getMonitor4() {
        return monitor4;
    }

    public void setMonitor4(Map<String, String> monitor4) {
        this.monitor4 = monitor4;
    }

    public Map<String, String> getItem() {
        return item;
    }

    public void setItem(Map<String, String> item) {
        this.item = item;
    }
}
