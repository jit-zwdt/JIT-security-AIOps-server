package com.jit.server.service.impl;

import com.jit.server.dto.SysLogDTO;
import com.jit.server.pojo.SysLogEntity;
import com.jit.server.repository.SysLogRepo;
import com.jit.server.service.SysLogService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogRepo sysLogRepo;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
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
     *
     * @param log_type       日志类型 0:登录日志;1:操作日志;2:错误日志
     * @param log_content    日志内容
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param operation_type 操作日志类型 0:未定义;1:添加;2:查询;3:修改;4:删除;5:导入;6:导出;7:上传;8:下载
     * @param page           当前页
     * @param size           每页的条数
     * @return 统一返回数据对象
     */
    @Override
    public Page<SysLogDTO> findSysLog(int log_type, String log_content, LocalDateTime startTime, LocalDateTime endTime, Integer operation_type, int page, int size) {
        //创建安全的日期转换对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        page = page - 1;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysLogDTO> query = cb.createQuery(SysLogDTO.class);
        Root<SysLogEntity> root = query.from(SysLogEntity.class);
        Path<String> id = root.get("id");
        Path<Integer> logType = root.get("logType");
        Path<String> logContent = root.get("logContent");
        Path<Integer> operationType = root.get("operationType");
        Path<String> userUsername = root.get("userUsername");
        Path<String> userName = root.get("userName");
        Path<String> ip = root.get("ip");
        Path<String> ipFrom = root.get("ipFrom");
        Path<String> method = root.get("method");
        Path<String> requestUrl = root.get("requestUrl");
        Path<String> requestParam = root.get("requestParam");
        Path<String> requestType = root.get("requestType");
        Path<Long> costTime = root.get("costTime");
        Path<String> errorLog = root.get("errorLog");
        Path<LocalDateTime> gmtCreate = root.get("gmtCreate");
        //查询字段
        query.multiselect(id, logType, logContent, operationType, userUsername, userName, ip, ipFrom, method, requestUrl, requestParam, requestType, costTime, errorLog, gmtCreate);
        //查询条件
        List<Predicate> list = new ArrayList<>();
        list.add(cb.equal(root.get("logType").as(Integer.class), log_type));
        list.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
        if (StringUtils.isNotBlank(log_content)) {
            list.add(cb.like(logContent.as(String.class), '%' + log_content + '%'));
        }
        //创建时间 开始时间 ~ 结束时间
        if (startTime != null) {
            //转换
            String startTimeStr = formatter.format(startTime);
            list.add(cb.greaterThanOrEqualTo(gmtCreate.as(String.class), startTimeStr));
        }
        if (endTime != null) {
            //转换
            String endTimeStr = formatter.format(endTime);
            //创建查询条件
            list.add(cb.lessThanOrEqualTo(gmtCreate.as(String.class), endTimeStr));
        }
        //操作类型
        if (operation_type != null) {
            //创建查询条件
            list.add(cb.equal(operationType.as(String.class), operation_type));
        }
        Predicate[] arr = new Predicate[list.size()];
        arr = list.toArray(arr);
        query.where(arr);
        query.orderBy(cb.asc(gmtCreate));
        TypedQuery<SysLogDTO> typedQuery = entityManager.createQuery(query);
        int startIndex = size * page;
        typedQuery.setFirstResult(startIndex);
        typedQuery.setMaxResults(size);
        //总条数
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<SysLogEntity> root1 = countQuery.from(SysLogEntity.class);
        countQuery.where(arr);
        countQuery.select(cb.count(root1));
        long count = entityManager.createQuery(countQuery).getSingleResult().longValue();
        //分页的定义
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Page<SysLogDTO> res = new PageImpl<>(typedQuery.getResultList(), pageable, count);
        return res;
    }
}
