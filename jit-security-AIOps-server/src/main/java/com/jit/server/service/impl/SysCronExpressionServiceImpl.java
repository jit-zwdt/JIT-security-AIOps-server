package com.jit.server.service.impl;

import com.jit.server.dto.CronExpressionDTO;
import com.jit.server.dto.SysCronExpressionDTO;
import com.jit.server.pojo.SysCronExpressionEntity;
import com.jit.server.repository.SysCronExpressionRepo;
import com.jit.server.service.SysCronExpressionService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Service
public class SysCronExpressionServiceImpl implements SysCronExpressionService {

    @Autowired
    private SysCronExpressionRepo sysCronExpressionRepo;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<SysCronExpressionDTO> getCronExpressions(PageRequest<Map<String, Object>> params) {
        Map<String, Object> param = params.getParam();
        if (param != null && !param.isEmpty()) {
            int size = params.getSize();
            int page = params.getPage() - 1;
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SysCronExpressionDTO> query = cb.createQuery(SysCronExpressionDTO.class);
            Root<SysCronExpressionEntity> root = query.from(SysCronExpressionEntity.class);
            Path<String> id = root.get("id");
            Path<String> cronExpressionDesc = root.get("cronExpressionDesc");
            Path<String> cronExpression = root.get("cronExpression");
            Path<LocalDateTime> gmtCreate = root.get("gmtCreate");
            //查询字段
            query.multiselect(id, cronExpressionDesc, cronExpression);
            //查询条件
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
            String cronExpressiondesc = param.get("cronExpressionDesc") != null ? param.get("cronExpressionDesc").toString() : "";
            if (StringUtils.isNotBlank(cronExpressiondesc)) {
                list.add(cb.like(cronExpressionDesc.as(String.class), "%" + cronExpressiondesc + "%"));
            }
            Predicate[] arr = new Predicate[list.size()];
            arr = list.toArray(arr);
            query.where(arr);
            query.orderBy(cb.asc(gmtCreate));
            TypedQuery<SysCronExpressionDTO> typedQuery = entityManager.createQuery(query);
            int startIndex = size * page;
            typedQuery.setFirstResult(startIndex);
            typedQuery.setMaxResults(params.getSize());
            //总条数
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SysCronExpressionEntity> root1 = countQuery.from(SysCronExpressionEntity.class);
            countQuery.where(arr);
            countQuery.select(cb.count(root1));
            long count = entityManager.createQuery(countQuery).getSingleResult().longValue();
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            Page<SysCronExpressionDTO> res = new PageImpl<>(typedQuery.getResultList(), pageable, count);
            return res;
        }
        return null;
    }

    @Override
    public List<CronExpressionDTO> getCronExpressionObject() throws Exception {
        List<CronExpressionDTO> res;
        List<Object> objectList = sysCronExpressionRepo.getCronExpressionObject();
        if (objectList != null && !objectList.isEmpty()) {
            res = new ArrayList<>(objectList.size());
            Object[] object;
            CronExpressionDTO cronExpressionDTO;
            for (int i = 0, len = objectList.size(); i < len; i++) {
                object = (Object[]) objectList.get(i);
                cronExpressionDTO = new CronExpressionDTO();
                cronExpressionDTO.setCronExpressionDesc(object[0] != null ? object[0].toString() : "");
                cronExpressionDTO.setCronExpression(object[1] != null ? object[1].toString() : "");
                res.add(cronExpressionDTO);
            }
            return res;
        }
        return null;
    }

    /**
     * 添加一个时间表达式对象数据
     *
     * @param cronExpression 时间表达式对象
     * @return 添加成功的时间表达式对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysCronExpressionEntity addCronExpression(SysCronExpressionEntity cronExpression) {

        // 获取的登录人的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //首先判断如果传过来的对象是 null 的值的话则进行赋值
        if (cronExpression.getId() == null || cronExpression.getId().isEmpty()) {//创建
            //赋值为 null 防止前端传入的字符串为 ""
            cronExpression.setId(null);
            cronExpression.setCreateBy(username);
            cronExpression.setGmtCreate(LocalDateTime.now());
        } else {//修改
            cronExpression.setUpdateBy(username);
            cronExpression.setGmtModified(LocalDateTime.now());
        }
        //调用添加方法进行添加
        sysCronExpressionRepo.save(cronExpression);
        return cronExpression;
    }

    /**
     * 根据表达式的描述和和表达式查询数据
     *
     * @param cronExpression SysCronExpressionEntity 对象
     * @return 是否具有这个数据 true 有 false 没有
     */
    @Override
    public boolean checkAddCronExpression(String cronExpression) {
        // 查看表达式和表达式名称是否具有重复的数据
        SysCronExpressionEntity sysCronExpression = sysCronExpressionRepo.findByCronExpressionAndIsDeleted(cronExpression, 0);
        if (sysCronExpression == null) {
            return false;
        }
        return true;
    }

    /**
     * 根据表达式的描述查询数据
     *
     * @param cronExpressionDesc 表达式描述查询数据
     * @return 是否具有这个数据 true 有 false 没有
     */
    @Override
    public boolean checkAddCronExpressionDesc(String cronExpressionDesc) {
        // 查看表达式和表达式名称是否具有重复的数据
        SysCronExpressionEntity sysCronExpression = sysCronExpressionRepo.findByCronExpressionDescAndIsDeleted(cronExpressionDesc, 0);
        if (sysCronExpression == null) {
            return false;
        }
        return true;
    }

    /**
     * 根据时间表达式的 ID 删除一条数据
     *
     * @param id 时间表达式的 id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCronExpression(String id) {
        sysCronExpressionRepo.updateIsDeleteByID(1, id);
    }

    /**
     * 查询所有的时间表达式
     *
     * @return 时间表达式数据
     */
    @Override
    public List<SysCronExpressionDTO> findAllCronExpression() {
        //查询所有的数据并返回
        return sysCronExpressionRepo.findAllCronExpression();
    }
}
