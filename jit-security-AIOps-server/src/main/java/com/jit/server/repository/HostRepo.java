package com.jit.server.repository;

import com.jit.server.dto.HostDTO;
import com.jit.server.pojo.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/19 13:25
 */
@Repository
public interface HostRepo extends JpaRepository<HostEntity, String>, JpaSpecificationExecutor<HostEntity> {

    @Override
    public Page<HostEntity> findAll(Specification<HostEntity> spec, Pageable pageable);

    @Query(value = "SELECT " +
            "hostentity.id AS id, " +
            "hostentity.hostid AS hostid, " +
            "hostentity.host_type_id AS hosttypeId, " +
            "hostentity.host_agent_ip AS agentIp, " +
            "hostentity.host_snmp_ip AS snmpIp, " +
            "hostentity.host_enable_monitor AS enableMonitor, " +
            "hostentity.host_group_id AS groupId, " +
            "hostentity.host_label AS hostLabel, " +
            "hostentity.host_object_name AS objectName, " +
            "hostentity.host_remark AS remark, " +
            "monitortem_.type AS type, " +
            "monitortem2_.type AS subtype " +
            "FROM " +
            "monitor_host hostentity " +
            "LEFT JOIN monitor_type monitortem_ ON hostentity.host_type_id = monitortem_.id " +
            "LEFT JOIN monitor_type monitortem2_ ON hostentity.host_subtype_id = monitortem2_.id ",
            countQuery = " SELECT count(hostentity.id) from monitor_host hostentity left  join monitor_templates monitortem_ on hostentity.host_type_id=monitortem_.id ",
            nativeQuery = true)
    Page<Object> getAllHostInfo(Pageable pageable);

    public List<HostEntity> findByTypeIdAndDeleted(String typeId, int deleted);

    public HostEntity findByHostId(String hostId);

    @Query("SELECT h.hostId,t.type,t.id FROM HostEntity h,MonitorTypeEntity t WHERE t.isDeleted = 0 AND t.pid = '0' AND h.deleted = 0 and h.typeId = t.id ORDER BY t.orderNum")
    List<Object> getHostByType();

    List<HostEntity> findByDeleted(int deleted);

    @Query("SELECT h.hostId,h.businessName FROM HostEntity h WHERE h.deleted = 0")
    List<Object> getHostIds();

    @Query(" SELECT h.hostId,h.businessName," +
            "    CASE " +
            "        WHEN h.agentIp is null THEN " +
            "            h.snmpIp " +
            "        WHEN h.agentIp ='' THEN " +
            "            h.snmpIp " +
            "        ELSE " +
            "            h.agentIp " +
            "    END AS ip  FROM HostEntity h WHERE h.deleted = 0 ")
    List<Object> getHostIdsAndIp();

    List<HostEntity> findByObjectNameAndDeleted(String objectName, int i);

    List<HostEntity> findByBusinessNameAndDeleted(String businessName, int i);

    /**
     * 根据 JMX IP 查询对应的数据
     * @param jmxIp JMX IP
     * @param deleted 删除状态 0 未删除 1 删除
     * @return 查询到的数据结果集合
     */
    List<HostEntity> findByJmxIpAndDeleted(String jmxIp, int deleted);

    /**
     * 查询数据根据关联 ID 进行查询
     * @param assetsId assetsId 关联ID
     * @param isDeleted 是否删除 0 未删除 1 删除
     * @return HostEntity 集合对象
     */
    List<HostEntity> findByAssetsIdAndDeleted(String assetsId, int isDeleted);

    @Query("SELECT new com.jit.server.dto.HostDTO(id, hostId, objectName, businessName, assetsId, proxyMonitor, enableMonitor, templatesId, typeId, subtypeId, groupId, remark, label, agentType, agentIp, agentDnsName, agentPort, jmxType, jmxIp, jmxDnsName, jmxPort, jmxMacro, snmpType, snmpIp, snmpDnsName, snmpPort, snmpMacro, ipmiType, ipmiIp, ipmiDnsName, ipmiPort, ipmiMacro, mssqlMacroInstance, mssqlMacroOdbc, mssqlMacroPassword, mssqlMacroUsername, oracleMacroIp, oracleMacroPort, oracleMacroAsm, oracleMacroDbname, oracleMacroPassword, oracleMacroUsername, vmMacroCpuFrequency, vmMacroPassword, vmMacroSdkLink, vmMacroUsername) FROM HostEntity h WHERE h.hostId=?1")
    HostDTO findHostByHostId(String id);
}
