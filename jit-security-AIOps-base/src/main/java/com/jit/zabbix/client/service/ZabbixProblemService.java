package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.problem.ProblemMethod;
import com.jit.zabbix.client.request.ZabbixGetProblemParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ZabbixProblemService {

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixProblemService(JsonMapper jsonMapper, ZabbixApiService apiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = apiService;
    }

    public List<ZabbixProblemDTO> get(ZabbixGetProblemParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(ProblemMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixProblemDTO.class);
    }
}
