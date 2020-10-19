package com.jit.server.service;

import com.jit.server.request.HistoryParams;
import com.jit.server.request.TrendParams;
import com.jit.zabbix.client.dto.ZabbixGetTrendDTO;
import com.jit.zabbix.client.dto.ZabbixHistoryDTO;

import java.util.List;

public interface HistoryService {
    List<ZabbixHistoryDTO> getHistoryInfoList(HistoryParams historyParams, String auth) throws Exception;
}
