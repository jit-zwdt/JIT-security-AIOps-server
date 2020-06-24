package com.jit.server.repository;

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

    @Query("select e.type from MonitorTypeEntity e where e.isDeleted = 0 and e.id = ?1")
    Object getTypeById(String id);
}
