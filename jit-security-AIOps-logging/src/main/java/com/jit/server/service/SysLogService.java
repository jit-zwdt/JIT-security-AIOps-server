package com.jit.server.service;

import com.jit.server.pojo.SysLogEntity;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface SysLogService {
    SysLogEntity saveOrUpdateLog(SysLogEntity sysLogEntity) throws Exception;

    String getUserName(String name) throws Exception;

    String getUserId(String name) throws Exception;

    /**
     * 根据传入的信息进行查询日志数据
     * @param logType 日志类型 0:登录日志;1:操作日志;2:错误日志
     * @param logContent 日志内容
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param operationType 操作日志类型 0:未定义;1:添加;2:查询;3:修改;4:删除
     * @param currentPage 当前页
     * @param currentSize 每页的条数
     * @return 统一返回数据对象
     */
    Page<SysLogEntity> findSysLog(int logType, String logContent, LocalDateTime startTime, LocalDateTime endTime, int operationType, int currentPage, int currentSize);

}
