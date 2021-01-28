package com.jit.server.repository;

import com.jit.server.dto.MonitorDailyOperationReportDTO;
import com.jit.server.pojo.MonitorDailyOperationReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020.10.22
 */
@Repository
public interface MonitorDailyOperationReportRepo extends JpaRepository<MonitorDailyOperationReportEntity, String>, JpaSpecificationExecutor<MonitorDailyOperationReportEntity> {

    MonitorDailyOperationReportEntity findByIdAndIsDeleted(String id, int isNotDeleted);

    @Query("select new com.jit.server.dto.MonitorDailyOperationReportDTO(e.id, e.operationUser, e.operationTime, e.signature, e.signatureDate, e.newProblemNum, e.newProblemDetail, " +
            "e.newProblemTotal, e.claimedProblemNum, e.claimedProblemDetail, e.claimedProblemTotal, e.processingProblemNum, e.processingProblemDetail, " +
            "e.processingProblemTotal, e.solvedProblemNum, e.solvedProblemDetail, e.solvedProblemTotail, e.gmtCreate) from MonitorDailyOperationReportEntity e where e.id = ?1 and e.isDeleted = 0")
    MonitorDailyOperationReportDTO findMonitorDailyOperationReportById(String id);
}
