package com.jit.server.service;

import com.jit.server.dto.ProblemRegisterDTO;
import com.jit.server.pojo.MonitorRegisterEntity;

import java.util.List;

public interface MonitorRegisterService {

    List<ProblemRegisterDTO> findByClaimId(String id) throws Exception;

    MonitorRegisterEntity addRegister(MonitorRegisterEntity monitorRegisterEntity);

    List<MonitorRegisterEntity> findAll();

    List<MonitorRegisterEntity> findByClaimIdAndIsResolve(String id);

    List<MonitorRegisterEntity> findByClaimIdAndProblemType(String id, String problemType);
}
