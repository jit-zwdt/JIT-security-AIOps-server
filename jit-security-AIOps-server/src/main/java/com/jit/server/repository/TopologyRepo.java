package com.jit.server.repository;

import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorTopologyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: jian_liu
 * @Date: 2020/12/23 14:57:22
 */
@Repository
public interface TopologyRepo extends JpaRepository<MonitorTopologyEntity, String>, JpaSpecificationExecutor<MonitorTopologyEntity> {
    List<MonitorTopologyEntity> findByInfoNameLikeAndIsDeletedOrderByGmtCreateDesc(String infoName, int isDeleted);
}
