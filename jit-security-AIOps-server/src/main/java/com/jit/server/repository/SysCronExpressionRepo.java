package com.jit.server.repository;

import com.jit.server.pojo.SysCronExpressionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Description: SysCronExpressionRepo
 * @Author: zengxin_miao
 * @Date: 2020.10.13
 */
@Repository
public interface SysCronExpressionRepo extends JpaRepository<SysCronExpressionEntity, String>, JpaSpecificationExecutor<SysCronExpressionEntity> {

}
