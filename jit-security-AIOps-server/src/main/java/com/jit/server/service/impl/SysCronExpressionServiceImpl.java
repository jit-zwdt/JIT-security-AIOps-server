package com.jit.server.service.impl;

import com.jit.server.dto.CronExpressionDTO;
import com.jit.server.pojo.SysCronExpressionEntity;
import com.jit.server.repository.SysCronExpressionRepo;
import com.jit.server.service.SysCronExpressionService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SysCronExpressionServiceImpl implements SysCronExpressionService {

    @Autowired
    private SysCronExpressionRepo sysCronExpressionRepo;

    @Override
    public Page<SysCronExpressionEntity> getCronExpressions(PageRequest<Map<String, Object>> params) {
        Map<String, Object> param = params.getParam();
        if (param != null && !param.isEmpty()) {
            //条件
            Specification<SysCronExpressionEntity> spec = new Specification<SysCronExpressionEntity>() {
                @Override
                public Predicate toPredicate(Root<SysCronExpressionEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
                    String cronExpressionDesc = param.get("cronExpressionDesc") != null ? param.get("cronExpressionDesc").toString() : "";
                    if (StringUtils.isNotBlank(cronExpressionDesc)) {
                        list.add(cb.like(root.get("cronExpressionDesc").as(String.class), "%" + cronExpressionDesc + "%"));
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
            return sysCronExpressionRepo.findAll(spec, pageable);
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
     * @param cronExpression 时间表达式对象
     * @return 添加成功的时间表达式对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysCronExpressionEntity addCronExpression(SysCronExpressionEntity cronExpression) {

        // 获取的登录人的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //首先判断如果传过来的对象是 null 的值的话则进行赋值
        if(cronExpression.getId() == null || cronExpression.getId().isEmpty()){//创建
            //赋值为 null 防止前端传入的字符串为 ""
            cronExpression.setId(null);
            cronExpression.setCreateBy(username);
            cronExpression.setGmtCreate(LocalDateTime.now());
        }else{//修改
            cronExpression.setUpdateBy(username);
            cronExpression.setGmtModified(LocalDateTime.now());
        }
        //调用添加方法进行添加
        sysCronExpressionRepo.save(cronExpression);
        return cronExpression;
    }

    /**
     * 根据表达式的描述和和表达式查询数据
     * @param cronExpression SysCronExpressionEntity 对象
     * @return 是否具有这个数据 true 有 false 没有
     */
    @Override
    public boolean checkAddCronExpression(String cronExpression){
        // 查看表达式和表达式名称是否具有重复的数据
        SysCronExpressionEntity sysCronExpression = sysCronExpressionRepo.findByCronExpressionAndIsDeleted(cronExpression , 0);
        if(sysCronExpression == null){
            return false;
        }
        return true;
    }

    /**
     * 根据表达式的描述查询数据
     * @param cronExpressionDesc 表达式描述查询数据
     * @return 是否具有这个数据 true 有 false 没有
     */
    @Override
    public boolean checkAddCronExpressionDesc(String cronExpressionDesc) {
        // 查看表达式和表达式名称是否具有重复的数据
        SysCronExpressionEntity sysCronExpression = sysCronExpressionRepo.findByCronExpressionDescAndIsDeleted(cronExpressionDesc , 0);
        if(sysCronExpression == null){
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
        sysCronExpressionRepo.updateIsDeleteByID(1 , id);
    }

    /**
     * 查询所有的时间表达式
     *
     * @return 时间表达式数据
     */
    @Override
    public List<SysCronExpressionEntity> findAllCronExpression() {
        //查询所有的数据并返回
        return sysCronExpressionRepo.findAll();
    }
}
