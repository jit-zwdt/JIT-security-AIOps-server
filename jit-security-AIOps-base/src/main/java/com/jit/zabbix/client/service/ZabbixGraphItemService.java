package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.ZabbixGetGraphItemDTO;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.graph.GraphItemMethod;
import com.jit.zabbix.client.model.graph.GraphPrototypeMethod;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;
import com.jit.zabbix.client.request.ZabbixGetGraphItemParams;
import com.jit.zabbix.client.request.ZabbixGetGraphPrototypeParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lancelot
 * @see ZabbixGraphItemService
 **/
@Service
public class ZabbixGraphItemService {

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixGraphItemService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }

    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/graphitem/get">graphitem.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of ZabbixGetGraphItemDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixGetGraphItemDTO> get(ZabbixGetGraphItemParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(GraphItemMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixGetGraphItemDTO.class);
    }
}
