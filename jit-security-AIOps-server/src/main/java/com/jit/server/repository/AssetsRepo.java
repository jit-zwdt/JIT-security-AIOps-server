package com.jit.server.repository;

import com.jit.server.pojo.AssetsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/16 13:25
 */
@Repository
public interface AssetsRepo extends JpaRepository<AssetsEntity, String>, JpaSpecificationExecutor<AssetsEntity> {



    public Page<AssetsEntity> findAll(Specification<AssetsEntity> spec, Pageable pageable);
}
