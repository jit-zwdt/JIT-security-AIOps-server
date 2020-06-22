package com.jit.server.repository;

import com.jit.server.pojo.MonitorTemplatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020-06-17 09:46:28
 */
@Repository
public interface MonitorTemplatesRepo extends JpaRepository<MonitorTemplatesEntity, String>, JpaSpecificationExecutor<MonitorTemplatesEntity> {
}
