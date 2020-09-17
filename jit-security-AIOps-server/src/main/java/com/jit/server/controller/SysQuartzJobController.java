package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysScheduleTaskEntity;
import com.jit.server.request.QuartzJobParams;
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
import java.util.Map;

/**
 * @Description: SysQuartzJobController
 * @Author: zengxin_miao
 * @Date: 2020/09/11
 */

@RestController
@RequestMapping("/sys/quartzJob")
@Slf4j
public class SysQuartzJobController {

    @Autowired
    private SysScheduleTaskService sysScheduleTaskService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping(value = "/getQuartzJobs")
    public Result getQuartzJobs(@RequestBody PageRequest<Map<String, Object>> params) {
        try {
            Page<SysScheduleTaskEntity> sysQuartzJobEntities = sysScheduleTaskService.getSysScheduleTasks(params);
            Map<String, Object> result = new HashMap<>(5);
            result.put("page", params.getPage());
            result.put("size", params.getSize());
            result.put("totalRow", sysQuartzJobEntities.getTotalElements());
            result.put("totalPage", sysQuartzJobEntities.getTotalPages());
            result.put("dataList", sysQuartzJobEntities.getContent());
            return Result.SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/addQuartzJob")
    public Result addQuartzJob(@RequestBody QuartzJobParams quartzJobParams) {
        try {
            SysScheduleTaskEntity sysScheduleTaskEntity;
            if (StringUtils.isBlank(quartzJobParams.getId())) {
                sysScheduleTaskEntity = new SysScheduleTaskEntity();
                sysScheduleTaskEntity.setGmtCreate(new Timestamp(System.currentTimeMillis()));
                sysScheduleTaskEntity.setCreateBy(userService.findIdByUsername());
                sysScheduleTaskEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
            } else {
                sysScheduleTaskEntity = sysScheduleTaskService.getSysScheduleTaskById(quartzJobParams.getId());
                sysScheduleTaskEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
            }
            sysScheduleTaskEntity.setJobClassName(quartzJobParams.getJobClassName());
            sysScheduleTaskEntity.setCronExpression(quartzJobParams.getCronExpression());
            sysScheduleTaskEntity.setJsonParam(quartzJobParams.getJsonParam());
            sysScheduleTaskEntity.setDescription(quartzJobParams.getDescription());
            sysScheduleTaskEntity.setJobGroup("".equals(quartzJobParams.getJobGroup()) ? null : quartzJobParams.getJobGroup());
            sysScheduleTaskEntity.setStatus(quartzJobParams.getStatus());
            return Result.SUCCESS(sysScheduleTaskService.saveAndScheduleJob(sysScheduleTaskEntity));
        } catch (ClassNotFoundException e) {
            return Result.ERROR(ExceptionEnum.SCHEDULER_USE_CLASS_EXCEPTION);
        } catch (RuntimeException e) {
            log.error("创建失败：原因 {}", e.getMessage());
            return Result.ERROR(ExceptionEnum.SCHEDULER_CREATE_EXCEPTION);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/delQuartzJob/{id}")
    public Result delQuartzJob(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                SysScheduleTaskEntity sysScheduleTaskEntity = sysScheduleTaskService.getSysScheduleTaskById(id);
                if (sysScheduleTaskEntity == null) {
                    return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
                } else {
                    boolean res = sysScheduleTaskService.stopScheduleTask(ConstUtil.JOB_ + id);
                    log.info("删除任务{}，结果{}", ConstUtil.JOB_ + id, res);
                    sysScheduleTaskEntity.setIsDeleted(1);
                    sysScheduleTaskEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                    sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
                    sysScheduleTaskService.saveAndScheduleJob(sysScheduleTaskEntity);
                    return Result.SUCCESS(true);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }
}
