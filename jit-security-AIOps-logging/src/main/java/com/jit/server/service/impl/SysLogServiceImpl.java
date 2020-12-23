package com.jit.server.service.impl;

import com.jit.server.pojo.SysLogEntity;
import com.jit.server.repository.SysLogRepo;
import com.jit.server.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogRepo sysLogRepo;

    @Override
    public SysLogEntity saveOrUpdateLog(SysLogEntity sysLogEntity) throws Exception {
        return sysLogRepo.saveAndFlush(sysLogEntity);
    }

    @Override
    public String getUserName(String name) throws Exception {
        return sysLogRepo.getUserName(name);
    }

    @Override
    public String getUserId(String name) throws Exception {
        return sysLogRepo.getUserId(name);
    }
}
