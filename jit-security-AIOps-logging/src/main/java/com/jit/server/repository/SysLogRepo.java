package com.jit.server.repository;

import com.jit.server.pojo.SysLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogRepo extends JpaRepository<SysLogEntity, String>, JpaSpecificationExecutor<SysLogEntity> {

    @Query(value = "SELECT s.name FROM sys_user s where s.is_deleted = '0' AND s.username = ?1", nativeQuery = true)
    String getUserName(String name);

    @Query(value = "SELECT s.id FROM sys_user s where s.is_deleted = '0' AND s.username = ?1", nativeQuery = true)
    String getUserId(String name);
}
