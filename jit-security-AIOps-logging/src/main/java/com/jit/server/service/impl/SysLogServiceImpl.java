package com.jit.server.service.impl;

import com.jit.server.pojo.SysLogEntity;
import com.jit.server.repository.SysLogRepo;
import com.jit.server.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogRepo sysLogRepo;

    @Override
    public SysLogEntity saveOrUpdateLog(SysLogEntity sysLogEntity) throws Exception {
        return sysLogRepo.saveAndFlush(sysLogEntity);
    }

    @Override
    public String getUserName(String name) throws Exception {
        return sysLogRepo.getUserName(name);
    }

    @Override
    public String getUserId(String name) throws Exception {
        return sysLogRepo.getUserId(name);
    }

    /**
     * 根据传入的信息进行查询日志数据
     * @param logType 日志类型 0:登录日志;1:操作日志;2:错误日志
     * @param logContent 日志内容
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param operationType 操作日志类型 0:未定义;1:添加;2:查询;3:修改;4:删除
     * @param currentPage 当前页
     * @param currentSize 每页的条数
     * @return 统一返回数据对象
     */
    @Override
    public Page<SysLogEntity> findSysLog(int logType, String logContent, LocalDateTime startTime, LocalDateTime endTime, int operationType, int currentPage, int currentSize) {
        //创建安全的日期转换对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = formatter.format(startTime);
        String endTimeStr = formatter.format(endTime);
        //创建多条件查询
        Specification<SysLogEntity> specification = new Specification<SysLogEntity>() {
            @Override
            public Predicate toPredicate(Root<SysLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //创建查询条件的 List 集合对象 进行查询
                List<Predicate> predicates = new ArrayList<>();
                //创建查询条件
                Predicate predicate = criteriaBuilder.equal(root.get("logType").as(Integer.class) , logType);
                //将第一个查询条件加入集合
                predicates.add(predicate);
                //创建查询条件
                predicate = criteriaBuilder.equal(root.get("isDeleted").as(Integer.class) , 0);
                //将第二个查询条件加入集合
                predicates.add(predicate);
                //日志名称 可选参数
                if(logContent != null && !logContent.isEmpty()){
                    //创建查询条件
                    Predicate temp = criteriaBuilder.like(root.get("logContent") , '%' + logContent + '%');
                    //将第二个查询条件加入集合
                    predicates.add(temp);
                }
                //创建时间 开始时间 ~ 结束时间
                if(startTime != null){
                    //创建查询条件
                    Predicate temp = criteriaBuilder.greaterThanOrEqualTo(root.get("gmtCreate").as(String.class) , startTimeStr);
                    //将第三个查询条件加入集合
                    predicates.add(temp);
                }
                if(endTime != null){
                    //创建查询条件
                    Predicate temp = criteriaBuilder.lessThanOrEqualTo(root.get("gmtCreate").as(String.class) , endTimeStr);
                    //将第三个查询条件加入集合
                    predicates.add(temp);
                }
                //操作类型
                if(operationType != 0){
                    //创建查询条件
                    Predicate temp = criteriaBuilder.equal(root.get("operationType") , operationType);
                    //将第四个查询条件加入集合
                    predicates.add(temp);
                }
                //创建一个数组
                Predicate[] listPredicates = new Predicate[predicates.size()];
                //进行多条件查询的数组转换并用 and 条件进行拼接
                return criteriaBuilder.and(predicates.toArray(listPredicates));
            }
        };
        //创建排序条件
        Sort sort = Sort.by(Sort.Direction.DESC , "gmtCreate");
        //创建分页和排序的条件
        Pageable pageable = PageRequest.of(currentPage - 1 , currentSize , sort);
        //查询
        Page<SysLogEntity> sysLogs = sysLogRepo.findAll(specification , pageable);
        //返回
        return sysLogs;
    }
}
