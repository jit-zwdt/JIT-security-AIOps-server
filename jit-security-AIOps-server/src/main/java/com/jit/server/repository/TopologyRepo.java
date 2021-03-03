package com.jit.server.repository;

import com.jit.server.dto.MonitorTopologyDTO;
import com.jit.server.pojo.MonitorTopologyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select new com.jit.server.dto.MonitorTopologyDTO(s.id, s.jsonParam, s.infoName, s.gmtCreate, s.homePageDisplay) from MonitorTopologyEntity s where s.infoName like %?1% and s.isDeleted = 0 order by s.gmtCreate desc")
    List<MonitorTopologyDTO> findMonitorTopologyByInfoName(String infoName);

    @Query("select new com.jit.server.dto.MonitorTopologyDTO(s.id, s.jsonParam, s.infoName, s.gmtCreate, s.homePageDisplay) from MonitorTopologyEntity s where s.id = ?1 and s.isDeleted = 0")
    MonitorTopologyDTO findMonitorTopologyById(String id);

    MonitorTopologyEntity findByIdAndIsDeleted(String id, int isNotDeleted);

    @Modifying
    @Query(value = "update MonitorTopologyEntity t set t.homePageDisplay= 0 where t.isDeleted = 0")
    void updateTopologyNoneHomePageDisplay();

    @Modifying
    @Query(value = "update MonitorTopologyEntity t set t.homePageDisplay= 1 where t.id = ?1 and t.isDeleted = 0")
    void updateTopologyHomePageDisplay(String id);

    @Query(value = "select t.id from MonitorTopologyEntity t where t.homePageDisplay= 1 and t.isDeleted = 0")
    String getHomePageDisplayTopologyId();
}
