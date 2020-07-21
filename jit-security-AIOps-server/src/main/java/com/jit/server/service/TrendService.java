package com.jit.server.service;

import com.jit.server.request.TrendParams;
import com.jit.zabbix.client.dto.ZabbixGetTrendDTO;

import java.util.List;

public interface TrendService {
    public List<ZabbixGetTrendDTO> getTrendInfoList(TrendParams trendParams) throws Exception;
}
