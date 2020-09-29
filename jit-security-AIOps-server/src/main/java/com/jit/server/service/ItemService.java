package com.jit.server.service;

import com.jit.server.request.ItemParams;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ItemService {
    public List<ZabbixGetItemDTO> getItemInfoList(ItemParams itemParams,String auth) throws Exception;
    public String updateItemStatus(String itemId, String status, HttpServletRequest req) throws Exception;
}
