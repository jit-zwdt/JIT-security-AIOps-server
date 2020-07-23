package com.jit.server.repository;

import com.jit.server.pojo.AssetsEntity;
import com.jit.server.pojo.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/16 13:25
 */
@Repository
public interface AssetsRepo extends JpaRepository<AssetsEntity, String>, JpaSpecificationExecutor<AssetsEntity> {

    @Override
    public Page<AssetsEntity> findAll(Specification<AssetsEntity> spec, Pageable pageable);
}
