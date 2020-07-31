package com.jit.server.service.impl;


import com.jit.server.pojo.MonitorHostDetailBindGraphs;
import com.jit.server.pojo.MonitorHostDetailBindItems;
import com.jit.server.repository.MonitorHostDetailBindGraphsRepo;
import com.jit.server.repository.MonitorHostDetailBindItemsRepo;
import com.jit.server.service.MonitorHostDetailBindGraphsService;
import com.jit.server.service.MonitorHostDetailBindItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: MonitorHostDetailBindGraphsImpl
 * @Author: lancelot
 * @Date: 2020.07.30
 */
@Service
public class MonitorHostDetailBindGraphsImpl implements MonitorHostDetailBindGraphsService {

    @Autowired
    private MonitorHostDetailBindGraphsRepo monitorHostDetailBindGraphsRepo;

    @Override
    public MonitorHostDetailBindGraphs saveOrUpdateMonitorHostDetailBindGraphs(MonitorHostDetailBindGraphs monitorHostDetailBindGraphs) throws Exception {
        return monitorHostDetailBindGraphsRepo.saveAndFlush(monitorHostDetailBindGraphs);
    }

    @Override
    public List<MonitorHostDetailBindGraphs> findMonitorHostDetailBindGraphsByHostId(String hostId, int isDeleted) throws Exception {
        return monitorHostDetailBindGraphsRepo.findByHostIdAndIsDeleted(hostId, isDeleted);
    }

    @Override
    public MonitorHostDetailBindGraphs findById(String id) throws Exception {
        return monitorHostDetailBindGraphsRepo.getOne(id);
    }

    @Override
    public MonitorHostDetailBindGraphs findByHostIdAndGraphIdAndIsDeleted(String hostId, String graphId, int isDeleted) throws Exception {
        return monitorHostDetailBindGraphsRepo.findByHostIdAndGraphIdAndIsDeleted(hostId, graphId, isDeleted);
    }
}
