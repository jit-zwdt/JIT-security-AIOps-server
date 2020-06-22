package com.jit.server.service;

import com.jit.server.pojo.MonitorTemplatesEntity;
import com.jit.server.request.MonitorTemplatesParams;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MonitorTemplatesService {

    List<MonitorTemplatesEntity> getMonitorTemplates() throws Exception;

    Page<MonitorTemplatesEntity> getMonitorTemplates(PageRequest<MonitorTemplatesParams> params) throws Exception;
}
