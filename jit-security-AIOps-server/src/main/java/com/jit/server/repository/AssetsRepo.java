package com.jit.server.repository;

import com.jit.server.pojo.MonitorAssetsEntity;
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
 * @Author: yongbin_jiang
 * @Date: 2020/06/16 13:25
 */
@Repository
public interface AssetsRepo extends JpaRepository<MonitorAssetsEntity, String>, JpaSpecificationExecutor<MonitorAssetsEntity> {

    @Override
    Page<MonitorAssetsEntity> findAll(Specification<MonitorAssetsEntity> spec, Pageable pageable);

    @Query(value = "select e.id,e.name,e.number from MonitorAssetsEntity e where e.isDeleted = 0 and e.type = '0'")
    List<Object> findHardwareInfo();
}
