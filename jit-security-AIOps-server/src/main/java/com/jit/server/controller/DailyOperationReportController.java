package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorDailyOperationReportEntity;
import com.jit.server.request.DailyOperationReportParams;
import com.jit.server.service.DailyOperationReportService;
import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @ResponseBody
    @GetMapping(value = "/getDailyOperationReport")
    public Result getDailyOperationReport(HttpServletRequest req) {
        try {
            MonitorDailyOperationReportEntity monitorDailyOperationReportEntity = new MonitorDailyOperationReportEntity();
            monitorDailyOperationReportEntity.setOperationUser(userService.findNamebyUsername());
            monitorDailyOperationReportEntity.setOperationTime(LocalDateTime.now());
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            // new problem
            List<String> theDateNewProblemList = dailyOperationReportService.getTheDateNewProblemList(auth);
            monitorDailyOperationReportEntity.setNewProblemNum(theDateNewProblemList != null ? String.valueOf(theDateNewProblemList.size()) : "0");
            monitorDailyOperationReportEntity.setNewProblemDetail(theDateNewProblemList != null ? StringUtils.join(theDateNewProblemList, "</br>") : "");
            List<String> theMonthNewProblemList = dailyOperationReportService.getTheMonthNewProblemList(auth);
            monitorDailyOperationReportEntity.setNewProblemTotal(theMonthNewProblemList != null ? String.valueOf(theMonthNewProblemList.size()) : "0");
            // claimed problem
            List<String> theDateClaimedProblemList = dailyOperationReportService.getTheDateClaimedProblemList(auth);
            monitorDailyOperationReportEntity.setClaimedProblemNum(theDateClaimedProblemList != null ? String.valueOf(theDateClaimedProblemList.size()) : "0");
            monitorDailyOperationReportEntity.setClaimedProblemDetail(theDateClaimedProblemList != null ? StringUtils.join(theDateClaimedProblemList, "</br>") : "");
            List<String> theMonthClaimedProblemList = dailyOperationReportService.getTheMonthClaimedProblemList(auth);
            monitorDailyOperationReportEntity.setClaimedProblemTotal(theMonthClaimedProblemList != null ? String.valueOf(theMonthClaimedProblemList.size()) : "0");
            // processing problem
            List<String> theDateProcessingProblemList = dailyOperationReportService.getTheDateProcessingProblemList(auth);
            monitorDailyOperationReportEntity.setProcessingProblemNum(theDateProcessingProblemList != null ? String.valueOf(theDateProcessingProblemList.size()) : "0");
            monitorDailyOperationReportEntity.setProcessingProblemDetail(theDateProcessingProblemList != null ? StringUtils.join(theDateProcessingProblemList, "</br>") : "");
            List<String> theMonthProcessingProblemList = dailyOperationReportService.getTheMonthProcessingProblemList(auth);
            monitorDailyOperationReportEntity.setProcessingProblemTotal(theMonthProcessingProblemList != null ? String.valueOf(theMonthProcessingProblemList.size()) : "0");
            // solved problem
            List<String> theDateSolvedProblemList = dailyOperationReportService.getTheDateSolvedProblemList(auth);
            monitorDailyOperationReportEntity.setSolvedProblemNum(theDateSolvedProblemList != null ? String.valueOf(theDateSolvedProblemList.size()) : "0");
            monitorDailyOperationReportEntity.setSolvedProblemDetail(theDateSolvedProblemList != null ? StringUtils.join(theDateSolvedProblemList, "</br>") : "");
            List<String> theMonthSolvedProblemList = dailyOperationReportService.getTheMonthSolvedProblemList(auth);
            monitorDailyOperationReportEntity.setSolvedProblemTotail(theMonthSolvedProblemList != null ? String.valueOf(theMonthSolvedProblemList.size()) : "0");
            return Result.SUCCESS(monitorDailyOperationReportEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PostMapping(value = "/addDailyOperationReport")
    public Result addDailyOperationReport(@RequestBody DailyOperationReportParams params, HttpServletRequest req) {
        try {
            if (params != null) {
                MonitorDailyOperationReportEntity monitorDailyOperationReportEntity = new MonitorDailyOperationReportEntity();
                BeanUtils.copyProperties(params, monitorDailyOperationReportEntity);
                monitorDailyOperationReportEntity.setGmtCreate(LocalDateTime.now());
                monitorDailyOperationReportEntity.setGmtModified(LocalDateTime.now());
                monitorDailyOperationReportEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
                return Result.SUCCESS(dailyOperationReportService.addDailyOperationReport(monitorDailyOperationReportEntity));
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
    public Result getDailyOperationReports(@RequestBody PageRequest<Map<String, String>> params) {

        try {
            Page<MonitorDailyOperationReportEntity> monitorDailyOperationReportEntities = dailyOperationReportService.getDailyOperationReports(params);
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

}