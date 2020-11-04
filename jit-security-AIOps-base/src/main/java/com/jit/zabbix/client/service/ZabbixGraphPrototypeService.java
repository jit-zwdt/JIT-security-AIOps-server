package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixUpdateItemDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.graph.GraphPrototypeMethod;
import com.jit.zabbix.client.model.item.ItemMethod;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;
import com.jit.zabbix.client.request.ZabbixGetGraphPrototypeParams;
import com.jit.zabbix.client.request.ZabbixGetItemParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lancelot
 * @see ZabbixGraphPrototypeService
 **/
@Service
public class ZabbixGraphPrototypeService {

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixGraphPrototypeService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }

    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/graphprototype/get#graphprototypeget">graphprototype.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of ZabbixGetGraphPrototypeDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixGetGraphPrototypeDTO> get(ZabbixGetGraphPrototypeParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(GraphPrototypeMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixGetGraphPrototypeDTO.class);
    }

    /**
     * create hosts request (<a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/graphprototype/create">item.update</a>).
     *
     * @param dto  The request param.
     * @param auth The auth token.
     * @return The list of affected graph ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<String> create(ZabbixCreateGraphPrototypeParams dto, String auth) throws ZabbixApiException {
        System.err.println("dto::::::::::::::;"+dto);
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(GraphPrototypeMethod.CREATE, dto, auth);
        System.err.println("request::::::::::"+request);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        List<String> graphids = jsonMapper.getList(response.getResult(),"graphids",String.class);
        if (CollectionUtils.isEmpty(graphids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return graphids;
    }

    public List<String> update(ZabbixCreateGraphPrototypeParams dto, String auth) throws ZabbixApiException {
        System.err.println("dto::::::::::::::;"+dto);
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(GraphPrototypeMethod.UPDATE, dto, auth);
        System.err.println("request::::::::::"+request);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        List<String> graphids = jsonMapper.getList(response.getResult(),"graphids",String.class);
        if (CollectionUtils.isEmpty(graphids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return graphids;
    }

    public List<String> delete(String graphid, String auth) throws ZabbixApiException {
        List<String> ids = new ArrayList<>();
        ids.add(graphid);
        System.err.println("dto::::::::::::::;"+graphid);
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(GraphPrototypeMethod.DELETE, ids, auth);
        System.err.println("request::::::::::"+request);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        System.err.println("response::::::::::"+response);
        List<String> graphids = jsonMapper.getList(response.getResult(),"graphids",String.class);
        if (CollectionUtils.isEmpty(graphids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return graphids;
    }
}
