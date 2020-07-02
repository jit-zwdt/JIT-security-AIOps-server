package com.jit.server.service;

import com.jit.server.request.ItemParams;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;

import java.util.List;

public interface ItemService {
    public List<ZabbixGetItemDTO> getItemInfoList(ItemParams itemParams) throws Exception;
}
