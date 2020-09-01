package com.jit.server.service.impl;

import com.jit.server.pojo.MonitorAssetsEntity;
import com.jit.server.repository.AssetsRepo;
import com.jit.server.request.AssetsParams;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    private AssetsRepo assetsRepo;

    @Override
    public Page<MonitorAssetsEntity> findByCondition(AssetsParams params, int page, int size) throws Exception {

        if (params != null) {
            //条件
            Specification<MonitorAssetsEntity> spec = new Specification<MonitorAssetsEntity>() {
                @Override
                public Predicate toPredicate(Root<MonitorAssetsEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    /** 可添加你的其他搜索过滤条件 默认已有创建时间过滤 **/
                    Path<LocalDateTime> createTimeField = root.get("assetRegisterDate");
                    Path<Date> endTimeField = root.get("assetLogoutDate");
                    //Path<String> categoryIdField=root.get("categoryId");

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
                    list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
                    /** 资产登记时间 **/
                    if (params.getAssetRegisterStartDate() != null) {
                        list.add(cb.greaterThanOrEqualTo(createTimeField, params.getAssetRegisterStartDate()));
                    }
                    if (params.getAssetRegisterEndDate() != null) {
                        list.add(cb.lessThanOrEqualTo(createTimeField, params.getAssetRegisterEndDate()));
                    }
                    /** 资产名称 **/
                    if (StringUtils.isNotEmpty(params.getAssetName())) {
                        list.add(cb.like(root.get("assetName").as(String.class), "%" + params.getAssetName() + "%"));
                    }

                    /** 资产类型 **/
                    if (StringUtils.isNotEmpty(params.getAssetType())) {
                        list.add(cb.equal(root.get("assetType").as(String.class), params.getAssetType()));
                    }

                    /** 资产所属人 **/
                    if (StringUtils.isNotEmpty(params.getAssetBelongsPerson())) {
                        list.add(cb.like(root.get("assetBelongsPerson").as(String.class), "%" + params.getAssetBelongsPerson() + "%"));
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

    @Override
    public void addAssets(MonitorAssetsEntity assets) throws Exception {
        assetsRepo.save(assets);
    }

    @Override
    public void deleteAssets(String id) throws Exception {
        assetsRepo.deleteById(id);
    }

    @Override
    public Optional<MonitorAssetsEntity> findByAssetsId(String id) throws Exception {
        return assetsRepo.findById(id);
    }

    @Override
    public void updateAssets(MonitorAssetsEntity assets) throws Exception {
        assetsRepo.save(assets);
    }

    @Override
    public List<MonitorAssetsEntity> findByConditionInfo() throws Exception {
        return this.assetsRepo.findAll();
    }
}
