package com.jit.server.service;

import com.jit.server.pojo.HostEntity;

import java.util.List;

public interface InspectionManageService {
    List<HostEntity> getHostInfo(String id) throws Exception;
    String createPDF() throws Exception;
}
