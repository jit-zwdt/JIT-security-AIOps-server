package com.jit.server.service;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.pojo.HostEntity;

import java.util.List;

public interface InspectionManageService {
    List<HostEntity> getHostInfo(String id) throws Exception;
    void createPDF(String jsonresult) throws Exception;
}
