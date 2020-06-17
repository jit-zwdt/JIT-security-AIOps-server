package com.jit.server.service.impl;


import com.jit.server.pojo.MonitorTemplates;
import com.jit.server.repository.MonitorTemplatesRepo;
import com.jit.server.service.MonitorTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorTemplatesImpl implements MonitorTemplatesService {

    @Autowired
    MonitorTemplatesRepo monitorTemplatesRepo;

    @Override
    public List<MonitorTemplates> getMonitorTemplates() throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "type");
        return monitorTemplatesRepo.findAll(sort);
    }

    @Override
    public List<MonitorTemplates> getMonitorTemplates(MonitorTemplates monitorTemplates, int page, int size) throws Exception {
        //Page<MonitorTemplates> getMonitorTemplates(String lastname, Pageable pageable);
        return null;
    }
}
