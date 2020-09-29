package com.jit.server.service.impl;

import com.jit.server.repository.SysUserRepo;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.zabbix.client.service.ZabbixApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ZabbixAuthServiceImpl implements ZabbixAuthService {

    //@Autowired private SysUserRepo sysUserRepo;

    @Autowired
    private ZabbixApiService zabbixApiService;

    @Autowired
    private Environment env;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Override
    public String getAuth() throws Exception {

        /* 用户和密码从配置文件中获取，不采用页面设定值，避免页面暴露相关信息
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUserEntity user = sysUserRepo.findZabbixActiveUserByUsername(username);
        if (user != null) {
            String zabbixUsername = user.getZabbixUsername();
            String zabbixPassword = user.getZabbixPassword();
            if (StringUtils.isNotBlank(zabbixUsername) && StringUtils.isNotBlank(zabbixPassword)) {
                return zabbixApiService.authenticate(zabbixUsername, zabbixPassword);
            }
        }*/

        //用户和密码从配置文件中获取
        String zabbixUsername = env.getProperty("zabbix.username");
        String zabbixPassword = env.getProperty("zabbix.password");
        if (StringUtils.isNotBlank(zabbixUsername) && StringUtils.isNotBlank(zabbixPassword)) {
            return zabbixApiService.authenticate(zabbixUsername, zabbixPassword);
        }

        return null;
    }

    /**
     * 获取保存在Context中的auth信息
     *
     * @param key
     * @return
     * @throws Exception
     */
    @Override
    public String getAuth(String key) throws Exception {
        ConcurrentHashMap<String, String> authMap = (ConcurrentHashMap<String, String>) webApplicationContext.getServletContext().getAttribute(ConstUtil.AUTH_MAP);
        return authMap.get(key);
    }

    /**
     * 由于调用zabbix接口需要使用到auth，多次重复登录zabbix回导致产生大量的开放会话记录。所以将zabbix登录信息保存到Context中。
     *
     * @param key
     */
    @Override
    public void setAuthToApplicationContext(String key) throws Exception {
        ConcurrentHashMap<String, String> authMap = (ConcurrentHashMap<String, String>) webApplicationContext.getServletContext().getAttribute(ConstUtil.AUTH_MAP);
        if (authMap.get(key) == null) {
            authMap.put(key, zabbixAuthService.getAuth());
        }
    }
}
