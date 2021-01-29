package com.jit.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.config.CronTaskRegistrar;
import com.jit.server.dto.SysScheduleTaskDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<SysScheduleTaskDTO> getSysScheduleTasks(PageRequest<Map<String, Object>> params) {
        Map<String, Object> param = params.getParam();
        if (param != null) {
            int size = params.getSize();
            int page = params.getPage() - 1;
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SysScheduleTaskDTO> query = cb.createQuery(SysScheduleTaskDTO.class);
            Root<SysScheduleTaskEntity> root = query.from(SysScheduleTaskEntity.class);
            Path<String> id = root.get("id");
            Path<String> jobClassName = root.get("jobClassName");
            Path<String> jobMethodName = root.get("jobMethodName");
            Path<String> cronExpression = root.get("cronExpression");
            Path<String> jsonParam = root.get("jsonParam");
            Path<String> description = root.get("description");
            Path<String> jobGroup = root.get("jobGroup");
            Path<Integer> status = root.get("status");
            Path<LocalDateTime> gmtCreate = root.get("gmtCreate");
            //查询字段
            query.multiselect(id, jobClassName, jobMethodName, cronExpression, jsonParam, description, jobGroup, status);
            //查询条件
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
            String jobClassName2 = param.get("jobClassName") != null ? param.get("jobClassName").toString() : "";
            if (StringUtils.isNotBlank(jobClassName2)) {
                list.add(cb.like(root.get("jobClassName").as(String.class), "%" + jobClassName2 + "%"));
            }
            String status2 = param.get("status") != null ? param.get("status").toString() : "";
            if (StringUtils.isNotBlank(status2)) {
                list.add(cb.equal(root.get("status").as(String.class), status2));
            }
            Predicate[] arr = new Predicate[list.size()];
            arr = list.toArray(arr);
            query.where(arr);
            query.orderBy(cb.asc(gmtCreate));
            TypedQuery<SysScheduleTaskDTO> typedQuery = entityManager.createQuery(query);
            int startIndex = size * page;
            typedQuery.setFirstResult(startIndex);
            typedQuery.setMaxResults(size);
            //总条数
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SysScheduleTaskEntity> root1 = countQuery.from(SysScheduleTaskEntity.class);
            countQuery.where(arr);
            countQuery.select(cb.count(root1));
            long count = entityManager.createQuery(countQuery).getSingleResult().longValue();
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            Page<SysScheduleTaskDTO> res = new PageImpl<>(typedQuery.getResultList(), pageable, count);
            return res;
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
    @Transactional(rollbackFor = Exception.class)
    public String saveAndScheduleJob(SysScheduleTaskEntity sysScheduleTaskEntity) throws Exception {
        String id = sysScheduleTaskRepo.saveAndFlush(sysScheduleTaskEntity).getId();
        if (StringUtils.isNotBlank(id)) {
            if (ConstUtil.STATUS_NORMAL == sysScheduleTaskEntity.getStatus() && ConstUtil.IS_NOT_DELETED == sysScheduleTaskEntity.getIsDeleted()) {
                try {
                    // 定时器添加
                    // 在添加定时器之前把传递的参数添加一个添加定时任务表数据的 ID
                    JSONObject jsonObject = JSONObject.parseObject(sysScheduleTaskEntity.getJsonParam());
                    jsonObject.put("scheduleId", id);
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
    public SysScheduleTaskDTO getSysScheduleTaskById(String id) throws Exception {
        return sysScheduleTaskRepo.findSysScheduleTaskById(id);
    }

    @Override
    public List<SysScheduleTaskDTO> getScheduleTaskList() throws Exception {
        return sysScheduleTaskRepo.findScheduleTasksByIsDeletedAndStatus(ConstUtil.IS_NOT_DELETED, ConstUtil.STATUS_NORMAL);
    }

    @Override
    public void startScheduleTask(String className, String methodName, String cron, String param) throws Exception {
        // 定时器添加
        cronTaskRegistrar.addCronTask(className, methodName, cron, param);
    }

    @Override
    public List<SysScheduleTaskDTO> getSysScheduleTaskByParams(String jobClassName, String jobMethodName, String cronExpression, String param) throws Exception {
        return sysScheduleTaskRepo.getSysScheduleTaskByParams(jobClassName, jobMethodName, cronExpression, param);
    }

    @Override
    public List<SysScheduleTaskDTO> getSysScheduleTaskByParams2(String id, String jobClassName, String jobMethodName, String cronExpression, String param) throws Exception {
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
            sysScheduleTaskEntity = sysScheduleTaskRepo.findByIdAndIsDeleted(scheduleTaskParams.getId(), ConstUtil.IS_NOT_DELETED);
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
            List<SysScheduleTaskDTO> sysScheduleTaskEntityList = this.getSysScheduleTaskByParams(jobClassName, jobMethodName, cronExpression, param);
            if (sysScheduleTaskEntityList != null && !sysScheduleTaskEntityList.isEmpty()) {
                res = true;
            }
        } else {
            List<SysScheduleTaskDTO> sysScheduleTaskEntityList = this.getSysScheduleTaskByParams2(id, jobClassName, jobMethodName, cronExpression, param);
            if (sysScheduleTaskEntityList != null && !sysScheduleTaskEntityList.isEmpty()) {
                res = true;
            }
        }
        return res;
    }

    @Override
    public void delScheduleTask(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            SysScheduleTaskEntity sysScheduleTaskEntity = sysScheduleTaskRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
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
            SysScheduleTaskEntity sysScheduleTaskEntity = sysScheduleTaskRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
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
