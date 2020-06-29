package com.jit.server.service;

import com.jit.server.pojo.MonitorTypeEntity;

import java.util.List;

public interface MonitorTypeService {

    List<MonitorTypeEntity> getMonitorTypes() throws Exception;

    List<MonitorTypeEntity> getMonitorSubTypes() throws Exception;

    MonitorTypeEntity getMonitorTypesById(String id) throws Exception;

    Object getTypeById(String id) throws Exception;
}
