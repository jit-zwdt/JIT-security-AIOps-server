package com.jit.server.service;

import com.jit.server.request.UserParams;
import com.jit.zabbix.client.dto.ZabbixUserDTO;

import java.util.List;

public interface UserService {
    List<ZabbixUserDTO> getUserInfo(String alias) throws Exception;

    List<UserParams> getUserAndMediaInfo(String alias, String userid) throws Exception;

    Object updateUserInfo(String userId, List<UserParams> params) throws Exception;

    String findIdByUsername() throws Exception;
}
