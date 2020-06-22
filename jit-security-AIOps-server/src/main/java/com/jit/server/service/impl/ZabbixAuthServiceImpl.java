package com.jit.server.service.impl;

import com.jit.server.pojo.SysUserEntity;
import com.jit.server.repository.SysUserRepo;
import com.jit.server.service.ZabbixAuthService;
import com.jit.zabbix.client.service.ZabbixApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ZabbixAuthServiceImpl implements ZabbixAuthService {

    @Autowired
    private SysUserRepo sysUserRepo;

    @Autowired
    private ZabbixApiService zabbixApiService;

    @Override
    public String getAuth() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUserEntity user = sysUserRepo.findZabbixActiveUserByUsername(username);
        if (user != null) {
            String zabbixUsername = user.getZabbixUsername();
            String zabbixPassword = user.getZabbixPassword();
            if (StringUtils.isNotBlank(zabbixUsername) && StringUtils.isNotBlank(zabbixPassword)) {
                return zabbixApiService.authenticate(zabbixUsername, zabbixPassword);
            }
        }
        return null;
    }
}
