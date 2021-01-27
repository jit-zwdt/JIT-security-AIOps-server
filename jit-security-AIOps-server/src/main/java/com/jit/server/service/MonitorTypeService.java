package com.jit.server.service;

import com.jit.server.dto.MonitorTypesDTO;
import com.jit.server.pojo.MonitorTypeEntity;

import java.util.List;

public interface MonitorTypeService {

    List<MonitorTypesDTO> getMonitorTypes() throws Exception;

    List<MonitorTypesDTO> getMonitorSubTypes() throws Exception;

    MonitorTypeEntity getMonitorTypesById(String id) throws Exception;

    Object getTypeById(String id) throws Exception;
}
