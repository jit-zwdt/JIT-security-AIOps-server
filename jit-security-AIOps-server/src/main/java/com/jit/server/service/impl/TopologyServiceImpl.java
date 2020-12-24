package com.jit.server.service.impl;

import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorTopologyEntity;
import com.jit.server.repository.TopologyRepo;
import com.jit.server.service.TopologyService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TopologyServiceImpl implements TopologyService {

    @Autowired
    TopologyRepo topologyRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<MonitorTopologyEntity> getMonitorTopologInfo(String id) throws Exception {
        return topologyRepo.findById(id);
    }

    @Override
    public List<MonitorTopologyEntity> getMonitorTopologAllInfo(String infoName) throws Exception {
        return topologyRepo.findByInfoNameLikeAndIsDeleted("%"+infoName+"%", ConstUtil.IS_NOT_DELETED);
    }

    @Override
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

}
