package com.jit.zabbix.client.service;


import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.host.HostInterfaceMethod;
import com.jit.zabbix.client.model.host.HostMethod;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.request.ZabbixGetHostInterfaceParams;
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
public class ZabbixHostInterfaceService {

    public static final String INTERFACEIDS_IDS_NODE = "interfaceids";

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixHostInterfaceService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }


    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostinterface/get">hostinterface.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of hostDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixHostInterface> get(ZabbixGetHostInterfaceParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostInterfaceMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixHostInterface.class);
    }

}
