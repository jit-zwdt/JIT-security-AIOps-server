package com.jit.server.repository;

import com.jit.server.dto.MonitorAssetsDTO;
import com.jit.server.pojo.MonitorAssetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/16 13:25
 */
@Repository
public interface AssetsRepo extends JpaRepository<MonitorAssetsEntity, String>, JpaSpecificationExecutor<MonitorAssetsEntity> {

//    @Override
//    Page<MonitorAssetsEntity> findAll(Specification<MonitorAssetsEntity> spec, Pageable pageable);

    @Query(value = "select e.id,e.name,e.number,e.ip from MonitorAssetsEntity e where e.isDeleted = 0 and e.type = '0'")
    List<Object> findHardwareInfo();

    @Query(value = "select e.id,e.name,e.number,e.ip from MonitorAssetsEntity e where e.isDeleted = 0 and e.type = '0' and e.ip is not null and length(e.ip) > 0")
    List<Object> findByConditionInfo();

    /**
     * 根据查询语句查询条数和总 CPU 大小 , 内存大小 , 硬件大小
     *
     * @return 数据数组
     */
    @Query(value = "select count(t.id) as number , sum(t.cpu) , sum(t.memory) , sum(t.hardDisk) from MonitorAssetsEntity t where t.type = '0' and t.isDeleted = 0")
    List<Object[]> getCountAndSum();

    /**
     * 根据 Ip 查询数据
     *
     * @param ip        ip
     * @param isDeleted 是否删除 0 未删除 1 已删除
     * @return MonitorAssetsEntity 集合对象
     */
    List<MonitorAssetsEntity> findByIpAndIsDeleted(String ip, int isDeleted);

    /**
     * 根据 number 资产编号查询数据
     *
     * @param number    资产编号
     * @param isDeleted 是否删除 0 未删除 1 已删除
     * @return MonitorAssetsEntity 集合对象
     */
    List<MonitorAssetsEntity> findByNumberAndIsDeleted(String number, int isDeleted);

    @Query(value = "select new com.jit.server.dto.MonitorAssetsDTO(t.id, t.name, t.number, t.type, t.state, t.classify, t.gbType, t.ip, t.backupIp, t.amount, t.belongsDept, t.belongsPerson, t.registerDate, t.registrant, t.updateDate, t.location, t.logoutDate, t.dateRecorded, t.worth, t.acquisitionMode, t.userDepartment, t.user, t.objectClassification, t.sn, t.brand, t.productModel, t.parentId, t.cpu, t.cpuCoreNumber, t.memory, t.hardDisk) from MonitorAssetsEntity t where t.id =?1 and t.isDeleted = 0")
    MonitorAssetsDTO findMonitorAssetByAssetsId(String id);
}
