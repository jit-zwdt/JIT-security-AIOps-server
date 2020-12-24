package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.exception.CronExpression;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.exception.SchedulerExistedException;
import com.jit.server.pojo.SysScheduleTaskEntity;
import com.jit.server.request.ScheduleTaskParams;
import com.jit.server.service.SysScheduleTaskService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: SysScheduleTaskController
 * @Author: zengxin_miao
 * @Date: 2020/09/11
 */

@RestController
@RequestMapping("/sys/scheduleTask")
@Slf4j
public class SysScheduleTaskController {

    @Autowired
    private SysScheduleTaskService sysScheduleTaskService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping(value = "/getScheduleTasks")
    @AutoLog(value = "定时任务-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getScheduleTasks(@RequestBody PageRequest<Map<String, Object>> params) {
        try {
            Page<SysScheduleTaskEntity> sysScheduleTaskEntities = sysScheduleTaskService.getSysScheduleTasks(params);
            Map<String, Object> result = new HashMap<>(5);
            result.put("page", params.getPage());
            result.put("size", params.getSize());
            result.put("totalRow", sysScheduleTaskEntities.getTotalElements());
            result.put("totalPage", sysScheduleTaskEntities.getTotalPages());
            result.put("dataList", sysScheduleTaskEntities.getContent());
            return Result.SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/addScheduleTask")
    @AutoLog(value = "定时任务-新增/编辑", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addScheduleTask(@RequestBody ScheduleTaskParams scheduleTaskParams) {
        try {
            return Result.SUCCESS(sysScheduleTaskService.addScheduleTask(scheduleTaskParams));
        } catch (SchedulerExistedException e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.SCHEDULER_EXISTED_EXCEPTION);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.SCHEDULER_USE_CLASS_EXCEPTION);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.SCHEDULER_USE_METHOD_EXCEPTION);
        } catch (CronExpression e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.SCHEDULER_CRON_EXPRESSION_EXCEPTION);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error("创建失败：原因 {}", e.getMessage());
            return Result.ERROR(ExceptionEnum.SCHEDULER_CREATE_EXCEPTION);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/delScheduleTask/{id}")
    @AutoLog(value = "定时任务-删除", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result delScheduleTask(@PathVariable String id) {
        try {
            sysScheduleTaskService.delScheduleTask(id);
            return Result.SUCCESS(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PutMapping(value = "/updateStatus/{id}")
    @AutoLog(value = "定时计划-修改启动状态 , 巡检计划管理-修改启动状态", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateStatus(@PathVariable String id) {
        try {
            sysScheduleTaskService.changeStatus(id);
            return Result.SUCCESS(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getScheduleTask/{id}")
    public Result getScheduleTask(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                SysScheduleTaskEntity sysScheduleTaskEntity = sysScheduleTaskService.getSysScheduleTaskById(id);
                if (sysScheduleTaskEntity == null) {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
                return Result.SUCCESS(sysScheduleTaskEntity);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }
}
