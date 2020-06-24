package com.jit.server.service.impl;


import com.jit.server.pojo.MonitorTypeEntity;
import com.jit.server.repository.MonitorTypeRepo;
import com.jit.server.service.MonitorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorTypeImpl implements MonitorTypeService {

    @Autowired
    MonitorTypeRepo monitorTypeRepo;

    @Override
    public List<MonitorTypeEntity> getMonitorTypes() throws Exception {
        return monitorTypeRepo.findByPidAndIsDeletedOrderByOrderNum("0", 0);
    }
}
