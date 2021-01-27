package com.jit.server.service.impl;

import com.jit.server.dto.MonitorAssetsDTO;
import com.jit.server.pojo.MonitorAssetsEntity;
import com.jit.server.repository.AssetsRepo;
import com.jit.server.request.AssetsParams;
import com.jit.server.service.AssetsService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    private AssetsRepo assetsRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<MonitorAssetsDTO> findByCondition(AssetsParams params, int page, int size) throws Exception {
        if (params != null) {
            page = page - 1;
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MonitorAssetsDTO> query = cb.createQuery(MonitorAssetsDTO.class);
            Root<MonitorAssetsEntity> root = query.from(MonitorAssetsEntity.class);
            Path<String> id = root.get("id");
            Path<String> name = root.get("name");
            Path<String> number = root.get("number");
            Path<String> type = root.get("type");
            Path<String> state = root.get("state");
            Path<String> classify = root.get("classify");
            Path<String> gbType = root.get("gbType");
            Path<String> ip = root.get("ip");
            Path<String> backupIp = root.get("backupIp");
            Path<Integer> amount = root.get("amount");
            Path<String> belongsDept = root.get("belongsDept");
            Path<String> belongsPerson = root.get("belongsPerson");
            Path<LocalDate> registerDate = root.get("registerDate");
            Path<String> registrant = root.get("registrant");
            Path<LocalDate> updateDate = root.get("updateDate");
            Path<String> location = root.get("location");
            Path<LocalDate> logoutDate = root.get("logoutDate");
            Path<LocalDate> dateRecorded = root.get("dateRecorded");
            Path<String> worth = root.get("worth");
            Path<String> acquisitionMode = root.get("acquisitionMode");
            Path<String> userDepartment = root.get("userDepartment");
            Path<String> user = root.get("user");
            Path<String> objectClassification = root.get("objectClassification");
            Path<String> sn = root.get("sn");
            Path<String> brand = root.get("brand");
            Path<String> productModel = root.get("productModel");
            Path<String> parentId = root.get("parentId");
            Path<Float> cpu = root.get("cpu");
            Path<Integer> cpuCoreNumber = root.get("cpuCoreNumber");
            Path<Integer> memory = root.get("memory");
            Path<Integer> hardDisk = root.get("hardDisk");
            Path<LocalDateTime> gmtCreate = root.get("gmtCreate");
            //查询字段
            query.multiselect(id, name, number, type, state, classify, gbType, ip, backupIp, amount, belongsDept, belongsPerson, registerDate, registrant, updateDate, location, logoutDate, dateRecorded, worth, acquisitionMode, userDepartment, user, objectClassification, sn, brand, productModel, parentId, cpu, cpuCoreNumber, memory, hardDisk);
            //查询条件
            List<Predicate> list = new ArrayList<>();
            list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
            /** 资产登记时间 **/
            if (params.getRegisterStartDate() != null) {
                list.add(cb.greaterThanOrEqualTo(registerDate, params.getRegisterStartDate()));
            }
            if (params.getRegisterEndDate() != null) {
                list.add(cb.lessThanOrEqualTo(registerDate, params.getRegisterEndDate()));
            }
            /** 资产名称 **/
            if (StringUtils.isNotEmpty(params.getName())) {
                list.add(cb.like(name.as(String.class), "%" + params.getName() + "%"));
            }

            /** 资产类型 **/
            if (StringUtils.isNotEmpty(params.getType())) {
                list.add(cb.equal(type.as(String.class), params.getType()));
            }

            /** 资产所属人 **/
            if (StringUtils.isNotEmpty(params.getBelongsPerson())) {
                list.add(cb.like(belongsPerson.as(String.class), "%" + params.getBelongsPerson() + "%"));
            }
            Predicate[] arr = new Predicate[list.size()];
            arr = list.toArray(arr);
            query.where(arr);
            List<Order> orders = new ArrayList<>();
            orders.add(cb.asc(ip));
            orders.add(cb.asc(gmtCreate));
            query.orderBy(orders);
            TypedQuery<MonitorAssetsDTO> typedQuery = entityManager.createQuery(query);
            int startIndex = size * page;
            typedQuery.setFirstResult(startIndex);
            typedQuery.setMaxResults(size);
            //总条数
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<MonitorAssetsEntity> root1 = countQuery.from(MonitorAssetsEntity.class);
            countQuery.where(arr);
            countQuery.select(cb.count(root1));
            long count = entityManager.createQuery(countQuery).getSingleResult().longValue();
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            Page<MonitorAssetsDTO> res = new PageImpl<>(typedQuery.getResultList(), pageable, count);
            return res;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAssets(MonitorAssetsEntity assets) throws Exception {
        assetsRepo.save(assets);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssets(String id) throws Exception {
        assetsRepo.deleteById(id);
    }

    @Override
    public Optional<MonitorAssetsEntity> findByAssetsId(String id) throws Exception {
        return assetsRepo.findById(id);
    }

    @Override
    public MonitorAssetsDTO findMonitorAssetByAssetsId(String id) throws Exception {
        return assetsRepo.findMonitorAssetByAssetsId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAssets(MonitorAssetsEntity assets) throws Exception {
        //更新数据
        assetsRepo.saveAndFlush(assets);
    }

    @Override
    public List<Object> findByConditionInfo() throws Exception {
        return this.assetsRepo.findByConditionInfo();
    }

    @Override
    public List<Object> getHardwareInfo() throws Exception {
        return assetsRepo.findHardwareInfo();
    }

    /**
     * 根据查询语句查询条数和总 CPU 大小 , 内存大小 , 硬件大小
     *
     * @return 数据数组
     */
    @Override
    public List<Object[]> getCountAndSum() {
        return assetsRepo.getCountAndSum();
    }

    /**
     * 根据传入的 ip 验证 Ip 值
     *
     * @param ip ip 值
     * @return true 代表有这个数据 false 代表没有这个数据
     */
    @Override
    public boolean validateIp(String ip) {
        //根据 Ip 查询数据
        List<MonitorAssetsEntity> monitorAssets = assetsRepo.findByIpAndIsDeleted(ip, ConstUtil.IS_NOT_DELETED);
        //判断查询的数据是否大于 0
        if (monitorAssets.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据传入的 number 验证 number 值 number: 资产编号
     *
     * @param number ip 值
     * @return true 代表有这个数据 false 代表没有这个数据
     */
    @Override
    public boolean validateNumber(String number) {
        //根据 Number 查询数据
        List<MonitorAssetsEntity> monitorAssets = assetsRepo.findByNumberAndIsDeleted(number, ConstUtil.IS_NOT_DELETED);
        //判断查询的数据是否大于 0
        if (monitorAssets.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
