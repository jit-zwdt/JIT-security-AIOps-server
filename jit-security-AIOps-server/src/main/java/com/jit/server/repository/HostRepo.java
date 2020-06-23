package com.jit.server.repository;

import com.jit.server.pojo.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/19 13:25
 */
@Repository
public interface HostRepo extends JpaRepository<HostEntity, String>, JpaSpecificationExecutor<HostEntity> {

    public Page<HostEntity> findAll(Specification<HostEntity> spec, Pageable pageable);

    @Query(value = " SELECT hostentity.id as id, " +
            "        hostentity.hostid as hostid, " +
            "        hostentity.host_type_id as hosttypeId, " +
            "        hostentity.host_agent_ip as agentIp, " +
            "        hostentity.host_snmp_ip as snmpIp, " +
            "        hostentity.host_enable_monitor as enableMonitor, " +
            "        hostentity.host_group_id as groupId, " +
            "        hostentity.host_label as hostLabel, " +
            "        hostentity.host_object_name as objectName, " +
            "        hostentity.host_remark as remark, " +
            "        monitortem_.type as typeId " +
            "    from " +
            "        monitor_host hostentity  " +
            "    left  join " +
            "        monitor_templates monitortem_ " +
            "            on hostentity.host_type_id=monitortem_.id " +
            "   ",
            countQuery = " SELECT count(hostentity.id) from monitor_host hostentity left  join monitor_templates monitortem_ on hostentity.host_type_id=monitortem_.id ",
            nativeQuery = true )
    Page<Object> getAllHostInfo(Pageable pageable);
}
