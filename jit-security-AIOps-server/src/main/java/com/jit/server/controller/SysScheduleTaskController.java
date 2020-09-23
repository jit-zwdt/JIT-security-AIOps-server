package com.jit.server.controller;

import com.jit.server.exception.CronExpression;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysRoleEntity;
import com.jit.server.pojo.SysScheduleTaskEntity;
import com.jit.server.request.ScheduleTaskParams;
import com.jit.server.service.SysScheduleTaskService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
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
    public Result addScheduleTask(@RequestBody ScheduleTaskParams scheduleTaskParams) {
        try {
            String id = scheduleTaskParams.getId();
            String jobClassName = scheduleTaskParams.getJobClassName();
            String jobMethodName = scheduleTaskParams.getJobMethodName();
            String cronExpression = scheduleTaskParams.getCronExpression();
            if (vaildateParams(id, jobClassName, jobMethodName, cronExpression)) {
                return Result.ERROR(ExceptionEnum.SCHEDULER_EXISTED_EXCEPTION);
            }
            SysScheduleTaskEntity sysScheduleTaskEntity;
            if (StringUtils.isBlank(scheduleTaskParams.getId())) {
                sysScheduleTaskEntity = new SysScheduleTaskEntity();
                sysScheduleTaskEntity.setGmtCreate(new Timestamp(System.currentTimeMillis()));
                sysScheduleTaskEntity.setCreateBy(userService.findIdByUsername());
                sysScheduleTaskEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
            } else {
                sysScheduleTaskEntity = sysScheduleTaskService.getSysScheduleTaskById(scheduleTaskParams.getId());
                sysScheduleTaskEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
            }
            sysScheduleTaskEntity.setJobClassName(jobClassName);
            sysScheduleTaskEntity.setJobMethodName(jobMethodName);
            sysScheduleTaskEntity.setCronExpression(cronExpression);
            sysScheduleTaskEntity.setJsonParam(scheduleTaskParams.getJsonParam());
            sysScheduleTaskEntity.setDescription(scheduleTaskParams.getDescription());
            sysScheduleTaskEntity.setJobGroup("".equals(scheduleTaskParams.getJobGroup()) ? null : scheduleTaskParams.getJobGroup());
            sysScheduleTaskEntity.setStatus(scheduleTaskParams.getStatus());
            return Result.SUCCESS(sysScheduleTaskService.saveAndScheduleJob(sysScheduleTaskEntity));
        } catch (ClassNotFoundException e) {
            return Result.ERROR(ExceptionEnum.SCHEDULER_USE_CLASS_EXCEPTION);
        } catch (NoSuchMethodException e) {
            return Result.ERROR(ExceptionEnum.SCHEDULER_USE_METHOD_EXCEPTION);
        } catch (CronExpression e) {
            return Result.ERROR(ExceptionEnum.SCHEDULER_CRON_EXPRESSION_EXCEPTION);
        } catch (RuntimeException e) {
            log.error("创建失败：原因 {}", e.getMessage());
            return Result.ERROR(ExceptionEnum.SCHEDULER_CREATE_EXCEPTION);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    /**
     * check param jobClassName, jobMethodName, cronExpression
     *
     * @param id
     * @param jobClassName
     * @param jobMethodName
     * @param cronExpression
     * @return
     * @throws Exception
     */
    private boolean vaildateParams(String id, String jobClassName, String jobMethodName, String cronExpression) throws Exception {
        boolean res = false;
        if (StringUtils.isBlank(id)) {
            List<SysScheduleTaskEntity> sysScheduleTaskEntityList = sysScheduleTaskService.getSysScheduleTaskByParams(jobClassName, jobMethodName, cronExpression);
            if (sysScheduleTaskEntityList != null && !sysScheduleTaskEntityList.isEmpty()) {
                res = true;
            }
        } else {
            List<SysScheduleTaskEntity> sysScheduleTaskEntityList = sysScheduleTaskService.getSysScheduleTaskByParams2(id, jobClassName, jobMethodName, cronExpression);
            if (sysScheduleTaskEntityList != null && !sysScheduleTaskEntityList.isEmpty()) {
                res = true;
            }
        }
        return res;
    }

    @ResponseBody
    @DeleteMapping(value = "/delScheduleTask/{id}")
    public Result delScheduleTask(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                SysScheduleTaskEntity sysScheduleTaskEntity = sysScheduleTaskService.getSysScheduleTaskById(id);
                if (sysScheduleTaskEntity == null) {
                    return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
                } else {
                    String key = sysScheduleTaskEntity.getJobClassName() + "." + sysScheduleTaskEntity.getJobMethodName() + "(" + sysScheduleTaskEntity.getCronExpression() + ")";
                    boolean res = sysScheduleTaskService.stopScheduleTask(key);
                    log.info("删除任务{}，结果{}", key, res);
                    if (res) {
                        sysScheduleTaskEntity.setIsDeleted(1);
                        sysScheduleTaskEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                        sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
                        sysScheduleTaskService.saveAndScheduleJob(sysScheduleTaskEntity);
                        return Result.SUCCESS(true);
                    } else {
                        return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
                    }
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PutMapping(value = "/changeStatus/{id}")
    public Result changeStatus(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                SysScheduleTaskEntity sysScheduleTaskEntity = sysScheduleTaskService.getSysScheduleTaskById(id);
                if (sysScheduleTaskEntity == null) {
                    return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
                } else {
                    if (ConstUtil.STATUS_NORMAL == sysScheduleTaskEntity.getStatus()) {
                        String key = sysScheduleTaskEntity.getJobClassName() + "." + sysScheduleTaskEntity.getJobMethodName() + "(" + sysScheduleTaskEntity.getCronExpression() + ")";
                        boolean res = sysScheduleTaskService.stopScheduleTask(key);
                        log.info("删除任务{}，结果{}", key, res);
                        if (res) {
                            sysScheduleTaskEntity.setStatus(ConstUtil.STATUS_STOP);
                            sysScheduleTaskEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                            sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
                            sysScheduleTaskService.saveAndScheduleJob(sysScheduleTaskEntity);
                            return Result.SUCCESS(true);
                        } else {
                            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
                        }
                    } else {
                        sysScheduleTaskService.startScheduleTask(sysScheduleTaskEntity.getJobClassName(), sysScheduleTaskEntity.getJobMethodName(), sysScheduleTaskEntity.getCronExpression(), sysScheduleTaskEntity.getJsonParam());
                        sysScheduleTaskEntity.setStatus(ConstUtil.STATUS_NORMAL);
                        sysScheduleTaskEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                        sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
                        sysScheduleTaskService.saveAndScheduleJob(sysScheduleTaskEntity);
                        return Result.SUCCESS(true);
                    }
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
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
