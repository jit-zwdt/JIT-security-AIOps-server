package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateTriggerDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.trigger.TriggerMethod;
import com.jit.zabbix.client.request.ZabbixGetTriggerParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Service Wrapper of {@link ZabbixApiService} for host interface methods.
 *
 * @author yongbin_jiang
 * @see ZabbixApiService
 **/
@Service
@Slf4j
public class ZabbixTriggerService {

    public static final String TRIGGERIDS_IDS_NODE = "triggerids";

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixTriggerService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }


    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/trigger/get">trigger.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of hostDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixTriggerDTO> get(ZabbixGetTriggerParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(TriggerMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixTriggerDTO.class);
    }

    /**
     * Update hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/trigger/update">trigger.update</a>).
     *
     * @param dto  The request param.
     * @param auth The auth token.
     * @return The list of affected hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public String update(ZabbixUpdateTriggerDTO dto, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(TriggerMethod.UPDATE, dto, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        List<String> ids = jsonMapper.getList(response.getResult(), TRIGGERIDS_IDS_NODE, String.class);
        if (CollectionUtils.isEmpty(ids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return ids.get(0);
    }

}
