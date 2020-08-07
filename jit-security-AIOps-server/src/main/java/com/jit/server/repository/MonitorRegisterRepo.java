package com.jit.server.repository;

import com.jit.server.pojo.MonitorRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MonitorRegisterRepo extends JpaRepository<MonitorRegisterEntity, String>, JpaSpecificationExecutor<MonitorRegisterEntity> {

    @Query("select e from MonitorRegisterEntity e  where e.claimId =  ?1 order by e.gmtCreate")
    List<MonitorRegisterEntity> findByClaimId(String id);

    List<MonitorRegisterEntity> findByClaimIdAndProblemTypeAndIsResolve(String id, String problemType,int isResolve);

    List<MonitorRegisterEntity> findByClaimIdAndIsResolve(String id, int isResolve);
}
