package com.jit.server.repository;

import com.jit.server.pojo.SysCronExpressionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: SysCronExpressionRepo
 * @Author: zengxin_miao
 * @Date: 2020.10.13
 */
@Repository
public interface SysCronExpressionRepo extends JpaRepository<SysCronExpressionEntity, String>, JpaSpecificationExecutor<SysCronExpressionEntity> {

    @Query("select e.cronExpressionDesc,e.cronExpression from SysCronExpressionEntity e where e.isDeleted = 0 ")
    List<Object> getCronExpressionObject();
}
