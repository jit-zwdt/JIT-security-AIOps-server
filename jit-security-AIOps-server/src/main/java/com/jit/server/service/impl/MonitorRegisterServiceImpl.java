package com.jit.server.service.impl;


import com.jit.server.pojo.MonitorRegisterEntity;
import com.jit.server.repository.MonitorRegisterRepo;
import com.jit.server.service.MonitorRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MonitorRegisterServiceImpl implements MonitorRegisterService {

    @Autowired
    private MonitorRegisterRepo monitorRegisterRepo;

    @Override
    public List<MonitorRegisterEntity> findByClaimId(String id){
        List<MonitorRegisterEntity> monitorRegisterEntity = monitorRegisterRepo.findByClaimId(id);
        return monitorRegisterEntity;
    }

    @Override
    public MonitorRegisterEntity addRegister(MonitorRegisterEntity monitorRegisterEntity) {
        MonitorRegisterEntity registerEntity = monitorRegisterRepo.save(monitorRegisterEntity);
        return registerEntity;
    }
}
