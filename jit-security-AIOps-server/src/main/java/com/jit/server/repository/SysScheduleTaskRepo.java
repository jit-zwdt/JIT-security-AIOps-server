package com.jit.server.repository;

import com.jit.server.pojo.SysScheduleTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select s from SysScheduleTaskEntity s where s.isDeleted = 0 and s.jobClassName = ?1 and s.jobMethodName = ?2 and s.cronExpression = ?3 order by s.gmtCreate")
    List<SysScheduleTaskEntity> getSysScheduleTaskByParams(String jobClassName, String jobMethodName, String cronExpression);

    @Query("select s from SysScheduleTaskEntity s where s.isDeleted = 0 and s.id <> ?1 and s.jobClassName = ?2 and s.jobMethodName = ?3 and s.cronExpression = ?4 order by s.gmtCreate")
    List<SysScheduleTaskEntity> getSysScheduleTaskByParams2(String id, String jobClassName, String jobMethodName, String cronExpression);

}