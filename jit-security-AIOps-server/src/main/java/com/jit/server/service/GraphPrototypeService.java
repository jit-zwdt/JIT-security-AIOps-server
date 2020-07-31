package com.jit.server.service;

import com.jit.server.request.CreateGraphPrototypeParams;
import com.jit.server.request.GraphPrototypeParams;
import com.jit.server.request.ItemParams;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;

import java.io.IOException;
import java.util.List;

public interface GraphPrototypeService {
    public List<ZabbixGetGraphPrototypeDTO> getGProList(GraphPrototypeParams graphPrototypeParams) throws Exception;

    List<String> createGPro(ZabbixCreateGraphPrototypeParams zabbixCreateGraphPrototypeParams) throws Exception;
}
