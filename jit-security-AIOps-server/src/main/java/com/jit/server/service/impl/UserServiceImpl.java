package com.jit.server.service.impl;

import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixUserDTO;
import com.jit.zabbix.client.request.ZabbixGetUserParams;
import com.jit.zabbix.client.service.ZabbixUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final String EXTEND = "extend";

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ZabbixUserService zabbixUserService;

    @Override
    public List<ZabbixUserDTO> getUserInfo() throws Exception {
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        ZabbixGetUserParams params_user = new ZabbixGetUserParams();
        params_user.setOutput(EXTEND);

        return zabbixUserService.get(params_user, authToken);
    }
}
