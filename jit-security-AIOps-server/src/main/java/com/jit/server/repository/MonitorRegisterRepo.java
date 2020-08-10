package com.jit.server.repository;

import com.jit.server.pojo.MonitorRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MonitorRegisterRepo extends JpaRepository<MonitorRegisterEntity, String>, JpaSpecificationExecutor<MonitorRegisterEntity> {

    @Query("select e.claimId,e.gmtCreate,e.isResolve,e.problemProcess,e.problemReason,e.problemSolution,e.problemType,c.problemHandleTime from MonitorRegisterEntity e,MonitorClaimEntity c where e.claimId =  ?1 and e.claimId = c.id order by e.gmtCreate")
    List<Object> findByClaimId(String id);

    List<MonitorRegisterEntity> findByClaimIdAndProblemTypeAndIsResolve(String id, String problemType,int isResolve);

    List<MonitorRegisterEntity> findByClaimIdAndIsResolve(String id, int isResolve);
}
