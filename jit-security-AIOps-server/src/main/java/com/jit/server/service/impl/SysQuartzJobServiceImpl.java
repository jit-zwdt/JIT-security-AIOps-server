package com.jit.server.service.impl;

import com.jit.server.dto.SchedulerDTO;
import com.jit.server.pojo.SysQuartzJobEntity;
import com.jit.server.repository.SysQuartzJobRepo;
import com.jit.server.service.SysQuartzJobService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: SysQuartzJobServiceImpl
 * @Author: zengxin_miao
 * @Date: 2020.09.14
 */
@Service
public class SysQuartzJobServiceImpl implements SysQuartzJobService {

    @Autowired
    private SysQuartzJobRepo sysQuartzJobRepo;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private UserService userService;

    @Override
    public Page<SysQuartzJobEntity> getQuartzJobs(PageRequest<Map<String, Object>> params) {
        Map<String, Object> param = params.getParam();
        if (param != null && !param.isEmpty()) {
            //条件
            Specification<SysQuartzJobEntity> spec = new Specification<SysQuartzJobEntity>() {
                @Override
                public Predicate toPredicate(Root<SysQuartzJobEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
                    String jobClassName = param.get("jobClassName") != null ? param.get("jobClassName").toString() : "";
                    if (StringUtils.isNotBlank(jobClassName)) {
                        list.add(cb.like(root.get("jobClassName").as(String.class), "%" + jobClassName + "%"));
                    }
                    String status = param.get("status") != null ? param.get("status").toString() : "";
                    if (StringUtils.isNotBlank(status)) {
                        list.add(cb.equal(root.get("status").as(String.class), status));
                    }
                    Predicate[] arr = new Predicate[list.size()];
                    return cb.and(list.toArray(arr));
                }
            };
            //排序的定义
            List<Sort.Order> orderList = new ArrayList<>();
            orderList.add(new Sort.Order(Sort.Direction.ASC, "gmtCreate"));
            Sort sort = Sort.by(orderList);
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(params.getPage() - 1, params.getSize(), sort);
            return sysQuartzJobRepo.findAll(spec, pageable);
        }
        return null;
    }

    /**
     * save and start schedule job.
     */
    @Override
    public String saveAndScheduleJob(SysQuartzJobEntity sysQuartzJobEntity) throws Exception {
        String id = sysQuartzJobRepo.saveAndFlush(sysQuartzJobEntity).getId();
        if (StringUtils.isNotBlank(id)) {
            if (ConstUtil.STATUS_NORMAL == sysQuartzJobEntity.getStatus()) {
                try {
                    // 定时器添加
                    SchedulerDTO schedulerDTO = new SchedulerDTO();
                    schedulerDTO.setJobClassName(sysQuartzJobEntity.getJobClassName().trim());
                    schedulerDTO.setCronExpression(sysQuartzJobEntity.getCronExpression().trim());
                    schedulerDTO.setJobName(ConstUtil.JOB_ + sysQuartzJobEntity.getId());
                    schedulerDTO.setJobDescription(sysQuartzJobEntity.getDescription());
                    schedulerDTO.setJobGroup(sysQuartzJobEntity.getJobGroup());
                    schedulerDTO.setJsonParam(sysQuartzJobEntity.getJsonParam());
                    schedulerDTO.setTriggerName(ConstUtil.TRIGGER_ + sysQuartzJobEntity.getId());
                    this.schedulerAdd(schedulerDTO);
                } catch (Exception e) {
                    sysQuartzJobRepo.deleteById(id);
                    throw e;
                }
            }
        }
        return id;
    }

    @Override
    public SysQuartzJobEntity getSysQuartzJobEntityById(String id) throws Exception {
        return sysQuartzJobRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
    }


    /**
     * add scheduler
     *
     * @param schedulerDTO
     */
    private void schedulerAdd(SchedulerDTO schedulerDTO) throws Exception {
        // start scheduler
        scheduler.start();

        // build job info
        JobDetail jobDetail = JobBuilder.newJob(getClass(schedulerDTO.getJobClassName()).getClass()).withIdentity(schedulerDTO.getJobName(), schedulerDTO.getJobGroup())
                .usingJobData("jsonParam", schedulerDTO.getJsonParam()).withDescription(schedulerDTO.getJobDescription()).build();

        // Expression schedule builder (that is, the time the task executes)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulerDTO.getCronExpression());

        // Build a new trigger based on the new cronExpression expression
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(schedulerDTO.getTriggerName(), schedulerDTO.getTriggerGroup()).withDescription(schedulerDTO.getTriggerDescription())
                .startNow().withSchedule(scheduleBuilder).build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    private static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job) class1.newInstance();
    }
}
