package com.jit.server.service.impl;

import com.jit.server.request.ProblemParams;
import com.jit.server.service.ProblemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.model.problem.ProblemSeverity;
import com.jit.zabbix.client.request.ZabbixGetProblemParams;
import com.jit.zabbix.client.service.ZabbixProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    private ZabbixProblemService zabbixProblemService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Override
    public List<ZabbixProblemDTO> findByCondition(ProblemParams params) throws Exception {
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)) {
            return null;
        }

        ProblemSeverity severity = ProblemSeverity.fromValue(params.getSeverity());
        ZabbixGetProblemParams _params = new ZabbixGetProblemParams();
        if(severity != null) {
            _params.setSeverity(severity);
        }

        return zabbixProblemService.get(_params, authToken);
    }
}
