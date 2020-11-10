package com.jit.server.service;

import com.jit.server.dto.DailyOperationReportDTO;
import com.jit.server.pojo.MonitorDailyOperationReportEntity;
import com.jit.server.request.DailyOperationReportParams;
import com.jit.server.request.MonitorTemplatesParams;
import com.jit.server.util.PageRequest;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

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

    Page<MonitorDailyOperationReportEntity> getDailyOperationReports(PageRequest<Map<String,String>> params) throws Exception;

    MonitorDailyOperationReportEntity getDailyOperationReportById(String id) throws Exception;

    /**
     * 导出 Xls 表格 数据根据传入的二维数组进行构建
     * @param dataArray 二维数组数据对象
     * @return Xls 表格对象
     */
    HSSFWorkbook exportDailyXls(String[][] dataArray);
}
