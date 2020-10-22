package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.ZabbixUpdateMediaDTO;
import com.jit.zabbix.client.dto.ZabbixUserDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.user.UserMethod;
import com.jit.zabbix.client.request.ZabbixGetUserParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class ZabbixUserService {

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public static final String USER_IDS_NODE = "userids";

    public ZabbixUserService(JsonMapper jsonMapper, ZabbixApiService apiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = apiService;
    }

    public List<ZabbixUserDTO> get(ZabbixGetUserParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(UserMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixUserDTO.class);
    }

    public String update(ZabbixUpdateMediaDTO zabbixUpdateMediaDTO, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(UserMethod.UPDATE, zabbixUpdateMediaDTO, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        List<String> ids = jsonMapper.getList(response.getResult(), USER_IDS_NODE, String.class);
        if (CollectionUtils.isEmpty(ids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return ids.get(0);
    }

    public boolean logout(String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(UserMethod.logout, new Object[]{}, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return response.getResult().asBoolean();
    }
}
