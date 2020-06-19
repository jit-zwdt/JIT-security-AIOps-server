package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.host.HostMethod;
import com.jit.zabbix.client.model.host.TemplateMethod;
import com.jit.zabbix.client.request.ZabbixGetTemplateParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Wrapper of {@link ZabbixTemplateService} for template methods.
 *
 * @author zengxin_miao
 * @see ZabbixTemplateService
 **/
@Service
@Slf4j
public class ZabbixTemplateService {

    public static final String TEMPLATE_IDS_NODE = "templateids";

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixTemplateService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }

    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/template/get">host.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of hostDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixHostDTO> get(ZabbixGetTemplateParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(TemplateMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixHostDTO.class);
    }
}
