package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixGetTemplateDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateItemDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateTriggerDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.item.ItemMethod;
import com.jit.zabbix.client.model.trigger.TriggerMethod;
import com.jit.zabbix.client.request.ZabbixGetItemParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Service Wrapper of {@link ZabbixItemService} for template methods.
 *
 * @author zengxin_miao
 * @see ZabbixItemService
 **/
@Service
@Slf4j
public class ZabbixItemService {

    public static final String ITEM_IDS_NODE = "itemids";

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixItemService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }

    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/get">item.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of ZabbixGetItemDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixGetItemDTO> get(ZabbixGetItemParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(ItemMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixGetItemDTO.class);
    }

    /**
     * Update hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/item/update">item.update</a>).
     *
     * @param dto  The request param.
     * @param auth The auth token.
     * @return The list of affected hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public String update(ZabbixUpdateItemDTO dto, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(ItemMethod.UPDATE, dto, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        List<String> ids = jsonMapper.getList(response.getResult(), ITEM_IDS_NODE, String.class);
        if (CollectionUtils.isEmpty(ids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return ids.get(0);
    }
}
