package com.jit.server.service.impl;


import com.jit.server.pojo.MonitorHostDetailBindItems;
import com.jit.server.repository.MonitorHostDetailBindItemsRepo;
import com.jit.server.service.MonitorHostDetailBindItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: MonitorHostDetailBindItemsImpl
 * @Author: zengxin_miao
 * @Date: 2020.07.21
 */
@Service
public class MonitorHostDetailBindItemsImpl implements MonitorHostDetailBindItemsService {

    @Autowired
    private MonitorHostDetailBindItemsRepo monitorHostDetailBindItemsRepo;

    @Override
    public MonitorHostDetailBindItems saveOrUpdateMonitorHostDetailBindItems(MonitorHostDetailBindItems monitorHostDetailBindItems) throws Exception {
        return monitorHostDetailBindItemsRepo.saveAndFlush(monitorHostDetailBindItems);
    }

    @Override
    public List<MonitorHostDetailBindItems> findMonitorHostDetailBindItemsByHostId(String hostId, int isDeleted) throws Exception {
        return monitorHostDetailBindItemsRepo.findByHostIdAndIsDeleted(hostId, isDeleted);
    }

    @Override
    public MonitorHostDetailBindItems findById(String id) throws Exception {
        return monitorHostDetailBindItemsRepo.getOne(id);
    }
}
