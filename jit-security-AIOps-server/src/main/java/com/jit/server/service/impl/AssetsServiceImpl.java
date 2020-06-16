package com.jit.server.service.impl;

import com.jit.server.pojo.AssetsEntity;
import com.jit.server.repository.AssetsRepo;
import com.jit.server.service.AssetsService;
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
import java.util.*;

@Service
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    private AssetsRepo assetsRepo;

    @Override
    public Page<AssetsEntity> findByCondition(AssetsEntity params, int page, int size) throws Exception {

        if (params!=null){
            //条件
            Specification<AssetsEntity> spec = new Specification<AssetsEntity>() {
                @Override
                public Predicate toPredicate(Root<AssetsEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    /** 可添加你的其他搜索过滤条件 默认已有创建时间过滤 **/
                    Path<Date> createTimeField=root.get("assetRegisterDate");
                    Path<Date> endTimeField=root.get("assetLogoutDate");
                    //Path<String> categoryIdField=root.get("categoryId");

                    /**
                     between : between and
                     like : like
                     equal : （相等）
                     gt : greater than（大于）
                     ge : lessThanOrEqualTo（大于或等于）
                     lt : less than（小于）
                     le : lessThanOrEqualTo（小于或等于）
                     **/
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.like(root.get("isDeleted").as(String.class), "0"));
                    /** 资产登记时间 **/
                    //if(StringUtils.isNotEmpty((String)params.get("gmtRegister"))&&StringUtils.isNotEmpty((String)params.get("gmtRegister"))){
                    if(params.getAssetRegisterDate()!=null){
                        //Date start = DateUtils.parseDate(params.get("gmtRegister").toString());
                        list.add(cb.greaterThanOrEqualTo(createTimeField, params.getAssetRegisterDate()));
                    }
                    /** 注销时间 **/
                    //if(StringUtils.isNotEmpty((String)params.get("gmtCancellation"))&&StringUtils.isNotEmpty((String)params.get("gmtCancellation"))){
                    if(params.getAssetLogoutDate()!=null){
                        //Date end = DateUtils.parseDate(params.get("gmtCancellation").toString());
                        list.add(cb.lessThanOrEqualTo(endTimeField, params.getAssetLogoutDate()));
                    }
                    /** 资产名称 **/
                    if(StringUtils.isNotEmpty(params.getAssetName())){
                        list.add(cb.like(root.get("assetName").as(String.class), params.getAssetName()));
                    }

                    /** 资产类型 **/
                    if(StringUtils.isNotEmpty(params.getAssetType())){
                        list.add(cb.equal(root.get("assetType").as(String.class), params.getAssetType()));
                    }

                    /** 资产所属人 **/
                    if(StringUtils.isNotEmpty(params.getAssetBelongsPerson())){
                        list.add(cb.like(root.get("assetBelongsPerson").as(String.class), params.getAssetBelongsPerson()));
                    }

                    Predicate[] arr = new Predicate[list.size()];
                    return cb.and(list.toArray(arr));
                }
            };
            //排序的定义
            List<Sort.Order> list = new ArrayList<>();
            Order order1 = new Order(Sort.Direction.DESC, "gmtModified");
            Order order2 = new Order(Sort.Direction.ASC, "id");
            list.add(order1);
            list.add(order2);
            Sort sort = Sort.by(list);
            //Sort sort = Sort.by(Sort.Order.desc("gmtModified"));
            //分页的定义
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            return this.assetsRepo.findAll(spec, pageable);

        }
        return null;
    }
}
