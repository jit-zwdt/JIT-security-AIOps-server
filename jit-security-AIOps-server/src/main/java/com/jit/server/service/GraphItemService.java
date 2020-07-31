package com.jit.server.service;

import com.jit.server.request.GraphItemParams;
import com.jit.server.request.GraphPrototypeParams;
import com.jit.zabbix.client.dto.ZabbixGetGraphItemDTO;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;

import java.util.List;

public interface GraphItemService {
    public List<ZabbixGetGraphItemDTO> getGItemList(GraphItemParams graphItemParams) throws Exception;
}
