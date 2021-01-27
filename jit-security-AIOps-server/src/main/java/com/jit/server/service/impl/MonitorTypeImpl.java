package com.jit.server.service.impl;


import com.jit.server.dto.MonitorTypesDTO;
import com.jit.server.pojo.MonitorTypeEntity;
import com.jit.server.repository.MonitorTypeRepo;
import com.jit.server.service.MonitorTypeService;
import com.jit.server.util.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorTypeImpl implements MonitorTypeService {

    @Autowired
    MonitorTypeRepo monitorTypeRepo;

    @Override
    public List<MonitorTypesDTO> getMonitorTypes() throws Exception {
        return monitorTypeRepo.findMonitorTypesByPid(ConstUtil.PID_0);
    }

    @Override
    public List<MonitorTypesDTO> getMonitorSubTypes() throws Exception {
        return monitorTypeRepo.findMonitorSubTypesByPid(ConstUtil.PID_0);
    }

    @Override
    public MonitorTypeEntity getMonitorTypesById(String id) throws Exception {
        return monitorTypeRepo.getOne(id);
    }

    @Override
    public Object getTypeById(String id) throws Exception {
        return monitorTypeRepo.getTypeById(id);
    }
}
