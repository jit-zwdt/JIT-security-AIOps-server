package com.jit.server.service;

import com.jit.server.request.TrendParams;
import com.jit.zabbix.client.dto.ZabbixGetTrendDTO;

import java.util.List;

public interface TrendService {
    List<ZabbixGetTrendDTO> getTrendInfoList(TrendParams trendParams, String auth) throws Exception;
}
