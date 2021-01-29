package com.jit.server.repository;

import com.jit.server.dto.SysScheduleTaskDTO;
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

    @Query("select new com.jit.server.dto.SysScheduleTaskDTO(s.id, s.jobClassName, s.jobMethodName, s.cronExpression, s.jsonParam, s.description, s.jobGroup, s.status) from SysScheduleTaskEntity s where s.isDeleted = 0 and s.jobClassName = ?1 and s.jobMethodName = ?2 and s.cronExpression = ?3 and s.jsonParam = ?4 order by s.gmtCreate")
    List<SysScheduleTaskDTO> getSysScheduleTaskByParams(String jobClassName, String jobMethodName, String cronExpression, String param);

    @Query("select new com.jit.server.dto.SysScheduleTaskDTO(s.id, s.jobClassName, s.jobMethodName, s.cronExpression, s.jsonParam, s.description, s.jobGroup, s.status) from SysScheduleTaskEntity s where s.isDeleted = 0 and s.id <> ?1 and s.jobClassName = ?2 and s.jobMethodName = ?3 and s.cronExpression = ?4 and s.jsonParam = ?5 order by s.gmtCreate")
    List<SysScheduleTaskDTO> getSysScheduleTaskByParams2(String id, String jobClassName, String jobMethodName, String cronExpression, String param);

    @Query("select new com.jit.server.dto.SysScheduleTaskDTO(s.id, s.jobClassName, s.jobMethodName, s.cronExpression, s.jsonParam, s.description, s.jobGroup, s.status) from SysScheduleTaskEntity s where s.isDeleted = 0 and s.id = ?1")
    SysScheduleTaskDTO findSysScheduleTaskById(String id);

    @Query("select new com.jit.server.dto.SysScheduleTaskDTO(s.id, s.jobClassName, s.jobMethodName, s.cronExpression, s.jsonParam, s.description, s.jobGroup, s.status) from SysScheduleTaskEntity s where s.isDeleted = ?1 and s.status = ?2 order by s.gmtCreate")
    List<SysScheduleTaskDTO> findScheduleTasksByIsDeletedAndStatus(int isDeleted, int status);
}
