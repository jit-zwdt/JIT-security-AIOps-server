package com.jit.server.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jit.server.annotation.AutoLog;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysLogEntity;
import com.jit.server.service.SysLogService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 日志系统的控制层
 * @author oldwang <br />
 * 创建时间: 2020.12.23
 */
@Slf4j
@RestController
@RequestMapping("/sys/syslog")
public class SysLogController {

    /**
     * 日志管理处理业务层对象
     */
    @Autowired
    private SysLogService sysLogService;

    /**
     * 根据传入的信息进行查询日志数据
     * @param logType 日志类型 0:登录日志;1:操作日志;2:错误日志
     * @param logContent 日志内容 (查询全部传 null)
     * @param startTime 开始时间 (查询全部传 null 查询一个也是好用的 查询的是当前时间范围后的所有数据)
     * @param endTime 结束时间 (查询全部传 null 查询一个也是好用的 查询的是当前时间范围前的所有数据)
     * @param operationType 操作日志类型 0:未定义;1:添加;2:查询;3:修改;4:删除;5:导入;6:导出;7:上传;8:下载 (查询全部传入 "")
     * @param currentPage 当前页 (必须传入的字段)
     * @param currentSize 每页的条数 (必须传入的字段)
     * @return 统一返回数据对象
     */
    @PostMapping("/getSysLogs")
    @AutoLog(value = "日志管理-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result findSysLog(int logType,
                             @RequestParam(value = "logContent" , required = false) String logContent ,
                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") @RequestParam(value = "startTime" , required = false) LocalDateTime startTime ,
                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") @RequestParam(value = "endTime" , required = false) LocalDateTime endTime ,
                             @RequestParam(value = "operationType" , required = false) Integer operationType,
                             int currentPage ,
                             int currentSize){
        try {
            //进行调用业务层进行数据的查询
            Page<SysLogEntity> sysLogs = sysLogService.findSysLog(logType, logContent, startTime, endTime, operationType, currentPage, currentSize);
            //返回数据
            return Result.SUCCESS(sysLogs);
        } catch (Exception e) {
            e.printStackTrace();
            //返回错误数据
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
