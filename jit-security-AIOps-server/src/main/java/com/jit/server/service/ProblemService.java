package com.jit.server.service;

import com.jit.server.dto.ProblemClaimDTO;
import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.request.ProblemClaimParams;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.request.ProblemParams;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;

import java.util.List;

public interface ProblemService {
    List<ZabbixProblemDTO> findByCondition(ProblemParams params, String authToken) throws Exception;

    List<ProblemHostDTO> findProblemHost(ProblemParams params) throws Exception;

    List<ProblemClaimDTO> findBySeverityLevel(ProblemClaimParams params) throws Exception;

    void addCalim(MonitorClaimEntity monitorClaimEntity) throws Exception;

    List<MonitorClaimEntity> findClaimByUser();

    void updateClaimAfterRegister(MonitorClaimEntity monitorClaimEntity);

    MonitorClaimEntity findByProblemId(String problemId);

    List<ZabbixProblemDTO> getAlertdata(ProblemParams params) throws Exception;

    List<MonitorClaimEntity> findAllClaim();

    List<MonitorClaimEntity> findByIsResolve();

    List<MonitorClaimEntity> findByIsResolveAndProblemNameLike(String problemName);
}
