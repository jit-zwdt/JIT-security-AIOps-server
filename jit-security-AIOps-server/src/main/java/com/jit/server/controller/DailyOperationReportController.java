package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.dto.MonitorDailyOperationReportDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorDailyOperationReportEntity;
import com.jit.server.request.DailyOperationReportParams;
import com.jit.server.service.DailyOperationReportService;
import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.10.19
 */

@Slf4j
@RestController
@RequestMapping("/dailyOperationReport")
public class DailyOperationReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private DailyOperationReportService dailyOperationReportService;


    @GetMapping(value = "/getDailyOperationReport")
    @AutoLog(value = "当日运维日报-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public @ResponseBody
    Result getDailyOperationReport(HttpServletRequest req) {
        try {
            MonitorDailyOperationReportDTO monitorDailyOperationReportDTO = new MonitorDailyOperationReportDTO();
            monitorDailyOperationReportDTO.setOperationUser(userService.findNamebyUsername());
            monitorDailyOperationReportDTO.setOperationTime(LocalDateTime.now());
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            // new problem
            List<String> theDateNewProblemList = dailyOperationReportService.getTheDateNewProblemList(auth);
            monitorDailyOperationReportDTO.setNewProblemNum(theDateNewProblemList != null ? String.valueOf(theDateNewProblemList.size()) : "0");
            monitorDailyOperationReportDTO.setNewProblemDetail(theDateNewProblemList != null ? StringUtils.join(theDateNewProblemList, "</br>") : "");
            List<String> theMonthNewProblemList = dailyOperationReportService.getTheMonthNewProblemList(auth);
            monitorDailyOperationReportDTO.setNewProblemTotal(theMonthNewProblemList != null ? String.valueOf(theMonthNewProblemList.size()) : "0");
            // claimed problem
            List<String> theDateClaimedProblemList = dailyOperationReportService.getTheDateClaimedProblemList(auth);
            monitorDailyOperationReportDTO.setClaimedProblemNum(theDateClaimedProblemList != null ? String.valueOf(theDateClaimedProblemList.size()) : "0");
            monitorDailyOperationReportDTO.setClaimedProblemDetail(theDateClaimedProblemList != null ? StringUtils.join(theDateClaimedProblemList, "</br>") : "");
            List<String> theMonthClaimedProblemList = dailyOperationReportService.getTheMonthClaimedProblemList(auth);
            monitorDailyOperationReportDTO.setClaimedProblemTotal(theMonthClaimedProblemList != null ? String.valueOf(theMonthClaimedProblemList.size()) : "0");
            // processing problem
            List<String> theDateProcessingProblemList = dailyOperationReportService.getTheDateProcessingProblemList(auth);
            monitorDailyOperationReportDTO.setProcessingProblemNum(theDateProcessingProblemList != null ? String.valueOf(theDateProcessingProblemList.size()) : "0");
            monitorDailyOperationReportDTO.setProcessingProblemDetail(theDateProcessingProblemList != null ? StringUtils.join(theDateProcessingProblemList, "</br>") : "");
            List<String> theMonthProcessingProblemList = dailyOperationReportService.getTheMonthProcessingProblemList(auth);
            monitorDailyOperationReportDTO.setProcessingProblemTotal(theMonthProcessingProblemList != null ? String.valueOf(theMonthProcessingProblemList.size()) : "0");
            // solved problem
            List<String> theDateSolvedProblemList = dailyOperationReportService.getTheDateSolvedProblemList(auth);
            monitorDailyOperationReportDTO.setSolvedProblemNum(theDateSolvedProblemList != null ? String.valueOf(theDateSolvedProblemList.size()) : "0");
            monitorDailyOperationReportDTO.setSolvedProblemDetail(theDateSolvedProblemList != null ? StringUtils.join(theDateSolvedProblemList, "</br>") : "");
            List<String> theMonthSolvedProblemList = dailyOperationReportService.getTheMonthSolvedProblemList(auth);
            monitorDailyOperationReportDTO.setSolvedProblemTotail(theMonthSolvedProblemList != null ? String.valueOf(theMonthSolvedProblemList.size()) : "0");
            return Result.SUCCESS(monitorDailyOperationReportDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PostMapping(value = "/addDailyOperationReport")
    @AutoLog(value = "当日运维日报-保存", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addDailyOperationReport(@RequestBody DailyOperationReportParams params, HttpServletRequest req) {
        try {
            if (params != null) {
                MonitorDailyOperationReportEntity monitorDailyOperationReportEntity = new MonitorDailyOperationReportEntity();
                BeanUtils.copyProperties(params, monitorDailyOperationReportEntity);
                monitorDailyOperationReportEntity.setGmtCreate(LocalDateTime.now());
                monitorDailyOperationReportEntity.setGmtModified(LocalDateTime.now());
                monitorDailyOperationReportEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
                dailyOperationReportService.addDailyOperationReport(monitorDailyOperationReportEntity);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PostMapping(value = "/getDailyOperationReports")
    @AutoLog(value = "历史运维日报列表-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getDailyOperationReports(@RequestBody PageRequest<Map<String, String>> params) {

        try {
            Page<MonitorDailyOperationReportDTO> monitorDailyOperationReportEntities = dailyOperationReportService.getDailyOperationReports(params);
            Map<String, Object> result = new HashMap<>();
            result.put("page", params.getPage());
            result.put("size", params.getSize());
            result.put("totalRow", monitorDailyOperationReportEntities.getTotalElements());
            result.put("totalPage", monitorDailyOperationReportEntities.getTotalPages());
            result.put("dataList", monitorDailyOperationReportEntities.getContent());
            return Result.SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getDailyOperationReported")
    @AutoLog(value = "历史运维日报列表-查看", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getDailyOperationReported(@RequestParam String id) {

        try {
            if (StringUtils.isNotBlank(id)) {
                return Result.SUCCESS(dailyOperationReportService.getDailyOperationReportById(id));
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    /**
     * 下载运维日报的 Xls 文件
     *
     * @param dailyOperationReport 传入数据集对象
     * @param response             HttpServletResponse 对象
     * @throws IOException
     */
    @PostMapping("/exportDaily")
    @AutoLog(value = "当日运维日报/历史运维日报-导出", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public void exportDaily(@RequestBody MonitorDailyOperationReportEntity dailyOperationReport, HttpServletResponse response) {
        //声明日期转换对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String[][] dataArray = {
                {dailyOperationReport.getOperationUser(), formatter.format(dailyOperationReport.getOperationTime())},
                {ExportXlsFileConst.OPERATION_REPORT_PROBLEMS, dailyOperationReport.getNewProblemNum(), dailyOperationReport.getNewProblemDetail().replaceAll("</br>", "\r\n"), dailyOperationReport.getNewProblemTotal()},
                {ExportXlsFileConst.OPERATION_REPORT_CLAIM_PROBLEMS, dailyOperationReport.getClaimedProblemNum(), dailyOperationReport.getClaimedProblemDetail(), dailyOperationReport.getClaimedProblemTotal()},
                {ExportXlsFileConst.OPERATION_REPORT_HANDLING_PROBLEMS, dailyOperationReport.getProcessingProblemNum(), dailyOperationReport.getProcessingProblemDetail(), dailyOperationReport.getProcessingProblemTotal()},
                {ExportXlsFileConst.OPERATION_REPORT_SOLVE_PROBLEMS, dailyOperationReport.getSolvedProblemNum(), dailyOperationReport.getSolvedProblemDetail(), dailyOperationReport.getSolvedProblemTotail()}
        };
        OutputStream out = null;
        try {
            //获取导出的 Xls 文件
            HSSFWorkbook workbook = dailyOperationReportService.exportDailyXls(dataArray);
            //获取响应流
            out = response.getOutputStream();
            //设置响应协议为响应xls文件
            response.setContentType("application/octet-stream");
            //设置弹出框
            response.setHeader("Content-Disposition", "attachment; fileName=" + UUID.randomUUID() + ".xls");
            //写出
            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                //关闭流
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
