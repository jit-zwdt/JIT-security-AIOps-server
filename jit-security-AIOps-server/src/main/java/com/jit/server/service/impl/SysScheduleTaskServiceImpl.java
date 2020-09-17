package com.jit.server.service.impl;

import com.jit.server.dto.SchedulerDTO;
import com.jit.server.pojo.SysScheduleTaskEntity;
import com.jit.server.repository.SysScheduleTaskRepo;
import com.jit.server.service.SysScheduleTaskService;
import com.jit.server.service.UserService;
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
    public boolean stopScheduleTask(String jobKey) throws Exception {
        return false;
    }

    /**
     * save and start schedule job.
     */
    @Override
    public String saveAndScheduleJob(SysScheduleTaskEntity sysScheduleTaskEntity) throws Exception {
        String id = sysScheduleTaskRepo.saveAndFlush(sysScheduleTaskEntity).getId();
        if (StringUtils.isNotBlank(id)) {
            if (ConstUtil.STATUS_NORMAL == sysScheduleTaskEntity.getStatus()) {
                try {
                    // 定时器添加
                    SchedulerDTO schedulerDTO = new SchedulerDTO();
                    schedulerDTO.setJobClassName(sysScheduleTaskEntity.getJobClassName().trim());
                    schedulerDTO.setCronExpression(sysScheduleTaskEntity.getCronExpression().trim());
                    schedulerDTO.setJobName(ConstUtil.JOB_ + sysScheduleTaskEntity.getId());
                    schedulerDTO.setJobDescription(sysScheduleTaskEntity.getDescription());
                    schedulerDTO.setJobGroup(sysScheduleTaskEntity.getJobGroup());
                    schedulerDTO.setJsonParam(sysScheduleTaskEntity.getJsonParam());
                    schedulerDTO.setTriggerName(ConstUtil.TRIGGER_ + sysScheduleTaskEntity.getId());
                    schedulerDTO.setTriggerGroup(sysScheduleTaskEntity.getId());
                    //this.schedulerAdd(schedulerDTO);
                } catch (Exception e) {
                    sysScheduleTaskRepo.deleteById(id);
                    throw e;
                }
            }
        }
        return id;
    }

    @Override
    public SysScheduleTaskEntity getSysScheduleTaskById(String id) throws Exception {
        return sysScheduleTaskRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
    }

    private static Class getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Class) class1.newInstance();
    }

}
