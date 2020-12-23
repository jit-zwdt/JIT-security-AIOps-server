package com.jit.server.service;

import com.jit.server.pojo.SysLogEntity;

public interface SysLogService {
    SysLogEntity saveOrUpdateLog(SysLogEntity sysLogEntity) throws Exception;

    String getUserName(String name) throws Exception;

    String getUserId(String name) throws Exception;
}
