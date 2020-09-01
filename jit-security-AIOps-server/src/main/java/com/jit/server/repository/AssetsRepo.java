package com.jit.server.repository;

import com.jit.server.pojo.MonitorAssetsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/16 13:25
 */
@Repository
public interface AssetsRepo extends JpaRepository<MonitorAssetsEntity, String>, JpaSpecificationExecutor<MonitorAssetsEntity> {

    @Override
    public Page<MonitorAssetsEntity> findAll(Specification<MonitorAssetsEntity> spec, Pageable pageable);
}
