package com.jit.server.service;

import com.jit.server.pojo.SysScheduleTaskEntity;
import com.jit.server.request.ScheduleTaskParams;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SysScheduleTaskService {

    String saveAndScheduleJob(SysScheduleTaskEntity sysScheduleTaskEntity) throws Exception;

    SysScheduleTaskEntity getSysScheduleTaskById(String id) throws Exception;

    Page<SysScheduleTaskEntity> getSysScheduleTasks(PageRequest<Map<String, Object>> params) throws Exception;

    boolean stopScheduleTask(String jobKey) throws Exception;

    void startScheduleTask(String className, String methodName, String cron, String param) throws Exception;

    List<SysScheduleTaskEntity> getScheduleTaskList() throws Exception;

    List<SysScheduleTaskEntity> getSysScheduleTaskByParams(String jobClassName, String jobMethodName, String cronExpression) throws Exception;

    List<SysScheduleTaskEntity> getSysScheduleTaskByParams2(String id, String jobClassName, String jobMethodName, String cronExpression) throws Exception;

    String addScheduleTask(ScheduleTaskParams scheduleTaskParams) throws Exception;

    void delScheduleTask(String id) throws Exception;

    void changeStatus(String id) throws Exception;
}
