package com.jit.server.service.impl;


import com.jit.server.dto.ProblemRegisterDTO;
import com.jit.server.pojo.MonitorRegisterEntity;
import com.jit.server.repository.MonitorRegisterRepo;
import com.jit.server.service.MonitorRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
public class MonitorRegisterServiceImpl implements MonitorRegisterService {

    @Autowired
    private MonitorRegisterRepo monitorRegisterRepo;

    @Override
    public List<ProblemRegisterDTO> findByClaimId(String id){
        List<Object> objects = monitorRegisterRepo.findByClaimId(id);
        List<ProblemRegisterDTO> monitorRegisterEntities= new ArrayList<>();
        for(Object o:objects){
            ProblemRegisterDTO monitorRegisterEntity = new ProblemRegisterDTO();
            Object[] a = (Object[]) o;
            monitorRegisterEntity.setId(a[0].toString());
            monitorRegisterEntity.setGmtCreate((LocalDateTime)a[1]);
            monitorRegisterEntity.setIsResolve(Integer.valueOf(a[2].toString()));
            monitorRegisterEntity.setProblemProcess(a[3].toString());
            monitorRegisterEntity.setProblemReason(a[4].toString());
            monitorRegisterEntity.setProblemSolution(a[5].toString());
            monitorRegisterEntity.setProblemType(a[6].toString());
            monitorRegisterEntity.setProblemHandleTime(a[7].toString());
            monitorRegisterEntities.add(monitorRegisterEntity);
        }
        return monitorRegisterEntities;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MonitorRegisterEntity addRegister(MonitorRegisterEntity monitorRegisterEntity) {
        MonitorRegisterEntity registerEntity = monitorRegisterRepo.save(monitorRegisterEntity);
        return registerEntity;
    }

    @Override
    public List<MonitorRegisterEntity> findAll() {
        List<MonitorRegisterEntity> list = monitorRegisterRepo.findAll();
        return list;
    }

    @Override
    public List<MonitorRegisterEntity> findByClaimIdAndProblemType(String id, String problemType) {
        return monitorRegisterRepo.findByClaimIdAndProblemTypeAndIsResolve(id,problemType,1);
    }

    @Override
    public List<MonitorRegisterEntity> findByClaimIdAndIsResolve(String id) {
        return monitorRegisterRepo.findByClaimIdAndIsResolve(id,1);
    }
}
