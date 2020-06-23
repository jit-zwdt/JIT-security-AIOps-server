package com.jit.zabbix.client.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.jit.zabbix.client.dto.ZabbixCreateHostGroupDTO;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.dto.ZabbixHostGroupDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.host.HostGroupMethod;
import com.jit.zabbix.client.model.host.HostMethod;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.request.ZabbixGetHostGroupParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service Wrapper of {@link ZabbixApiService} for host group methods.
 *
 * @author zengxin_miao
 * @see ZabbixApiService
 **/
@Service
@Slf4j
public class ZabbixHostGroupService {

    public static final String GROUPIDS_IDS_NODE = "groupids";

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixHostGroupService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }

    /**
     * Hosts creation request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostgroup/create">hostgroup.create</a>).
     *
     * @param dtos HostGroups to create.
     * @param auth The auth token.
     * @return The list of created hostGroups ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<String> create(List<ZabbixCreateHostGroupDTO> dtos, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostGroupMethod.CREATE, dtos, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        JsonNode jsonNode = response.getResult().findValue(GROUPIDS_IDS_NODE);
        List<String> list = null;
        if (!jsonNode.isEmpty()) {
            list = new ArrayList<>();
            for (JsonNode j : jsonNode) {
                list.add(j.textValue());
            }
        }
        return list;
    }

    /**
     * Single host creation request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostgroup/create">hostgroup.create</a>).
     *
     * @param dto  HostGroup to create.
     * @param auth The auth token.
     * @return The created hostGroup id.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error or no host id was returned.
     */
    public String create(ZabbixCreateHostGroupDTO dto, String auth) throws ZabbixApiException {
        List<String> ids = create(Collections.singletonList(dto), auth);
        if (CollectionUtils.isEmpty(ids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return ids.get(0);
    }

    /**
     * Get hostGroups request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostgroup/get">hostgroup.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of hostGroupDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixHostGroupDTO> get(ZabbixGetHostGroupParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostGroupMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixHostGroupDTO.class);
    }

    /**
     * Delete hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostgroup/delete">hostgroup.delete</a>).
     *
     * @param hostGroupIds IDs of hostGroups to delete.
     * @param auth         The auth token.
     * @return The list of deleted hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<String> delete(List<String> hostGroupIds, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostGroupMethod.DELETE, hostGroupIds, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), GROUPIDS_IDS_NODE, String.class);
    }
}
