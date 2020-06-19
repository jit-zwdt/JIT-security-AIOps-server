package com.jit.server.repository;

import com.jit.server.pojo.AssetsEntity;
import com.jit.server.pojo.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/19 13:25
 */
@Repository
public interface HostRepo extends JpaRepository<HostEntity, String>, JpaSpecificationExecutor<HostEntity> {


    public Page<HostEntity> findAll(Specification<HostEntity> spec, Pageable pageable);
}
