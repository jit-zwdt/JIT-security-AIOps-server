package com.jit.server.service;

import com.jit.server.dto.ProblemClaimDTO;
import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.request.ProblemClaimParams;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.request.ProblemParams;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProblemService {
    List<ZabbixProblemDTO> findByCondition(ProblemParams params, String authToken) throws Exception;

    List<ProblemHostDTO> findProblemHost(ProblemParams params ,String auth) throws Exception;

    List<ProblemClaimDTO> findBySeverityLevel(ProblemClaimParams params, String auth ) throws Exception;

    void addCalim(MonitorClaimEntity monitorClaimEntity) throws Exception;

    List<MonitorClaimEntity> findClaimByUser(String problemName, int resolveType);

    void updateClaimAfterRegister(MonitorClaimEntity monitorClaimEntity);

    MonitorClaimEntity findByProblemId(String problemId);

    List<ZabbixProblemDTO> getAlertdata(ProblemParams params,String auth ) throws Exception;

    List<MonitorClaimEntity> findAllClaim();

    List<MonitorClaimEntity> findByIsResolve();

    List<MonitorClaimEntity> findByIsResolve(String resolveTimeStart, String resolveTimeEnd) throws Exception;

    List<MonitorClaimEntity> findByIsResolveAndProblemName(String problemName);

    List<MonitorClaimEntity> findByIsResolveAndProblemName(String problemName, String resolveTimeStart, String resolveTimeEnd);

    List<ZabbixProblemDTO> findProblemById(String[] params,String auth) throws Exception;
}
