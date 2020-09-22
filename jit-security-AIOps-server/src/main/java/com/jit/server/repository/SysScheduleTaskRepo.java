package com.jit.server.repository;

import com.jit.server.pojo.SysScheduleTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: SysScheduleTaskRepo
 * @Author: zengxin_miao
 * @Date: 2020/06/09 13:25
 */
@Repository
public interface SysScheduleTaskRepo extends JpaRepository<SysScheduleTaskEntity, String>, JpaSpecificationExecutor<SysScheduleTaskEntity> {


    SysScheduleTaskEntity findByIdAndIsDeleted(String id, int isDeleted);

    List<SysScheduleTaskEntity> findByIsDeletedAndStatusOrderByGmtCreate(int isNotDeleted, int status);
}
