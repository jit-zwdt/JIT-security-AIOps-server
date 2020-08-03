package com.jit.server.service;

import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.request.ProblemClaimParams;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.request.ProblemParams;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;

import java.util.List;

public interface ProblemService {
    List<ZabbixProblemDTO> findByCondition(ProblemParams params) throws Exception;

    List<ProblemHostDTO> findProblemHost(ProblemParams params) throws Exception;

    List<ZabbixProblemDTO> findBySeverityLevel(ProblemClaimParams params) throws Exception;

    void addCalim(MonitorClaimEntity monitorClaimEntity) throws Exception;

}
