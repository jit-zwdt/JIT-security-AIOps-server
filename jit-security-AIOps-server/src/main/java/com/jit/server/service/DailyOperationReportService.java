package com.jit.server.service;

import com.jit.server.pojo.MonitorDailyOperationReportEntity;

import java.util.List;

public interface DailyOperationReportService {
    List<String> getTheDateNewProblemList(String auth) throws Exception;

    List<String> getTheMonthNewProblemList(String auth) throws Exception;

    List<String> getTheDateClaimedProblemList(String auth) throws Exception;

    List<String> getTheMonthClaimedProblemList(String auth) throws Exception;

    List<String> getTheDateProcessingProblemList(String auth) throws Exception;

    List<String> getTheMonthProcessingProblemList(String auth) throws Exception;

    List<String> getTheDateSolvedProblemList(String auth) throws Exception;

    List<String> getTheMonthSolvedProblemList(String auth) throws Exception;

    MonitorDailyOperationReportEntity addDailyOperationReport(MonitorDailyOperationReportEntity monitorDailyOperationReportEntity) throws Exception;
}
