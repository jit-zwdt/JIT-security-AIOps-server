package com.jit.server.service.impl;

import com.jit.server.config.CronTaskRegistrar;
import com.jit.server.pojo.SysScheduleTaskEntity;
import com.jit.server.repository.SysScheduleTaskRepo;
import com.jit.server.service.SysScheduleTaskService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: SysQuartzJobServiceImpl
 * @Author: zengxin_miao
 * @Date: 2020.09.14
 */
@Service
public class SysScheduleTaskServiceImpl implements SysScheduleTaskService {

    @Autowired
    private SysScheduleTaskRepo sysScheduleTaskRepo;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Override
    public Page<SysScheduleTaskEntity> getSysScheduleTasks(PageRequest<Map<String, Object>> params) {
        Map<String, Object> param = params.getParam();
        if (param != null && !param.isEmpty()) {
            //条件
            Specification<SysScheduleTaskEntity> spec = new Specification<SysScheduleTaskEntity>() {
                @Override
                public Predicate toPredicate(Root<SysScheduleTaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
                    cronTaskRegistrar.addCronTask(sysScheduleTaskEntity.getJobClassName(), sysScheduleTaskEntity.getJobMethodName(), sysScheduleTaskEntity.getCronExpression(), sysScheduleTaskEntity.getJsonParam());
                } catch (Exception e) {
                    sysScheduleTaskRepo.deleteById(id);
                    throw e;
                }
            }
            if (ConstUtil.STATUS_STOP == sysScheduleTaskEntity.getStatus() && ConstUtil.IS_NOT_DELETED == sysScheduleTaskEntity.getIsDeleted()) {
                String key = sysScheduleTaskEntity.getJobClassName() + "." + sysScheduleTaskEntity.getJobMethodName() + "(" + sysScheduleTaskEntity.getCronExpression() + ")";
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
    public List<SysScheduleTaskEntity> getSysScheduleTaskByParams(String jobClassName, String jobMethodName, String cronExpression) throws Exception {
        return sysScheduleTaskRepo.getSysScheduleTaskByParams(jobClassName, jobMethodName, cronExpression);
    }

    @Override
    public List<SysScheduleTaskEntity> getSysScheduleTaskByParams2(String id, String jobClassName, String jobMethodName, String cronExpression) throws Exception {
        return sysScheduleTaskRepo.getSysScheduleTaskByParams2(id, jobClassName, jobMethodName, cronExpression);
    }

}
