package com.jit.server.service.impl;


import com.jit.server.dto.MonitorTemplatesDTO;
import com.jit.server.pojo.MonitorTemplatesEntity;
import com.jit.server.repository.MonitorTemplatesRepo;
import com.jit.server.request.MonitorTemplatesParams;
import com.jit.server.service.MonitorTemplatesService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonitorTemplatesImpl implements MonitorTemplatesService {

    @Autowired
    MonitorTemplatesRepo monitorTemplatesRepo;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<MonitorTemplatesEntity> getMonitorTemplates() throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "type");
        return monitorTemplatesRepo.findAll(sort);
    }

    @Override
    public Page<MonitorTemplatesDTO> getMonitorTemplates(PageRequest<MonitorTemplatesParams> params) throws Exception {
        MonitorTemplatesParams param = params.getParam();
        if (param != null) {
            int size = params.getSize();
            int page = params.getPage() - 1;
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MonitorTemplatesDTO> query = cb.createQuery(MonitorTemplatesDTO.class);
            Root<MonitorTemplatesEntity> root = query.from(MonitorTemplatesEntity.class);
            Path<String> id = root.get("id");
            Path<String> name = root.get("name");
            Path<String> typeId = root.get("typeId");
            Path<String> subtypeIds = root.get("subtypeIds");
            Path<String> templateIds = root.get("templateIds");
            Path<String> templates = root.get("templates");
            Path<String> helpDoc = root.get("helpDoc");
            Path<String> tempKey = root.get("tempKey");
            Path<Integer> orderNum = root.get("orderNum");
            Path<String> ico = root.get("ico");
            Path<LocalDateTime> gmtCreate = root.get("gmtCreate");
            //查询字段
            query.multiselect(id, name, typeId, subtypeIds, templateIds, templates, helpDoc, tempKey, orderNum, ico,gmtCreate);
            //查询条件
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
            if (StringUtils.isNotBlank(param.getName())) {
                list.add(cb.like(name.as(String.class), "%" + param.getName() + "%"));
            }
            if (StringUtils.isNotBlank(param.getType())) {
                list.add(cb.equal(typeId.as(String.class), param.getType()));
            }
            Predicate[] arr = new Predicate[list.size()];
            arr = list.toArray(arr);
            query.where(arr);
            query.orderBy(cb.asc(typeId));
            TypedQuery<MonitorTemplatesDTO> typedQuery = entityManager.createQuery(query);
            int startIndex = size * page;
            typedQuery.setFirstResult(startIndex);
            typedQuery.setMaxResults(params.getSize());
            //总条数
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<MonitorTemplatesEntity> root1 = countQuery.from(MonitorTemplatesEntity.class);
            countQuery.where(arr);
            countQuery.select(cb.count(root1));
            long count = entityManager.createQuery(countQuery).getSingleResult().longValue();
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            Page<MonitorTemplatesDTO> res = new PageImpl<>(typedQuery.getResultList(), pageable, count);
            return res;
        }
        return null;
    }

    @Override
    public MonitorTemplatesEntity getMonitorTemplate(String id) throws Exception {
        return monitorTemplatesRepo.getOne(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
