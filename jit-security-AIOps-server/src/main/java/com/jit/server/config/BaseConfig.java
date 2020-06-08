package com.jit.server.config;

import com.jit.server.filter.ApiAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/08 10:12
 */
@Configuration
public class BaseConfig {
    @Bean
    public FilterRegistrationBean addFilterRegistrationBean() {
        //filter registerclass
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ApiAuthFilter());
        //Interfaces to be filtered
        registration.addUrlPatterns("/api/getAuth");
        registration.addUrlPatterns("/host/createHost");
        return registration;
    }
}
