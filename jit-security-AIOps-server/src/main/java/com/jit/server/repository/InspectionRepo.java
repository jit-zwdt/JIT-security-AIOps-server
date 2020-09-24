package com.jit.server.repository;

import com.jit.server.pojo.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: jian_liu
 * @Date: 2020/09/23 10:25
 */
@Repository
public interface InspectionRepo extends JpaRepository<HostEntity, String>, JpaSpecificationExecutor<HostEntity> {

    @Query("SELECT h FROM HostEntity h WHERE h.deleted = 0")
    List<HostEntity> getHostInfo();

    @Query("SELECT h FROM HostEntity h WHERE h.deleted = 0 and h.id = ?1")
    List<HostEntity> getHostInfoById(String id);

}
