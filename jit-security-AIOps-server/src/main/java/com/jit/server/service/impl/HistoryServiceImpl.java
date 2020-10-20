package com.jit.server.service.impl;


import com.jit.server.request.HistoryParams;
import com.jit.server.service.HistoryService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixHistoryDTO;
import com.jit.zabbix.client.request.ZabbixGetHistoryParams;
import com.jit.zabbix.client.service.ZabbixHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private ZabbixHistoryService zabbixHistoryService;
    public static final String TREND_OUTPUT_EXTEND = "extend";
    public static final List<String> TREND_ARRAY_NAME = Collections.singletonList("clock");

    @Override
    public List<ZabbixHistoryDTO> getHistoryInfoList(HistoryParams historyParams, String authToken) throws Exception {
        if (historyParams == null) {
            return null;
        }
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }
        ZabbixGetHistoryParams params = new ZabbixGetHistoryParams();
        List<String> itemids = historyParams.getItemids();
        if (itemids != null && !CollectionUtils.isEmpty(itemids)) {
            params.setItemIds(itemids);
            params.setSortFields(TREND_ARRAY_NAME);
            params.setLimit(ConstUtil.LIMIT_MAX);
            params.setOutput(TREND_OUTPUT_EXTEND);
            params.setTimeFrom(historyParams.getTimefrom());
            params.setTimeTill(historyParams.getTimetill());
        }
        return zabbixHistoryService.get(params, authToken);
    }
}
