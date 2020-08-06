package com.jit.server.repository;

import com.jit.server.pojo.MonitorRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MonitorRegisterRepo extends JpaRepository<MonitorRegisterEntity, String>, JpaSpecificationExecutor<MonitorRegisterEntity> {

    @Query("select e from MonitorRegisterEntity e  where e.problemId =  ?1 order by e.gmtCreate DESC ")
    List<MonitorRegisterEntity> findByProblemId(String id);
}
