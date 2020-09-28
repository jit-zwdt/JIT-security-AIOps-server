package com.jit.server.service.impl;


import com.jit.server.request.TrendParams;
import com.jit.server.service.TrendService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetTrendDTO;
import com.jit.zabbix.client.request.ZabbixGetTrendParams;
import com.jit.zabbix.client.service.ZabbixTrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TrendServiceImpl implements TrendService {

    @Autowired
    private ZabbixTrendService zabbixTrendService;
    @Autowired
    private ZabbixAuthService zabbixAuthService;
    public static final String TREND_OUTPUT_ITEMID = "itemid";
    public static final String TREND_OUTPUT_CLOCK = "clock";
    public static final String TREND_OUTPUT_NUM = "num";
    public static final String TREND_OUTPUT_VALUE_MIN = "value_min";
    public static final String TREND_OUTPUT_VALUE_AVG = "value_avg";
    public static final String TREND_OUTPUT_VALUE_MAX = "value_max";
    public static final int TREND_LIMIT = 100;
    public static final List<String> TREND_ARRAY_NAME = Collections.singletonList("clock");

    @Override
    public List<ZabbixGetTrendDTO> getTrendInfoList(TrendParams trendParams, String authToken) throws Exception {
        if (trendParams == null) {
            return null;
        }
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }
        ZabbixGetTrendParams params = new ZabbixGetTrendParams();
        List<String> itemids = trendParams.getItemids();
        List<String> output = new ArrayList<>();
        output.add(TREND_OUTPUT_ITEMID);
        output.add(TREND_OUTPUT_CLOCK);
        output.add(TREND_OUTPUT_NUM);
        output.add(TREND_OUTPUT_VALUE_MIN);
        output.add(TREND_OUTPUT_VALUE_AVG);
        output.add(TREND_OUTPUT_VALUE_MAX);
        if (itemids != null && !CollectionUtils.isEmpty(itemids)) {
            params.setItemids(itemids);
            params.setOutput(output);
            params.setSortFields(TREND_ARRAY_NAME);
            params.setLimit(TREND_LIMIT);
            params.setTime_from(trendParams.getTimefrom());
            params.setTime_till(trendParams.getTimetill());
        }
        return zabbixTrendService.get(params, authToken);
    }
}
