package com.jit.server.service;

import com.jit.server.pojo.MonitorRegisterEntity;

import java.util.List;

public interface MonitorRegisterService {

    List<MonitorRegisterEntity> findByClaimId(String id) throws Exception;

    MonitorRegisterEntity addRegister(MonitorRegisterEntity monitorRegisterEntity);
}
