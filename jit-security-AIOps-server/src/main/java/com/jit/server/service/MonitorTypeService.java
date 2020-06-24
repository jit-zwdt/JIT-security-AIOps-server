package com.jit.server.service;

import com.jit.server.pojo.MonitorTypeEntity;

import java.util.List;

public interface MonitorTypeService {

    List<MonitorTypeEntity> getMonitorTypes() throws Exception;

    MonitorTypeEntity getMonitorTypesById(String id) throws Exception;
}
