package com.jit.server.service.impl;

import com.jit.server.pojo.HostEntity;
import com.jit.server.repository.HostRepo;
import com.jit.server.request.HostParams;
import com.jit.server.service.HostService;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private HostRepo hostRepo;

    @Override
    public Page<HostEntity> findByCondition(HostParams params, int page, int size) throws Exception {

        if (params!=null){
            //条件
            Specification<HostEntity> spec = new Specification<HostEntity>() {
                @Override
                public Predicate toPredicate(Root<HostEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    /** 可添加你的其他搜索过滤条件 默认已有是否删除过滤 **/
                    /**
                     between : between and
                     like : like
                     equal : （相等）
                     gt : greaterThan（大于）
                     ge : lessThanOrEqualTo（大于或等于）
                     lt : lessThan（小于）
                     le : lessThanOrEqualTo（小于或等于）
                     **/
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.equal(root.get("deleted").as(Boolean.class), false));

                    /** 对象名称 **/
                    if(StringUtils.isNotEmpty(params.getObjectName())){
                        list.add(cb.like(root.get("objectName").as(String.class),"%"+params.getObjectName()+"%"));
                    }

                    /** 业务名称 **/
                    if(StringUtils.isNotEmpty(params.getBusinessName())){
                        list.add(cb.like(root.get("businessName").as(String.class),"%"+params.getBusinessName()+"%"));
                    }

                    /** 备注 **/
                    if(StringUtils.isNotEmpty(params.getRemark())){
                        list.add(cb.like(root.get("remark").as(String.class),"%"+params.getRemark()+"%"));
                    }

                    /** 标签 **/
                    if(StringUtils.isNotEmpty(params.getLabel())){
                        list.add(cb.like(root.get("label").as(String.class),"%"+params.getLabel()+"%"));
                    }

                    /** 资产 **/
                    if(StringUtils.isNotEmpty(params.getAssetsId())){
                        list.add(cb.equal(root.get("assetsId").as(String.class), params.getAssetsId()));
                    }

                    /** 类型 **/
                    if(StringUtils.isNotEmpty(params.getTypeId())){
                        list.add(cb.equal(root.get("typeId").as(String.class), params.getTypeId()));
                    }

                    /** 分组 **/
                    if(StringUtils.isNotEmpty(params.getGroupId())){
                        list.add(cb.equal(root.get("groupId").as(String.class), params.getGroupId()));
                    }

                    /** 是否监控 **/
                    if(StringUtils.isNotEmpty(params.getEnableMonitor())){
                        list.add(cb.equal(root.get("enableMonitor").as(String.class), params.getEnableMonitor()));
                    }

                    Predicate[] arr = new Predicate[list.size()];
                    return cb.and(list.toArray(arr));
                }
            };
            //排序的定义
            List<Order> list = new ArrayList<>();
            Order order1 = new Order(Sort.Direction.DESC, "gmtModified");
            Order order2 = new Order(Sort.Direction.ASC, "id");
            list.add(order1);
            list.add(order2);
            Sort sort = Sort.by(list);
            //Sort sort = Sort.by(Sort.Order.desc("gmtModified"));
            //分页的定义
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            return this.hostRepo.findAll(spec, pageable);

        }
        return null;
    }

    @Override
    public void addHost(HostEntity host) throws Exception {
        hostRepo.save(host);
    }

    @Override
    public void deleteHost(String id) throws Exception {
        hostRepo.deleteById(id);
    }

    @Override
    public Optional<HostEntity> findByHostId(String id) throws Exception {
        return hostRepo.findById(id);
    }

    @Override
    public void updateHost(HostEntity host) throws Exception {
        hostRepo.save(host);
    }
}
