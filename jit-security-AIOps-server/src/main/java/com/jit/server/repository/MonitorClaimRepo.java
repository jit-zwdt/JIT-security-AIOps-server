package com.jit.server.repository;

import com.jit.server.pojo.MonitorClaimEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface MonitorClaimRepo extends JpaRepository<MonitorClaimEntity, String>, JpaSpecificationExecutor<MonitorClaimEntity> {

    @Query("select e from MonitorClaimEntity e where e.problemId =  ?1")
    MonitorClaimEntity getMonitorClaimEntityById(String problemId);
}
