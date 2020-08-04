package com.jit.server.service.impl;

import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixUserDTO;
import com.jit.zabbix.client.request.ZabbixGetUserParams;
import com.jit.zabbix.client.service.ZabbixUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    public static final String EXTEND = "extend";

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ZabbixUserService zabbixUserService;

    @Override
    public List<ZabbixUserDTO> getUserInfo(String alias) throws Exception {
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        ZabbixGetUserParams params_user = new ZabbixGetUserParams();
        params_user.setOutput(EXTEND);
        params_user.setSelectMedias(EXTEND);
        params_user.setSelectMediatypes(EXTEND);
        if (!StringUtils.isEmpty(alias) && !alias.equals("NULL")) {
            Map<String, Object> search = new HashMap<>();
            search.put("alias", alias);
            params_user.setSearch(search);
        }

        return zabbixUserService.get(params_user, authToken);
    }
}
