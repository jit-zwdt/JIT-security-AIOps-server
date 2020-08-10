package com.jit.server.repository;

import com.jit.server.pojo.MonitorClaimEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface MonitorClaimRepo extends JpaRepository<MonitorClaimEntity, String>, JpaSpecificationExecutor<MonitorClaimEntity> {

    @Query("select e from MonitorClaimEntity e where e.problemId =  ?1")
    MonitorClaimEntity getMonitorClaimEntityById(String problemId);

    @Query(value = "select * from monitor_claim e where e.claim_user_id =  ?1 and e.problem_name like %?2% and if(?3 != -1,e.is_resolve =?3,1=1)",nativeQuery = true)
    List<MonitorClaimEntity> findClaimByUser(String claimUserId,String problemName,int resolveType);

    @Transactional
    @Modifying
    @Query("update MonitorClaimEntity e set  e.isRegister = ?2,e.isResolve =  ?3,e.problemHandleTime = ?4 where e.id =  ?1")
    void updateClaimAfterRegister(String id,int isRegister,int isResolve,String handleTime);

    List<MonitorClaimEntity> findByIsResolve(int i);

    @Query("select e from MonitorClaimEntity e where e.isResolve=  ?1 and e.problemName like %?2%")
    List<MonitorClaimEntity> findByIsResolveAndProblemName(int i,String problemName);
}
