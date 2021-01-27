package com.jit.server.repository;

import com.jit.server.dto.MonitorTypesDTO;
import com.jit.server.pojo.MonitorTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020-06-17 09:46:28
 */
@Repository
public interface MonitorTypeRepo extends JpaRepository<MonitorTypeEntity, String>, JpaSpecificationExecutor<MonitorTypeEntity> {

    List<MonitorTypeEntity> findByPidAndIsDeletedOrderByOrderNum(String pid, int isDeleted);

    List<MonitorTypeEntity> findByPidNotAndIsDeletedOrderByOrderNum(String pid, int isDeleted);

    @Query("select e.type from MonitorTypeEntity e where e.isDeleted = 0 and e.id = ?1")
    Object getTypeById(String id);

    @Query("select new com.jit.server.dto.MonitorTypesDTO(e.id, e.type, e.pid, e.orderNum ) from MonitorTypeEntity e where e.isDeleted = 0 and e.pid = ?1 order by e.orderNum")
    List<MonitorTypesDTO> findMonitorTypesByPid(String pid0);

    @Query("select new com.jit.server.dto.MonitorTypesDTO(e.id, e.type, e.pid, e.orderNum ) from MonitorTypeEntity e where e.isDeleted = 0 and e.pid <> ?1 order by e.orderNum")
    List<MonitorTypesDTO> findMonitorSubTypesByPid(String pid0);
}
