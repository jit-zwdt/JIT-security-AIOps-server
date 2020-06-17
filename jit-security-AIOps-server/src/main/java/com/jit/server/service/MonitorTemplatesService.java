package com.jit.server.service;

import com.jit.server.pojo.MonitorTemplates;

import java.util.List;

public interface MonitorTemplatesService {

    List<MonitorTemplates> getMonitorTemplates() throws Exception;

    List<MonitorTemplates> getMonitorTemplates(MonitorTemplates monitorTemplates, int page, int size) throws Exception;
}
