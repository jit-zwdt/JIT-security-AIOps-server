package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysQuartzJobEntity;
import com.jit.server.request.QuartzJobParams;
import com.jit.server.service.SysQuartzJobService;
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
    private SysQuartzJobService sysQuartzJobService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping(value = "/getQuartzJobs")
    public Result getQuartzJobs(@RequestBody PageRequest<Map<String, Object>> params) {
        try {
            Page<SysQuartzJobEntity> sysQuartzJobEntities = sysQuartzJobService.getQuartzJobs(params);
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
            SysQuartzJobEntity sysQuartzJobEntity;
            if (StringUtils.isBlank(quartzJobParams.getId())) {
                sysQuartzJobEntity = new SysQuartzJobEntity();
                sysQuartzJobEntity.setGmtCreate(new Timestamp(System.currentTimeMillis()));
                sysQuartzJobEntity.setCreateBy(userService.findIdByUsername());
                sysQuartzJobEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
            } else {
                sysQuartzJobEntity = sysQuartzJobService.getSysQuartzJobEntityById(quartzJobParams.getId());
                sysQuartzJobEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                sysQuartzJobEntity.setUpdateBy(userService.findIdByUsername());
            }
            sysQuartzJobEntity.setJobClassName(quartzJobParams.getJobClassName());
            sysQuartzJobEntity.setCronExpression(quartzJobParams.getCronExpression());
            sysQuartzJobEntity.setJsonParam(quartzJobParams.getJsonParam());
            sysQuartzJobEntity.setDescription(quartzJobParams.getDescription());
            sysQuartzJobEntity.setJobGroup("".equals(quartzJobParams.getJobGroup()) ? null : quartzJobParams.getJobGroup());
            sysQuartzJobEntity.setStatus(quartzJobParams.getStatus());
            return Result.SUCCESS(sysQuartzJobService.saveAndScheduleJob(sysQuartzJobEntity));
        } catch (ClassNotFoundException e) {
            return Result.ERROR(ExceptionEnum.SCHEDULER_USE_CLASS_EXCEPTION);
        } catch (RuntimeException e) {
            log.error("创建失败：原因 {}", e.getMessage());
            return Result.ERROR(ExceptionEnum.SCHEDULER_CREATE_EXCEPTION);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
