package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.*;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.host.HostMethod;
import com.jit.zabbix.client.request.ZabbixGetHostParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Service Wrapper of {@link ZabbixApiService} for host methods.
 *
 * @author Mamadou Lamine NIANG
 * @see ZabbixApiService
 **/
@Service
@Slf4j
public class ZabbixHostService {

    public static final String HOSTS_IDS_NODE = "hostids";

    private final JsonMapper jsonMapper;
    private final com.jit.zabbix.client.service.ZabbixApiService apiService;

    public ZabbixHostService(JsonMapper jsonMapper, com.jit.zabbix.client.service.ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }

    /**
     * Hosts creation request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/create">host.create</a>).
     *
     * @param dtos Hosts to create.
     * @param auth The auth token.
     * @return The list of created hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<String> create(List<ZabbixHostDTO> dtos, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostMethod.CREATE, dtos, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        //return response.getResult().findValuesAsText(HOSTS_IDS_NODE);
        /*JsonNode skillsNode = response.getResult().get(HOSTS_IDS_NODE);
        List<String> result = new ArrayList<String>();
        if (!skillsNode.isEmpty()) {
            for(int i = 0;i < skillsNode.size();i++) {
                result.add(skillsNode.get(i).asText());
            }
        }
        return result;*/
        return jsonMapper.getList(response.getResult(), HOSTS_IDS_NODE, String.class);
    }

    /**
     * Single host creation request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/create">host.create</a>).
     *
     * @param dto  Host to create.
     * @param auth The auth token.
     * @return The created host id.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error or no host id was returned.
     */
    public String create(ZabbixHostDTO dto, String auth) throws ZabbixApiException {
        List<String> ids = create(Collections.singletonList(dto), auth);
        if (CollectionUtils.isEmpty(ids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return ids.get(0);
    }

    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/get">host.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of hostDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixHostDTO> get(ZabbixGetHostParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixHostDTO.class);
    }

    /**
     * Delete hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/delete">host.delete</a>).
     *
     * @param hostIds IDs of hosts to delete.
     * @param auth    The auth token.
     * @return The list of deleted hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<String> delete(List<String> hostIds, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostMethod.DELETE, hostIds, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), HOSTS_IDS_NODE, String.class);
    }

    /**
     * Delete hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/delete">host.delete</a>).
     *
     * @param hostIds IDs of hosts to delete.
     * @param auth    The auth token.
     * @return The list of deleted hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public String delete(String hostIds, String auth) throws ZabbixApiException {
        List<String> ids = delete(Collections.singletonList(hostIds), auth);
        if (CollectionUtils.isEmpty(ids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return ids.get(0);
    }

    /**
     * Mass add hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/massadd">host.massadd</a>).
     *
     * @param dto  The request param.
     * @param auth The auth token.
     * @return The list of affected hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<String> massAdd(ZabbixMassAddHostDTO dto, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostMethod.MASS_ADD, dto, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), HOSTS_IDS_NODE, String.class);
    }

    /**
     * Mass remove hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/massremove">host.massremove</a>).
     *
     * @param dto  The request param.
     * @param auth The auth token.
     * @return The list of affected hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<String> massRemove(ZabbixMassRemoveHostDTO dto, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostMethod.MASS_REMOVE, dto, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), HOSTS_IDS_NODE, String.class);
    }

    /**
     * Mass update hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/massupdate">host.massupdate</a>).
     *
     * @param dto  The request param.
     * @param auth The auth token.
     * @return The list of affected hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<String> massUpdate(ZabbixMassUpdateHostDTO dto, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostMethod.MASS_UPDATE, dto, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), HOSTS_IDS_NODE, String.class);
    }

    /**
     * Update hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/update">host.update</a>).
     *
     * @param dto  The request param.
     * @param auth The auth token.
     * @return The list of affected hosts ids.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public String update(ZabbixUpdateHostDTO dto, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(HostMethod.UPDATE, dto, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        List<String> ids = jsonMapper.getList(response.getResult(), HOSTS_IDS_NODE, String.class);
        if (CollectionUtils.isEmpty(ids)) {
            throw new ZabbixApiException("Aucun id recu.");
        }
        return ids.get(0);
    }
}
