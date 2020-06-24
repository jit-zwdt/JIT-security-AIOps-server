package com.jit.server.repository;

import com.jit.server.pojo.MonitorTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
}
