package com.jit.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.config.CronTaskRegistrar;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.exception.SchedulerExistedException;
import com.jit.server.pojo.SysScheduleTaskEntity;
import com.jit.server.repository.SysScheduleTaskRepo;
import com.jit.server.request.ScheduleTaskParams;
import com.jit.server.service.SysScheduleTaskService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: SysQuartzJobServiceImpl
 * @Author: zengxin_miao
 * @Date: 2020.09.14
 */
@Service
@Slf4j
public class SysScheduleTaskServiceImpl implements SysScheduleTaskService {

    @Autowired
    private SysScheduleTaskRepo sysScheduleTaskRepo;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Autowired
    private UserService userService;

    @Override
    public Page<SysScheduleTaskEntity> getSysScheduleTasks(PageRequest<Map<String, Object>> params) {
        Map<String, Object> param = params.getParam();
        if (param != null && !param.isEmpty()) {
            //条件
            Specification<SysScheduleTaskEntity> spec = new Specification<SysScheduleTaskEntity>() {
                @Override
                public Predicate toPredicate(Root<SysScheduleTaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
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
            return sysScheduleTaskRepo.findAll(spec, pageable);
        }
        return null;
    }

    @Override
    public boolean stopScheduleTask(String jobKey) {
        return cronTaskRegistrar.removeCronTask(jobKey);
    }

    /**
     * save and start schedule job.
     */
    @Override
    public String saveAndScheduleJob(SysScheduleTaskEntity sysScheduleTaskEntity) throws Exception {
        String id = sysScheduleTaskRepo.saveAndFlush(sysScheduleTaskEntity).getId();
        if (StringUtils.isNotBlank(id)) {
            if (ConstUtil.STATUS_NORMAL == sysScheduleTaskEntity.getStatus() && ConstUtil.IS_NOT_DELETED == sysScheduleTaskEntity.getIsDeleted()) {
                try {
                    // 定时器添加
                    // 在添加定时器之前把传递的参数添加一个添加定时任务表数据的 ID
                    JSONObject jsonObject = JSONObject.parseObject(sysScheduleTaskEntity.getJsonParam());
                    jsonObject.put("scheduleId" , id);
                    sysScheduleTaskEntity.setJsonParam(jsonObject.toJSONString());
                    // 添加定时任务
                    cronTaskRegistrar.addCronTask(sysScheduleTaskEntity.getJobClassName(), sysScheduleTaskEntity.getJobMethodName(), sysScheduleTaskEntity.getCronExpression(), sysScheduleTaskEntity.getJsonParam());
                } catch (Exception e) {
                    sysScheduleTaskRepo.deleteById(id);
                    throw e;
                }
            }
            if (ConstUtil.STATUS_STOP == sysScheduleTaskEntity.getStatus() && ConstUtil.IS_NOT_DELETED == sysScheduleTaskEntity.getIsDeleted()) {
                String key = sysScheduleTaskEntity.getJobClassName() + "." + sysScheduleTaskEntity.getJobMethodName() + "(" + sysScheduleTaskEntity.getCronExpression() + "){" + sysScheduleTaskEntity.getJsonParam() + "}";
                cronTaskRegistrar.removeCronTask(key);
            }
        }
        return id;
    }

    @Override
    public SysScheduleTaskEntity getSysScheduleTaskById(String id) throws Exception {
        return sysScheduleTaskRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public List<SysScheduleTaskEntity> getScheduleTaskList() throws Exception {
        return sysScheduleTaskRepo.findByIsDeletedAndStatusOrderByGmtCreate(ConstUtil.IS_NOT_DELETED, ConstUtil.STATUS_NORMAL);
    }

    @Override
    public void startScheduleTask(String className, String methodName, String cron, String param) throws Exception {
        // 定时器添加
        cronTaskRegistrar.addCronTask(className, methodName, cron, param);
    }

    @Override
    public List<SysScheduleTaskEntity> getSysScheduleTaskByParams(String jobClassName, String jobMethodName, String cronExpression, String param) throws Exception {
        return sysScheduleTaskRepo.getSysScheduleTaskByParams(jobClassName, jobMethodName, cronExpression, param);
    }

    @Override
    public List<SysScheduleTaskEntity> getSysScheduleTaskByParams2(String id, String jobClassName, String jobMethodName, String cronExpression, String param) throws Exception {
        return sysScheduleTaskRepo.getSysScheduleTaskByParams2(id, jobClassName, jobMethodName, cronExpression, param);
    }

    @Override
    public String addScheduleTask(ScheduleTaskParams scheduleTaskParams) throws Exception {
        String id = scheduleTaskParams.getId();
        String jobClassName = scheduleTaskParams.getJobClassName();
        String jobMethodName = scheduleTaskParams.getJobMethodName();
        String cronExpression = scheduleTaskParams.getCronExpression();
        String param = scheduleTaskParams.getJsonParam();
        if (vaildateParams(id, jobClassName, jobMethodName, cronExpression, param)) {
            throw new SchedulerExistedException(String.valueOf(ExceptionEnum.SCHEDULER_EXISTED_EXCEPTION));
        }
        SysScheduleTaskEntity sysScheduleTaskEntity;
        if (StringUtils.isBlank(scheduleTaskParams.getId())) {
            sysScheduleTaskEntity = new SysScheduleTaskEntity();
            sysScheduleTaskEntity.setGmtCreate(LocalDateTime.now());
            sysScheduleTaskEntity.setCreateBy(userService.findIdByUsername());
            sysScheduleTaskEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
        } else {
            sysScheduleTaskEntity = this.getSysScheduleTaskById(scheduleTaskParams.getId());
            sysScheduleTaskEntity.setGmtModified(LocalDateTime.now());
            sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
        }
        sysScheduleTaskEntity.setJobClassName(jobClassName);
        sysScheduleTaskEntity.setJobMethodName(jobMethodName);
        sysScheduleTaskEntity.setCronExpression(cronExpression);
        sysScheduleTaskEntity.setJsonParam(scheduleTaskParams.getJsonParam());
        sysScheduleTaskEntity.setDescription(scheduleTaskParams.getDescription());
        sysScheduleTaskEntity.setJobGroup("".equals(scheduleTaskParams.getJobGroup()) ? null : scheduleTaskParams.getJobGroup());
        sysScheduleTaskEntity.setStatus(scheduleTaskParams.getStatus());

        return this.saveAndScheduleJob(sysScheduleTaskEntity);
    }

    /**
     * check param jobClassName, jobMethodName, cronExpression, param
     *
     * @param id
     * @param jobClassName
     * @param jobMethodName
     * @param cronExpression
     * @return
     * @throws Exception
     */
    private boolean vaildateParams(String id, String jobClassName, String jobMethodName, String cronExpression, String param) throws Exception {
        boolean res = false;
        if (StringUtils.isBlank(id)) {
            List<SysScheduleTaskEntity> sysScheduleTaskEntityList = this.getSysScheduleTaskByParams(jobClassName, jobMethodName, cronExpression, param);
            if (sysScheduleTaskEntityList != null && !sysScheduleTaskEntityList.isEmpty()) {
                res = true;
            }
        } else {
            List<SysScheduleTaskEntity> sysScheduleTaskEntityList = this.getSysScheduleTaskByParams2(id, jobClassName, jobMethodName, cronExpression, param);
            if (sysScheduleTaskEntityList != null && !sysScheduleTaskEntityList.isEmpty()) {
                res = true;
            }
        }
        return res;
    }

    @Override
    public void delScheduleTask(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            SysScheduleTaskEntity sysScheduleTaskEntity = this.getSysScheduleTaskById(id);
            if (sysScheduleTaskEntity == null) {
                throw new Exception(String.valueOf(ExceptionEnum.QUERY_DATA_EXCEPTION));
            } else {
                String key = sysScheduleTaskEntity.getJobClassName() + "." + sysScheduleTaskEntity.getJobMethodName() + "(" + sysScheduleTaskEntity.getCronExpression() + "){" + sysScheduleTaskEntity.getJsonParam() + "}";
                boolean res = this.stopScheduleTask(key);
                log.info("删除任务{}，结果{}", key, res);
                if (res || ConstUtil.STATUS_STOP == sysScheduleTaskEntity.getStatus()) {
                    sysScheduleTaskEntity.setIsDeleted(ConstUtil.IS_DELETED);
                    sysScheduleTaskEntity.setGmtModified(LocalDateTime.now());
                    sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
                    this.saveAndScheduleJob(sysScheduleTaskEntity);
                } else {
                    throw new Exception(String.valueOf(ExceptionEnum.QUERY_DATA_EXCEPTION));
                }
            }
        } else {
            throw new Exception(String.valueOf(ExceptionEnum.PARAMS_NULL_EXCEPTION));
        }
    }

    @Override
    public void changeStatus(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            SysScheduleTaskEntity sysScheduleTaskEntity = this.getSysScheduleTaskById(id);
            if (sysScheduleTaskEntity == null) {
                throw new Exception(String.valueOf(ExceptionEnum.QUERY_DATA_EXCEPTION));
            } else {
                if (ConstUtil.STATUS_NORMAL == sysScheduleTaskEntity.getStatus()) {
                    String key = sysScheduleTaskEntity.getJobClassName() + "." + sysScheduleTaskEntity.getJobMethodName() + "(" + sysScheduleTaskEntity.getCronExpression() + "){" + sysScheduleTaskEntity.getJsonParam() + "}";
                    boolean res = this.stopScheduleTask(key);
                    log.info("删除任务{}，结果{}", key, res);
                    if (res) {
                        sysScheduleTaskEntity.setStatus(ConstUtil.STATUS_STOP);
                        sysScheduleTaskEntity.setGmtModified(LocalDateTime.now());
                        sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
                        this.saveAndScheduleJob(sysScheduleTaskEntity);
                    } else {
                        throw new Exception(String.valueOf(ExceptionEnum.QUERY_DATA_EXCEPTION));
                    }
                } else {
                    this.startScheduleTask(sysScheduleTaskEntity.getJobClassName(), sysScheduleTaskEntity.getJobMethodName(), sysScheduleTaskEntity.getCronExpression(), sysScheduleTaskEntity.getJsonParam());
                    sysScheduleTaskEntity.setStatus(ConstUtil.STATUS_NORMAL);
                    sysScheduleTaskEntity.setGmtModified(LocalDateTime.now());
                    sysScheduleTaskEntity.setUpdateBy(userService.findIdByUsername());
                    this.saveAndScheduleJob(sysScheduleTaskEntity);
                }
            }
        } else {
            throw new Exception(String.valueOf(ExceptionEnum.PARAMS_NULL_EXCEPTION));
        }
    }
}
