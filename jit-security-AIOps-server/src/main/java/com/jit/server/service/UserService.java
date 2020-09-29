package com.jit.server.service;

import com.jit.server.request.UserParams;
import com.jit.zabbix.client.dto.ZabbixUserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    List<ZabbixUserDTO> getUserInfo(String alias ,String auth) throws Exception;

    List<UserParams> getUserAndMediaInfo(String alias, String userid, String auth ) throws Exception;

    String updateUserInfo(String userId, List<UserParams> params, String auth ) throws Exception;

    String findIdByUsername() throws Exception;
}
