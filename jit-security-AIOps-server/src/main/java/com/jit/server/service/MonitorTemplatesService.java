package com.jit.server.service;

import com.jit.server.dto.MonitorTemplatesDTO;
import com.jit.server.pojo.MonitorTemplatesEntity;
import com.jit.server.request.MonitorTemplatesParams;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MonitorTemplatesService {

    List<MonitorTemplatesEntity> getMonitorTemplates() throws Exception;

    Page<MonitorTemplatesDTO> getMonitorTemplates(PageRequest<MonitorTemplatesParams> params) throws Exception;

    MonitorTemplatesEntity getMonitorTemplate(String id) throws Exception;

    void updateMonitorTemplate(MonitorTemplatesEntity monitorTemplatesEntity) throws Exception;

    List<MonitorTemplatesEntity> getMonitorTemplatesByTypeId(String typeId) throws Exception;

    List<MonitorTemplatesEntity> getMonitorTemplatesByTypeIdAndNameLike(String typeId, String keyword) throws Exception;
}
