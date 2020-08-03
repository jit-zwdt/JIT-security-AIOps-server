package com.jit.server.service;

import com.jit.server.request.ItemParams;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixUserDTO;

import java.util.List;

public interface UserService {
    public List<ZabbixUserDTO> getUserInfo() throws Exception;
}
