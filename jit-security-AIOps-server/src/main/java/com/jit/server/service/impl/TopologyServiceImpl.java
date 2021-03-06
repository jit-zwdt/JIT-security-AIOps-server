package com.jit.server.service.impl;

import com.jit.server.dto.MonitorTopologyDTO;
import com.jit.server.pojo.MonitorTopologyEntity;
import com.jit.server.repository.TopologyRepo;
import com.jit.server.service.TopologyService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopologyServiceImpl implements TopologyService {

    @Autowired
    private TopologyRepo topologyRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MonitorTopologyDTO getMonitorTopologyInfo(String id) throws Exception {
        return topologyRepo.findMonitorTopologyById(id);
    }

    @Override
    public MonitorTopologyEntity getMonitorTopologyInfoById(String id) throws Exception {
        return topologyRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public List<MonitorTopologyDTO> getMonitorTopologyAllInfo(String infoName) throws Exception {
        return topologyRepo.findMonitorTopologyByInfoName(infoName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTopology(MonitorTopologyEntity topology) throws Exception {
        topologyRepo.save(topology);
    }

    @Override
    public List<Object> getTopologyItemInfo(String ip) throws Exception {
        String comditionalSQL = "";
        String orderbySQL = " order by hostentity.objectName,hostentity.gmtCreate desc ";
        String baseSQL = "SELECT " +
                "hostentity.hostId " +
                "FROM " +
                "HostEntity hostentity " +
                "WHERE hostentity.deleted=0 ";
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(ip)) {
            comditionalSQL += " and (hostentity.agentIp like :hostIp or hostentity.snmpIp like :hostIp)";
            map.put("hostIp", "%" + ip + "%");
        }
        String resSQL = baseSQL + comditionalSQL + orderbySQL;
        Query res = this.entityManager.createQuery(resSQL);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            res.setParameter(entry.getKey(), entry.getValue());
        }
        List<Object> resultList = res.getResultList();
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTopologyHomePageDisplay(String id) throws Exception {
        topologyRepo.updateTopologyNoneHomePageDisplay();
        topologyRepo.updateTopologyHomePageDisplay(id);
    }

    @Override
    public String getHomePageDisplayTopologyId() throws Exception {
        return topologyRepo.getHomePageDisplayTopologyId();
    }


}
