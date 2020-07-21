package com.jit.zabbix.client.service;

import com.jit.zabbix.client.dto.ZabbixGetTrendDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.trend.TrendMethod;
import com.jit.zabbix.client.request.ZabbixGetTrendParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Wrapper of {@link ZabbixTrendService} for template methods.
 *
 * @author jian_liu
 * @see ZabbixTrendService
 **/
@Service
@Slf4j
public class ZabbixTrendService {

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixTrendService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }

    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/trend/get">trend.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of ZabbixGetTrendDTO.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixGetTrendDTO> get(ZabbixGetTrendParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(TrendMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixGetTrendDTO.class);
    }

}
