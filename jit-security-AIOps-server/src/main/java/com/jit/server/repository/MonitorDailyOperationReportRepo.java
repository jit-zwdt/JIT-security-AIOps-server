package com.jit.server.repository;

import com.jit.server.pojo.MonitorDailyOperationReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020.10.22
 */
@Repository
public interface MonitorDailyOperationReportRepo extends JpaRepository<MonitorDailyOperationReportEntity, String>, JpaSpecificationExecutor<MonitorDailyOperationReportEntity> {

}
