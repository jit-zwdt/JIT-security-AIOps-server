package com.jit.server.service;

import com.jit.server.pojo.MonitorHostDetailBindGraphs;
import com.jit.server.pojo.MonitorHostDetailBindItems;

import java.util.List;

/**
 * @Description: MonitorHostDetailBindGraphsService
 * @Author: lancelot
 * @Date: 2020.07.30
 */
public interface MonitorHostDetailBindGraphsService {

    MonitorHostDetailBindGraphs saveOrUpdateMonitorHostDetailBindGraphs(MonitorHostDetailBindGraphs monitorHostDetailBindGraphs) throws Exception;

    List<MonitorHostDetailBindGraphs> findMonitorHostDetailBindGraphsByHostId(String hostId, int isDeleted) throws Exception;

    MonitorHostDetailBindGraphs findById(String id) throws Exception;

    MonitorHostDetailBindGraphs findByHostIdAndGraphIdAndIsDeleted(String hostId, String graphId, int isDeleted) throws Exception;
}
