package com.jit.server.service;

import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorTopologyEntity;

import java.util.List;
import java.util.Optional;

public interface TopologyService {

    Optional<MonitorTopologyEntity> getMonitorTopologInfo(String id) throws Exception;

    List<MonitorTopologyEntity> getMonitorTopologAllInfo(String infoName) throws Exception;

    void addTopology(MonitorTopologyEntity topology) throws Exception;

    List<Object> getTopologyItemInfo(String ip) throws Exception;
}
