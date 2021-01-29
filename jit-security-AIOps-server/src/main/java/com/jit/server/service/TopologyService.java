package com.jit.server.service;

import com.jit.server.dto.MonitorTopologyDTO;
import com.jit.server.pojo.MonitorTopologyEntity;

import java.util.List;

public interface TopologyService {

    MonitorTopologyDTO getMonitorTopologyInfo(String id) throws Exception;

    MonitorTopologyEntity getMonitorTopologyInfoById(String id) throws Exception;

    List<MonitorTopologyDTO> getMonitorTopologyAllInfo(String infoName) throws Exception;

    void addTopology(MonitorTopologyEntity topology) throws Exception;

    List<Object> getTopologyItemInfo(String ip) throws Exception;
}
