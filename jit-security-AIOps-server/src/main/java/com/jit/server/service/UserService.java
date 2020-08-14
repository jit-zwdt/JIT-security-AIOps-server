package com.jit.server.service;

import com.jit.server.request.ItemParams;
import com.jit.server.request.UserParams;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixUserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    public List<ZabbixUserDTO> getUserInfo(String alias) throws Exception;
    public List<UserParams> getUserAndMediaInfo(String alias, String userid) throws Exception;
    Object updateUserInfo(String userId, List<UserParams> params) throws Exception;
}
