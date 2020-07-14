package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.ZabbixHistoryDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.history.HistoryMethod;
import com.jit.zabbix.client.request.ZabbixGetHistoryParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Wrapper of {@link ZabbixApiService} for host interface methods.
 *
 * @author yongbin_jiang
 * @see ZabbixApiService
 **/
@Service
@Slf4j
public class ZabbixHistoryService {

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixHistoryService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }


    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/history/get">trigger.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of hostDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixHistoryDTO> get(ZabbixGetHistoryParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HistoryMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixHistoryDTO.class);
    }
}
