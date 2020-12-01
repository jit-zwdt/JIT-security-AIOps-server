package com.jit.server.service.impl;


import com.jit.server.pojo.MonitorTemplatesEntity;
import com.jit.server.repository.MonitorTemplatesRepo;
import com.jit.server.request.MonitorTemplatesParams;
import com.jit.server.service.MonitorTemplatesService;
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

@Service
public class MonitorTemplatesImpl implements MonitorTemplatesService {

    @Autowired
    MonitorTemplatesRepo monitorTemplatesRepo;

    @Override
    public List<MonitorTemplatesEntity> getMonitorTemplates() throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "type");
        return monitorTemplatesRepo.findAll(sort);
    }

    @Override
    public Page<MonitorTemplatesEntity> getMonitorTemplates(PageRequest<MonitorTemplatesParams> params) throws Exception {
        MonitorTemplatesParams param = params.getParam();
        if (param != null) {
            //条件
            Specification<MonitorTemplatesEntity> spec = new Specification<MonitorTemplatesEntity>() {
                @Override
                public Predicate toPredicate(Root<MonitorTemplatesEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
                    if (StringUtils.isNotBlank(param.getName())) {
                        list.add(cb.like(root.get("name").as(String.class), "%"+param.getName()+"%"));
                    }
                    if (StringUtils.isNotBlank(param.getType())) {
                        list.add(cb.equal(root.get("typeId").as(String.class), param.getType()));
                    }
                    Predicate[] arr = new Predicate[list.size()];
                    return cb.and(list.toArray(arr));
                }
            };
            //排序的定义
            List<Map<String, String>> orders = params.getOrders();
            List<Sort.Order> orderList = new ArrayList<>();
            if (orders != null && !orders.isEmpty()) {
                Sort.Direction direction = Sort.Direction.ASC;
                for (Map<String, String> order : orders) {
                    if (StringUtils.isNotBlank(order.get("property"))) {
                        String d = order.get("direction");
                        if (StringUtils.isNotBlank(d)) {
                            if (Sort.Direction.DESC.name().equals(d.toUpperCase())) {
                                direction = Sort.Direction.DESC;
                            }
                        }
                        Sort.Order o = new Sort.Order(direction, order.get("property"));
                        orderList.add(o);
                    }
                }
            } else {
                orderList.add(new Sort.Order(Sort.Direction.ASC, "typeId"));
            }
            Sort sort = Sort.by(orderList);
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(params.getPage() - 1, params.getSize(), sort);
            return this.monitorTemplatesRepo.findAll(spec, pageable);
        }
        return null;
    }

    @Override
    public MonitorTemplatesEntity getMonitorTemplate(String id) throws Exception {
        return monitorTemplatesRepo.getOne(id);
    }

    @Override
    public void updateMonitorTemplate(MonitorTemplatesEntity monitorTemplatesEntity) throws Exception {
        monitorTemplatesRepo.saveAndFlush(monitorTemplatesEntity);
    }

    @Override
    public List<MonitorTemplatesEntity> getMonitorTemplatesByTypeId(String typdId) throws Exception {
        return monitorTemplatesRepo.findByTypeIdAndIsDeletedOrderByOrderNum(typdId, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public List<MonitorTemplatesEntity> getMonitorTemplatesByTypeIdAndNameLike(String typeId, String keyword) throws Exception {
        return monitorTemplatesRepo.findByTypeIdAndIsDeletedAndNameLike(typeId, ConstUtil.IS_NOT_DELETED, "%" + keyword + "%");
    }
}
