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

    @Query("select e from MonitorClaimEntity e where e.claimUserId =  ?1")
    List<MonitorClaimEntity> findClaimByUser(String claimUserId);

    @Transactional
    @Modifying
    @Query("update MonitorClaimEntity e set  e.isRegister = ?2,e.isResolve =  ?3,e.handleTime = ?4 where e.id =  ?1")
    void updateClaimAfterRegister(String id,int isRegister,int isResolve,String handleTime);
}
