package com.jit.server.service.impl;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorTemplatesEntity;
import com.jit.server.pojo.SysMenuEntity;
import com.jit.server.repository.HostRepo;
import com.jit.server.request.HostParams;
import com.jit.server.service.HostService;
import com.jit.server.service.MonitorTemplatesService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.*;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.host.HostMacro;
import com.jit.zabbix.client.model.host.InterfaceType;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.model.template.ZabbixTemplate;
import com.jit.zabbix.client.request.*;
import com.jit.zabbix.client.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private HostRepo hostRepo;
    @Autowired
    private ZabbixHostService zabbixHostService;
    @Autowired
    private ZabbixHostInterfaceService zabbixHostInterfaceService;
    @Autowired
    private ZabbixAuthService zabbixAuthService;
    @Autowired
    private MonitorTemplatesService monitorTemplatesService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ZabbixHostGroupService zabbixHostGroupService;
    @Autowired
    private ZabbixItemService zabbixItemService;
    @Autowired
    private ZabbixHistoryService zabbixHistoryService;
    @Autowired
    private ZabbixTriggerService zabbixTriggerService;

    @Override
    public List<HostEntity> findByCondition(HostParams params) throws Exception {

        if (params != null) {
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
                    if (StringUtils.isNotEmpty(params.getObjectName())) {
                        list.add(cb.like(root.get("objectName").as(String.class), "%" + params.getObjectName() + "%"));
                    }

                    /** 业务名称 **/
                    if (StringUtils.isNotEmpty(params.getBusinessName())) {
                        list.add(cb.like(root.get("businessName").as(String.class), "%" + params.getBusinessName() + "%"));
                    }

                    /** 备注 **/
                    if (StringUtils.isNotEmpty(params.getRemark())) {
                        list.add(cb.like(root.get("remark").as(String.class), "%" + params.getRemark() + "%"));
                    }

                    /** 标签 **/
                    if (StringUtils.isNotEmpty(params.getLabel())) {
                        list.add(cb.like(root.get("label").as(String.class), "%" + params.getLabel() + "%"));
                    }

                    /** 资产 **/
                    if (StringUtils.isNotEmpty(params.getAssetsId())) {
                        list.add(cb.equal(root.get("assetsId").as(String.class), params.getAssetsId()));
                    }

                    /** 类型 **/
                    if (StringUtils.isNotEmpty(params.getTypeId())) {
                        list.add(cb.equal(root.get("typeId").as(String.class), params.getTypeId()));
                    }

                    /** 子类型 **/
                    if (StringUtils.isNotEmpty(params.getSubtypeId())) {
                        list.add(cb.equal(root.get("subtypeId").as(String.class), params.getSubtypeId()));
                    }

                    /** 分组 **/
                    if (StringUtils.isNotEmpty(params.getGroupId())) {
                        list.add(cb.equal(root.get("groupId").as(String.class), params.getGroupId()));
                    }

                    /** 是否监控 **/
                    if (StringUtils.isNotEmpty(params.getEnableMonitor())) {
                        list.add(cb.equal(root.get("enableMonitor").as(String.class), params.getEnableMonitor()));
                    }

                    Predicate[] arr = new Predicate[list.size()];
                    return cb.and(list.toArray(arr));
                }
            };
            //排序的定义
            /*List<Order> list = new ArrayList<>();
            Order order1 = new Order(Sort.Direction.DESC, "gmtModified");
            Order order2 = new Order(Sort.Direction.ASC, "id");
            list.add(order1);
            list.add(order2);
            Sort sort = Sort.by(list);
            //分页的定义
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            return this.hostRepo.findAll(spec, pageable);*/
            return this.hostRepo.findAll(spec);

        }
        return null;
    }

    @Override
    public String addHost(HostEntity host, String auth) throws Exception {
        //调用zabbix接口进行保存
        String hostid = createHostToZabbix(host,auth);
        if (StringUtils.isNotEmpty(hostid)) {
            host.setHostId(hostid.trim());
            //保存到本地
            hostRepo.save(host);
        }
        return hostid;
    }

    @Override
    public String deleteHost(HostEntity host , String auth) throws Exception {
        //调用zabbix接口进行删除
        String hostid = zabbixHostService.delete(host.getHostId(), auth);
        if (StringUtils.isNotEmpty(hostid)) {
            //更新本地
            hostRepo.save(host);
        }
        return hostid;
    }

    @Override
    public Optional<HostEntity> findByHostId(String id) throws Exception {
        return hostRepo.findById(id);
    }

    @Override
    public String updateHost(HostEntity host,String auth) throws Exception {
        //调用zabbix接口进行保存
        String hostid = updateHostToZabbix(host,auth);
        if (StringUtils.isNotEmpty(hostid)) {
            //更新本地
            hostRepo.save(host);
        }
        return hostid;
    }

    @Override
    public String updateHostEnableMonitor(HostEntity host,String auth) throws Exception {
        //调用zabbix接口进行保存
        String hostid = updateHostStatusToZabbix(host.getHostId(), host.getEnableMonitor(),auth);
        if (StringUtils.isNotEmpty(hostid)) {
            //更新本地
            hostRepo.save(host);
        }
        return hostid;
    }

    @Override
    public Page<Object> hostinfo(HostParams params, int page, int size) throws Exception {
        //分页的定义
        Pageable pageable = PageRequest.of(page - 1, size);
        //return this.hostRepo.getAllHostInfo(pageable);
        return predicateQuery(params, pageable);
    }

    @Override
    public List<ZabbixHostDTO> getHostAvailableFromZabbix(List<String> hostIds,String auth) throws Exception {
        //hostids 必填项
        if (hostIds == null || CollectionUtils.isEmpty(hostIds)) {
            return null;
        }
        //主机信息
        ZabbixGetHostParams params = new ZabbixGetHostParams();
        params.setHostIds(hostIds);
        return zabbixHostService.get(params, auth);
    }

    /**
     * 自定义拼接条件查询
     */
    private Page<Object> predicateQuery(HostParams params, Pageable pageable) {
        String comditionalSQL = "";
        String orderbySQL = " order by hostentity.gmtCreate desc,hostentity.id ";
        String baseSQL = "SELECT " +
                "hostentity.id, " +
                "hostentity.hostId, " +
                "hostentity.typeId, " +
                "hostentity.agentIp, " +
                "hostentity.snmpIp, " +
                "hostentity.enableMonitor, " +
                "hostentity.groupId, " +
                "hostentity.label, " +
                "hostentity.objectName, " +
                "hostentity.remark, " +
                "monitortem_.type, " +
                "monitortem2_.type as subtype, " +
                "hostentity.businessName, " +
                "hostentity.templatesId, " +
                "monitortemlate_.subtypeIds " +
                "FROM " +
                "HostEntity hostentity " +
                "LEFT JOIN MonitorTypeEntity monitortem_ ON hostentity.typeId = monitortem_.id " +
                "LEFT JOIN MonitorTypeEntity monitortem2_ ON hostentity.subtypeId = monitortem2_.id " +
                "LEFT JOIN MonitorTemplatesEntity monitortemlate_ ON hostentity.templatesId = monitortemlate_.id " +
                "WHERE hostentity.deleted=0 ";
        String countSQL = "SELECT count(1) " +
                "FROM " +
                "HostEntity hostentity " +
                "LEFT JOIN MonitorTypeEntity monitortem_ ON hostentity.typeId = monitortem_.id " +
                "LEFT JOIN MonitorTypeEntity monitortem2_ ON hostentity.subtypeId = monitortem2_.id " +
                "LEFT JOIN MonitorTemplatesEntity monitortemlate_ ON hostentity.templatesId = monitortemlate_.id " +
                "WHERE  hostentity.deleted=0 ";
        //map用来组装SQL占位符和对应的值
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(params.getHostObjectName())) {
            comditionalSQL += " and (hostentity.businessName like :hostObjectName or hostentity.remark like :hostObjectName)";
            map.put("hostObjectName", "%" + params.getHostObjectName().trim() + "%");
        }
        if (StringUtils.isNotEmpty(params.getHostIp())) {
            comditionalSQL += " and (hostentity.agentIp like :hostIp or hostentity.snmpIp like :hostIp)";
            map.put("hostIp", "%" + params.getHostIp().trim() + "%");
        }
        if (StringUtils.isNotEmpty(params.getTypeId())) {
            comditionalSQL += " and hostentity.typeId = :typeId";
            map.put("typeId", params.getTypeId().trim());
        }
        if (StringUtils.isNotEmpty(params.getSubtypeId())) {
            comditionalSQL += " and hostentity.subtypeId = :subtypeId";
            map.put("subtypeId", params.getSubtypeId().trim());
        }
        if (StringUtils.isNotEmpty(params.getEnableMonitor())) {
            comditionalSQL += " and hostentity.enableMonitor = :enableMonitor";
            map.put("enableMonitor", params.getEnableMonitor().trim());
        }
        if (StringUtils.isNotEmpty(params.getGroupId())) {
            comditionalSQL += " and hostentity.groupId like :groupId";
            map.put("groupId", "%" + params.getGroupId().trim() + "%");
        }
        //组装SQL
        String resSQL = baseSQL + comditionalSQL + orderbySQL;
        countSQL = countSQL + comditionalSQL;
        //创建查询对象
        /*这里是重点，将SQL放入方法中，创建一个查询对象，确切的说应该是HQL，
        他接收的也是Hibernate查询语句（我写原生SQL报错）。重点是，SQL是自己写的，
        这就灵活了很多。性能方面没有关注*/
        Query res = this.entityManager.createQuery(resSQL);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            res.setParameter(entry.getKey(), entry.getValue());
        }
        res.setFirstResult((int) pageable.getOffset());
        res.setMaxResults(pageable.getPageSize());

        Query countQuery = entityManager.createQuery(countSQL);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        Long totalCount = (Long) countQuery.getSingleResult();

        List<Object> resultList = res.getResultList();
        Page<Object> page = new PageImpl<Object>(resultList, pageable, totalCount.longValue());

        return page;
        //Query countRes=em.createQuery(countSQL);
    }

    /**
     * 调用zabbix接口进行保存
     *
     * @param host
     * @return
     * @throws ZabbixApiException
     */
    private String createHostToZabbix(HostEntity host,String auth) throws Exception {
        //主机名称、主机组、主机接口 必填项
        if (host == null || StringUtils.isEmpty(host.getObjectName())) {
            return null;
        }
        //主机信息
        ZabbixHostDTO dto = new ZabbixHostDTO();
        dto.setTechnicalName(host.getObjectName());
        dto.setName(host.getBusinessName());
        dto.setDescription(host.getRemark());
        dto.setUnmonitored("1".equals(host.getEnableMonitor()) ? false : true);
        //主机组信息
        String groupIds = host.getGroupId();
        if (StringUtils.isEmpty(groupIds)) {
            return null;
        }
        List<ZabbixHostGroup> groups = new ArrayList<ZabbixHostGroup>();
        String[] _groupIds = groupIds.split(",");
        for (String groupId : _groupIds) {
            if (StringUtils.isNotEmpty(groupId)) {
                ZabbixHostGroup group = new ZabbixHostGroup();
                group.setId(groupId.trim());
                groups.add(group);
            }
        }
        if (CollectionUtils.isEmpty(groups)) {
            return null;
        }
        dto.setGroups(groups);
        //主机接口信息
        String isIp = "1";
        String isDns = "2";
        List<ZabbixHostInterface> interfaces = new ArrayList<ZabbixHostInterface>();
        //AGENT
        String agentType = host.getAgentType();
        String agentPort = host.getAgentPort();
        if (StringUtils.isNotEmpty(agentType) && StringUtils.isNotEmpty(agentPort)) {
            String agentDnsName = host.getAgentDnsName();
            String agentIp = host.getAgentIp();
            ZabbixHostInterface _interface = new ZabbixHostInterface();
            if (isIp.equals(agentType.trim()) && StringUtils.isNotEmpty(agentIp.trim())) {
                //使用ip
                _interface.setDns(agentDnsName != null ? agentDnsName.trim() : "");
                _interface.setIp(agentIp.trim());
                _interface.setUseIp(true);
            } else if (isDns.equals(agentType.trim()) && StringUtils.isNotEmpty(agentDnsName.trim())) {
                //使用DNS
                _interface.setDns(agentDnsName.trim());
                _interface.setIp(agentIp != null ? agentIp.trim() : "");
                _interface.setUseIp(false);
            } else {
                return null;
            }
            _interface.setMain(true);
            _interface.setPort(agentPort.trim());
            _interface.setType(InterfaceType.AGENT);
            interfaces.add(_interface);
        }
        //JMX
        String jmxType = host.getJmxType();
        String jmxPort = host.getJmxPort();
        if (StringUtils.isNotEmpty(jmxType) && StringUtils.isNotEmpty(jmxPort)) {
            String jmxDnsName = host.getJmxDnsName();
            String jmxIp = host.getJmxIp();
            ZabbixHostInterface _interface = new ZabbixHostInterface();
            if (isIp.equals(jmxType.trim()) && StringUtils.isNotEmpty(jmxIp.trim())) {
                //使用ip
                _interface.setDns(jmxDnsName != null ? jmxDnsName.trim() : "");
                _interface.setIp(jmxIp.trim());
                _interface.setUseIp(true);
            } else if (isDns.equals(jmxType.trim()) && StringUtils.isNotEmpty(jmxDnsName.trim())) {
                //使用DNS
                _interface.setDns(jmxDnsName.trim());
                _interface.setIp(jmxIp != null ? jmxIp.trim() : "");
                _interface.setUseIp(false);
            } else {
                return null;
            }
            _interface.setMain(true);
            _interface.setPort(jmxPort.trim());
            _interface.setType(InterfaceType.JMX);
            interfaces.add(_interface);
        }
        //SNMP
        String snmpType = host.getSnmpType();
        String snmpPort = host.getSnmpPort();
        if (StringUtils.isNotEmpty(snmpType) && StringUtils.isNotEmpty(snmpPort)) {
            String snmpDnsName = host.getSnmpDnsName();
            String snmpIp = host.getSnmpIp();
            ZabbixHostInterface _interface = new ZabbixHostInterface();
            if (isIp.equals(snmpType.trim()) && StringUtils.isNotEmpty(snmpIp.trim())) {
                //使用ip
                _interface.setDns(snmpDnsName != null ? snmpDnsName.trim() : "");
                _interface.setIp(snmpIp.trim());
                _interface.setUseIp(true);
            } else if (isDns.equals(snmpType.trim()) && StringUtils.isNotEmpty(snmpDnsName.trim())) {
                //使用DNS
                _interface.setDns(snmpDnsName.trim());
                _interface.setIp(snmpIp != null ? snmpIp.trim() : "");
                _interface.setUseIp(false);
            } else {
                return null;
            }
            _interface.setMain(true);
            _interface.setPort(snmpPort.trim());
            _interface.setType(InterfaceType.SNMP);
            interfaces.add(_interface);
        }
        //IPMI
        String ipmiType = host.getIpmiType();
        String ipmiPort = host.getIpmiPort();
        if (StringUtils.isNotEmpty(ipmiType) && StringUtils.isNotEmpty(ipmiPort)) {
            String ipmiDnsName = host.getSnmpDnsName();
            String ipmiIp = host.getSnmpIp();
            ZabbixHostInterface _interface = new ZabbixHostInterface();
            if (isIp.equals(ipmiType.trim()) && StringUtils.isNotEmpty(ipmiIp.trim())) {
                //使用ip
                _interface.setDns(ipmiDnsName != null ? ipmiDnsName.trim() : "");
                _interface.setIp(ipmiIp.trim());
                _interface.setUseIp(true);
            } else if (isDns.equals(ipmiType.trim()) && StringUtils.isNotEmpty(ipmiDnsName.trim())) {
                //使用DNS
                _interface.setDns(ipmiDnsName.trim());
                _interface.setIp(ipmiIp != null ? ipmiIp.trim() : "");
                _interface.setUseIp(false);
            } else {
                return null;
            }
            _interface.setMain(true);
            _interface.setPort(ipmiPort.trim());
            _interface.setType(InterfaceType.IPMI);
            interfaces.add(_interface);
        }
        if (CollectionUtils.isEmpty(interfaces)) {
            return null;
        }
        dto.setInterfaces(interfaces);
        //主机模板信息
        String templatesId = host.getTemplatesId();
        if (StringUtils.isNotEmpty(templatesId)) {
            MonitorTemplatesEntity monitorTemplatesEntity = monitorTemplatesService.getMonitorTemplate(templatesId.trim());
            if (monitorTemplatesEntity != null) {
                String templateIds = monitorTemplatesEntity.getTemplateIds();
                if (StringUtils.isNotEmpty(templateIds)) {
                    String[] _templateIds = templateIds.split(",");
                    List<ZabbixTemplate> templates = new ArrayList<ZabbixTemplate>();
                    for (String templateId : _templateIds) {
                        if (StringUtils.isNotEmpty(templateId)) {
                            ZabbixTemplate template = new ZabbixTemplate();
                            template.setId(templateId.trim());
                            templates.add(template);
                        }
                    }
                    if (!CollectionUtils.isEmpty(templates)) {
                        dto.setTemplates(templates);
                    }
                }
            }
        }

        //主机宏信息
        List<HostMacro> macros = new ArrayList<HostMacro>();
        String macro_jmx = host.getJmxMacro();
        if (StringUtils.isNotEmpty(macro_jmx)) {
            //JMX：主机宏(JMX路径)
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PATH}");
            macro.setValue(macro_jmx.trim());
            macros.add(macro);
        }
        String macro_snmp = host.getSnmpMacro();
        if (StringUtils.isNotEmpty(macro_snmp)) {
            //SNMP：主机宏(SNMP团体名)
            HostMacro macro = new HostMacro();
            macro.setMacro("{$SNMP_COMMUNITY}");
            macro.setValue(macro_snmp.trim());
            macros.add(macro);
        }
        String macro_mssql_instance = host.getMssqlMacroInstance();
        if (StringUtils.isNotEmpty(macro_mssql_instance)) {
            //MSSQLSERVICE主机宏：实例名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$INSTANCENAME}");
            macro.setValue(macro_mssql_instance.trim());
            macros.add(macro);
        }
        String macro_mssql_odbc = host.getMssqlMacroOdbc();
        if (StringUtils.isNotEmpty(macro_mssql_odbc)) {
            //MSSQLSERVICE主机宏：ODBC源名称
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ODBC}");
            macro.setValue(macro_mssql_odbc.trim());
            macros.add(macro);
        }
        String macro_mssql_password = host.getMssqlMacroPassword();
        if (StringUtils.isNotEmpty(macro_mssql_password)) {
            //MSSQLSERVICE主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_mssql_password.trim());
            macros.add(macro);
        }
        String macro_mssql_username = host.getMssqlMacroUsername();
        if (StringUtils.isNotEmpty(macro_mssql_username)) {
            //MSSQLSERVICE主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USER}");
            macro.setValue(macro_mssql_username.trim());
            macros.add(macro);
        }
        String macro_oracle_ip = host.getOracleMacroIp();
        if (StringUtils.isNotEmpty(macro_oracle_ip)) {
            //ORACLE主机宏：oracle所在主机IP
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ADDRESS}");
            macro.setValue(macro_oracle_ip.trim());
            macros.add(macro);
        }
        String macro_oracle_port = host.getOracleMacroPort();
        if (StringUtils.isNotEmpty(macro_oracle_port)) {
            //ORACLE主机宏：oracle端口
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PORT}");
            macro.setValue(macro_oracle_port.trim());
            macros.add(macro);
        }
        String macro_oracle_asm = host.getOracleMacroAsm();
        if (StringUtils.isNotEmpty(macro_oracle_asm)) {
            //ORACLE主机宏：ASM卷名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ARCHIVE}");
            macro.setValue(macro_oracle_asm.trim());
            macros.add(macro);
        }
        String macro_oracle_dbname = host.getOracleMacroDbname();
        if (StringUtils.isNotEmpty(macro_oracle_dbname)) {
            //ORACLE主机宏：数据库名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$DATABASE}");
            macro.setValue(macro_oracle_dbname.trim());
            macros.add(macro);
        }
        String macro_oracle_password = host.getOracleMacroPassword();
        if (StringUtils.isNotEmpty(macro_oracle_password)) {
            //ORACLE主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_oracle_password.trim());
            macros.add(macro);
        }
        String macro_oracle_username = host.getOracleMacroUsername();
        if (StringUtils.isNotEmpty(macro_oracle_username)) {
            //ORACLE主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USERNAME}");
            macro.setValue(macro_oracle_username.trim());
            macros.add(macro);
        }
        String macro_vm_cpu_frequency = host.getVmMacroCpuFrequency();
        if (StringUtils.isNotEmpty(macro_vm_cpu_frequency)) {
            //虚拟化主机宏：CPU单核频率
            HostMacro macro = new HostMacro();
            macro.setMacro("{$FREQ}");
            macro.setValue(macro_vm_cpu_frequency.trim());
            macros.add(macro);
        }
        String macro_vm_password = host.getVmMacroPassword();
        if (StringUtils.isNotEmpty(macro_vm_password)) {
            //虚拟化主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_vm_password.trim());
            macros.add(macro);
        }
        String macro_vm_sdk_link = host.getVmMacroSdkLink();
        if (StringUtils.isNotEmpty(macro_vm_sdk_link)) {
            //虚拟化主机宏：SDK链接
            HostMacro macro = new HostMacro();
            macro.setMacro("{$URL}");
            macro.setValue(macro_vm_sdk_link.trim());
            macros.add(macro);
        }
        String macro_vm_username = host.getVmMacroUsername();
        if (StringUtils.isNotEmpty(macro_vm_username)) {
            //虚拟化主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USERNAME}");
            macro.setValue(macro_vm_username.trim());
            macros.add(macro);
        }
        if (!CollectionUtils.isEmpty(macros)) {
            dto.setMacros(macros);
        }
        return zabbixHostService.create(dto, auth);
    }

    /**
     * 调用zabbix接口进行修改
     *
     * @param host
     * @return
     * @throws ZabbixApiException
     */
    private String updateHostToZabbix(HostEntity host, String auth) throws Exception {
        //hostid 必填项
        if (host == null || StringUtils.isEmpty(host.getHostId())) {
            return null;
        }
        //主机信息
        ZabbixHostDTO dto = new ZabbixHostDTO();
        dto.setId(host.getHostId().trim());
        if (host.getObjectName() != null) {
            dto.setTechnicalName(host.getObjectName());
        }
        if (host.getBusinessName() != null) {
            dto.setName(host.getBusinessName());
        }
        if (host.getRemark() != null) {
            dto.setDescription(host.getRemark());
        }
        if ("0".equals(host.getEnableMonitor()) || "1".equals(host.getEnableMonitor())) {
            dto.setUnmonitored("1".equals(host.getEnableMonitor()) ? false : true);
        }

        //主机组信息
        String groupIds = host.getGroupId();
        if (StringUtils.isNotEmpty(groupIds)) {
            List<ZabbixHostGroup> groups = new ArrayList<ZabbixHostGroup>();
            String[] _groupIds = groupIds.split(",");
            for (String groupId : _groupIds) {
                if (StringUtils.isNotEmpty(groupId)) {
                    ZabbixHostGroup group = new ZabbixHostGroup();
                    group.setId(groupId.trim());
                    groups.add(group);
                }
            }
            if (!CollectionUtils.isEmpty(groups)) {
                dto.setGroups(groups);
            }
        }

        //主机接口信息
        ZabbixGetHostInterfaceParams params = new ZabbixGetHostInterfaceParams();
        params.setHostIds(Arrays.asList(new String[]{host.getHostId().trim()}));
        List<ZabbixHostInterface> zabbixHostInterfaceList = zabbixHostInterfaceService.get(params, auth);
        if (zabbixHostInterfaceList != null && !CollectionUtils.isEmpty(zabbixHostInterfaceList)) {
            String isIp = "1";
            String isDns = "2";
            for (ZabbixHostInterface zabbixInterface : zabbixHostInterfaceList) {
                if (InterfaceType.AGENT.equals(zabbixInterface.getType())) {
                    //AGENT
                    String agentType = host.getAgentType();
                    String agentPort = host.getAgentPort();
                    if (StringUtils.isNotEmpty(agentType) && StringUtils.isNotEmpty(agentPort)) {
                        String agentDnsName = host.getAgentDnsName();
                        String agentIp = host.getAgentIp();
                        if (isIp.equals(agentType.trim()) && StringUtils.isNotEmpty(agentIp.trim())) {
                            //使用ip
                            zabbixInterface.setDns(agentDnsName != null ? agentDnsName.trim() : "");
                            zabbixInterface.setIp(agentIp.trim());
                            zabbixInterface.setUseIp(true);
                        } else if (isDns.equals(agentType.trim()) && StringUtils.isNotEmpty(agentDnsName.trim())) {
                            //使用DNS
                            zabbixInterface.setDns(agentDnsName.trim());
                            zabbixInterface.setIp(agentIp != null ? agentIp.trim() : "");
                            zabbixInterface.setUseIp(false);
                        } else {
                            continue;
                        }
                        zabbixInterface.setPort(agentPort.trim());
                    }
                } else if (InterfaceType.JMX.equals(zabbixInterface.getType())) {
                    //JMX
                    String jmxType = host.getJmxType();
                    String jmxPort = host.getJmxPort();
                    if (StringUtils.isNotEmpty(jmxType) && StringUtils.isNotEmpty(jmxPort)) {
                        String jmxDnsName = host.getJmxDnsName();
                        String jmxIp = host.getJmxIp();
                        if (isIp.equals(jmxType.trim()) && StringUtils.isNotEmpty(jmxIp.trim())) {
                            //使用ip
                            zabbixInterface.setDns(jmxDnsName != null ? jmxDnsName.trim() : "");
                            zabbixInterface.setIp(jmxIp.trim());
                            zabbixInterface.setUseIp(true);
                        } else if (isDns.equals(jmxType.trim()) && StringUtils.isNotEmpty(jmxDnsName.trim())) {
                            //使用DNS
                            zabbixInterface.setDns(jmxDnsName.trim());
                            zabbixInterface.setIp(jmxIp != null ? jmxIp.trim() : "");
                            zabbixInterface.setUseIp(false);
                        } else {
                            continue;
                        }
                        zabbixInterface.setPort(jmxPort.trim());
                    }
                } else if (InterfaceType.SNMP.equals(zabbixInterface.getType())) {
                    //SNMP
                    String snmpType = host.getSnmpType();
                    String snmpPort = host.getSnmpPort();
                    if (StringUtils.isNotEmpty(snmpType) && StringUtils.isNotEmpty(snmpPort)) {
                        String snmpDnsName = host.getSnmpDnsName();
                        String snmpIp = host.getSnmpIp();
                        if (isIp.equals(snmpType.trim()) && StringUtils.isNotEmpty(snmpIp.trim())) {
                            //使用ip
                            zabbixInterface.setDns(snmpDnsName != null ? snmpDnsName.trim() : "");
                            zabbixInterface.setIp(snmpIp.trim());
                            zabbixInterface.setUseIp(true);
                        } else if (isDns.equals(snmpType.trim()) && StringUtils.isNotEmpty(snmpDnsName.trim())) {
                            //使用DNS
                            zabbixInterface.setDns(snmpDnsName.trim());
                            zabbixInterface.setIp(snmpIp != null ? snmpIp.trim() : "");
                            zabbixInterface.setUseIp(false);
                        } else {
                            continue;
                        }
                        zabbixInterface.setPort(snmpPort.trim());
                    }
                }
            }
            dto.setInterfaces(zabbixHostInterfaceList);
        }
        //更新中不做主机模板信息更新

        //主机宏信息
        List<HostMacro> macros = new ArrayList<HostMacro>();
        String macro_jmx = host.getJmxMacro();
        if (StringUtils.isNotEmpty(macro_jmx)) {
            //JMX：主机宏(JMX路径)
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PATH}");
            macro.setValue(macro_jmx.trim());
            macros.add(macro);
        }
        String macro_snmp = host.getSnmpMacro();
        if (StringUtils.isNotEmpty(macro_snmp)) {
            //SNMP：主机宏(SNMP团体名)
            HostMacro macro = new HostMacro();
            macro.setMacro("{$SNMP_COMMUNITY}");
            macro.setValue(macro_snmp.trim());
            macros.add(macro);
        }
        String macro_mssql_instance = host.getMssqlMacroInstance();
        if (StringUtils.isNotEmpty(macro_mssql_instance)) {
            //MSSQLSERVICE主机宏：实例名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$INSTANCENAME}");
            macro.setValue(macro_mssql_instance.trim());
            macros.add(macro);
        }
        String macro_mssql_odbc = host.getMssqlMacroOdbc();
        if (StringUtils.isNotEmpty(macro_mssql_odbc)) {
            //MSSQLSERVICE主机宏：ODBC源名称
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ODBC}");
            macro.setValue(macro_mssql_odbc.trim());
            macros.add(macro);
        }
        String macro_mssql_password = host.getMssqlMacroPassword();
        if (StringUtils.isNotEmpty(macro_mssql_password)) {
            //MSSQLSERVICE主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_mssql_password.trim());
            macros.add(macro);
        }
        String macro_mssql_username = host.getMssqlMacroUsername();
        if (StringUtils.isNotEmpty(macro_mssql_username)) {
            //MSSQLSERVICE主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USER}");
            macro.setValue(macro_mssql_username.trim());
            macros.add(macro);
        }
        String macro_oracle_ip = host.getOracleMacroIp();
        if (StringUtils.isNotEmpty(macro_oracle_ip)) {
            //ORACLE主机宏：oracle所在主机IP
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ADDRESS}");
            macro.setValue(macro_oracle_ip.trim());
            macros.add(macro);
        }
        String macro_oracle_asm = host.getOracleMacroAsm();
        if (StringUtils.isNotEmpty(macro_oracle_asm)) {
            //ORACLE主机宏：ASM卷名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ARCHIVE}");
            macro.setValue(macro_oracle_asm.trim());
            macros.add(macro);
        }
        String macro_oracle_dbname = host.getOracleMacroDbname();
        if (StringUtils.isNotEmpty(macro_oracle_dbname)) {
            //ORACLE主机宏：数据库名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$DATABASE}");
            macro.setValue(macro_oracle_dbname.trim());
            macros.add(macro);
        }
        String macro_oracle_password = host.getOracleMacroPassword();
        if (StringUtils.isNotEmpty(macro_oracle_password)) {
            //ORACLE主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_oracle_password.trim());
            macros.add(macro);
        }
        String macro_oracle_username = host.getOracleMacroUsername();
        if (StringUtils.isNotEmpty(macro_oracle_username)) {
            //ORACLE主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USERNAME}");
            macro.setValue(macro_oracle_username.trim());
            macros.add(macro);
        }
        String macro_vm_cpu_frequency = host.getVmMacroCpuFrequency();
        if (StringUtils.isNotEmpty(macro_vm_cpu_frequency)) {
            //虚拟化主机宏：CPU单核频率
            HostMacro macro = new HostMacro();
            macro.setMacro("{$FREQ}");
            macro.setValue(macro_vm_cpu_frequency.trim());
            macros.add(macro);
        }
        String macro_vm_password = host.getVmMacroPassword();
        if (StringUtils.isNotEmpty(macro_vm_password)) {
            //虚拟化主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_vm_password.trim());
            macros.add(macro);
        }
        String macro_vm_sdk_link = host.getVmMacroSdkLink();
        if (StringUtils.isNotEmpty(macro_vm_sdk_link)) {
            //虚拟化主机宏：SDK链接
            HostMacro macro = new HostMacro();
            macro.setMacro("{$URL}");
            macro.setValue(macro_vm_sdk_link.trim());
            macros.add(macro);
        }
        String macro_vm_username = host.getVmMacroUsername();
        if (StringUtils.isNotEmpty(macro_vm_username)) {
            //虚拟化主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USERNAME}");
            macro.setValue(macro_vm_username.trim());
            macros.add(macro);
        }
        if (!CollectionUtils.isEmpty(macros)) {
            dto.setMacros(macros);
        }
        return zabbixHostService.update(dto, auth);
    }

    /**
     * 调用zabbix接口进行修改Status
     *
     * @param hostId
     * @param status
     * @return
     * @throws ZabbixApiException
     */
    private String updateHostStatusToZabbix(String hostId, String status, String auth) throws Exception {
        //hostid status 必填项
        if (StringUtils.isEmpty(hostId) || StringUtils.isEmpty(status)) {
            return null;
        }
        if (!"0".equals(status.trim()) && !"1".equals(status.trim())) {
            return null;
        }
        //主机信息
        ZabbixHostDTO dto = new ZabbixHostDTO();
        dto.setId(hostId.trim());
        dto.setUnmonitored("1".equals(status.trim()) ? false : true);
        return zabbixHostService.update(dto, auth);
    }

    /**
     * 按主机类型获得zabbix主机组
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public List<ZabbixHostGroupDTO> findHostGroupByTypeId(Map<String, Object> params,String auth) throws Exception {
        if (params != null) {
            String typeId = (String) params.get("typeId");
            if (StringUtils.isNotEmpty(typeId)) {
                String groupName = (String) params.get("groupName");
                List<HostEntity> hostList = hostRepo.findByTypeIdAndDeleted(typeId, ConstUtil.IS_NOT_DELETED);
                if (null != hostList && !CollectionUtils.isEmpty(hostList)) {
                    List<String> hostIds = new ArrayList<>();
                    for (HostEntity host : hostList) {
                        if (StringUtils.isNotEmpty(host.getHostId())) {
                            hostIds.add(host.getHostId().trim());
                        }
                    }
                    if (!CollectionUtils.isEmpty(hostIds)) {
                        ZabbixGetHostGroupParams _params = new ZabbixGetHostGroupParams();
                        _params.setHostIds(hostIds);
                        if (StringUtils.isNotEmpty(groupName)) {
                            Map<String, Object> search = new HashMap<>();
                            search.put("name", groupName);
                            _params.setSearch(search);
                        }
                        return zabbixHostGroupService.get(_params, auth);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获得所有主机指标项的TOP5值
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> getTop5ByItem(Map<String, Object> params,String auth) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        List<Map<String, String>> resultnew = new ArrayList<>();
        if (params != null) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Map<String, String> map = null;
            String typeId = (String) params.get("typeId");
            String subtypeId = (String) params.get("subtypeId");
            String itemKey = (String) params.get("itemKey");
            String valueType = (String) params.get("valueType");

            if (StringUtils.isEmpty(itemKey)) {
                return null;
            }
            HostParams hostParams = new HostParams();
            if (StringUtils.isNotEmpty(typeId)) {
                hostParams.setTypeId(typeId);
            }
            if (StringUtils.isNotEmpty(subtypeId)) {
                hostParams.setSubtypeId(subtypeId);
            }
            List<HostEntity> hostList = findByCondition(hostParams);

            List<String> hostIds = new ArrayList<>();
            for (HostEntity host : hostList) {
                String hostId = host.getHostId();
                hostIds.add(hostId);
                map = new HashMap<>();
                map.put("hostId", hostId);
                map.put("hostName", host.getBusinessName());
                result.add(map);
            }

            if (CollectionUtils.isEmpty(hostIds)) {
                return null;
            }

            ZabbixGetItemParams itemParams = new ZabbixGetItemParams();
            itemParams.setOutput(Arrays.asList(new String[]{"itemid", "hostid", "name", "key_"}));
            itemParams.setHostIds(hostIds);
            Map<String, Object> filter = new HashMap<>();
            // filter.put("key_","jmx[\"java.lang:type=Threading\",ThreadCount]");
            filter.put("key_", itemKey.trim());
            itemParams.setFilter(filter);

            List<ZabbixGetItemDTO> itemList = zabbixItemService.get(itemParams, auth);
            ZabbixGetHistoryParams historyParams = null;
            for (ZabbixGetItemDTO dto : itemList) {
                String hostId = dto.getHostId();
                String itemId = dto.getId();
                String itemName = dto.getName();

                historyParams = new ZabbixGetHistoryParams();
                try {
                    historyParams.setHistory(!"0".equals(valueType) ? Integer.parseInt(valueType) : 0);
                } catch (Exception e) {
                    historyParams.setHistory(0);
                }
                historyParams.setItemIds(Arrays.asList(new String[]{itemId}));
                historyParams.setSortFields(Arrays.asList(new String[]{"clock"}));
                historyParams.setSortOrder(Arrays.asList(new String[]{"DESC"}));
                historyParams.setLimit(1);

                for (Map<String, String> _map : result) {
                    if (hostId.equals(_map.get("hostId"))) {
                        Map<String, String> mapData = new HashMap<String, String>();
                        mapData.put("hostId", _map.get("hostId"));
                        mapData.put("hostName", _map.get("hostName"));
                        mapData.put("itemId", itemId);
                        mapData.put("itemName", itemName);

                        List<ZabbixHistoryDTO> historyList = zabbixHistoryService.get(historyParams, auth);
                        if (historyList != null && !CollectionUtils.isEmpty(historyList)) {
                            ZabbixHistoryDTO history = historyList.get(0);
                            try {
                                mapData.put("clock", df.format(history.getClock()));
                            } catch (Exception e) {
                                mapData.put("clock", "");
                            }
                            mapData.put("value", history.getValue());
                        } else {
                            mapData.put("clock", "");
                            mapData.put("value", "0");
                        }
                        resultnew.add(mapData);
                        break;
                    }
                }
            }

            if (!CollectionUtils.isEmpty(resultnew)) {
                // 比较value为0移除此数据
                for (int i = 0; i < resultnew.size(); i++) {
                    try {
                        Map<String, String> _map = resultnew.get(i);
                            double d = Double.parseDouble(_map.get("value"));
                            if ((d < 0.0001) && (d > -0.0001)) {
                                result.remove(i);
                            }
                    } catch (Exception e) {
                        result.remove(i);
                        break;
                    }
                }
                Collections.sort(resultnew, new Comparator<Map>() {
                    @Override
                    public int compare(Map o1, Map o2) {
                        try {
                            double v1 = Double.parseDouble((String) o1.get("value"));
                            double v2 = Double.parseDouble((String) o2.get("value"));
                            if (v1 > v2) {
                                return -1;
                            } else if (v1 < v2) {
                                return 1;
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            return 0;
                        }
                    }
                });
                resultnew = resultnew.subList(0, resultnew.size() > 4 ? 5 : resultnew.size());
            }
            return resultnew;
        }
        return null;
    }

    /**
     * 获得所有主机触发器严重的TOP5值
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> getTop5ByTrigger(Map<String, Object> params, String auth) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        if (params != null) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Map<String, String> map = null;
            String typeId = (String) params.get("typeId");
            String subtypeId = (String) params.get("subtypeId");
            String valueType = (String) params.get("valueType");

            HostParams hostParams = new HostParams();
            if (StringUtils.isNotEmpty(typeId)) {
                hostParams.setTypeId(typeId);
            }
            if (StringUtils.isNotEmpty(subtypeId)) {
                hostParams.setSubtypeId(subtypeId);
            }
            List<HostEntity> hostList = findByCondition(hostParams);

            List<String> hostIds = new ArrayList<>();
            for (HostEntity host : hostList) {
                String hostId = host.getHostId();
                hostIds.add(hostId);
                map = new HashMap<>();
                map.put("hostId", hostId);
                map.put("hostName", host.getBusinessName());
                ZabbixGetTriggerParams triggerParams = new ZabbixGetTriggerParams();
                triggerParams.setOutput(Arrays.asList(new String[]{"triggerid", "description", "priority", "value"}));
                triggerParams.setHostIds(Arrays.asList(new String[]{hostId}));
                Map<String, Object> filter = new HashMap<>();
                //filter.put("status",0);
                filter.put("value", 1);
                try {
                    filter.put("priority", !"4".equals(valueType) ? Integer.parseInt(valueType) : 4);
                } catch (Exception e) {
                    //filter.put("priority",4);
                }
                triggerParams.setFilter(filter);
                triggerParams.setSortFields(Arrays.asList(new String[]{"priority"}));
                triggerParams.setSortOrder(Arrays.asList(new String[]{"DESC"}));
                List<ZabbixTriggerDTO> triggerList = zabbixTriggerService.get(triggerParams, auth);
                if (triggerList != null && !CollectionUtils.isEmpty(triggerList)) {
                    map.put("value", String.valueOf(triggerList.size()));
                    result.add(map);
                } else {
                    map.put("value", "0");
                }
                //result.add(map);
            }
            if (!CollectionUtils.isEmpty(result)) {
                Collections.sort(result, new Comparator<Map>() {
                    @Override
                    public int compare(Map o1, Map o2) {
                        int v1 = Integer.parseInt((String) o1.get("value"));
                        int v2 = Integer.parseInt((String) o2.get("value"));
                        if (v1 > v2) {
                            return -1;
                        } else if (v1 < v2) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                result = result.subList(0, result.size() > 4 ? 5 : result.size());
            }
            return result;
        }
        return null;
    }

    @Override
    public HostEntity findHostIdinfo(String id) throws Exception {
        return hostRepo.findByHostId(id);
    }

    @Override
    public List<HostEntity> getHosts() throws Exception {
        return hostRepo.findByDeleted(ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public List<Object> getHostIds() throws Exception {
        return hostRepo.getHostIds();
    }

    /**
     * 判断主机名称是否为空
     * @param objectName 主机名称
     * @param odlObjectName 旧的主机名称
     * @return 返回false表示主机名称重复，反之true
     */
    @Override
    public Boolean checkObjectName(String objectName, String odlObjectName) throws Exception {
        List<HostEntity> hostEntities = hostRepo.findByObjectNameAndDeleted(objectName, ConstUtil.IS_NOT_DELETED);
        if (objectName.equals(odlObjectName)) {
            return true;
        }
        if (hostEntities.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断业务名称是否为空
     * @param businessName 业务名称
     * @param odlBusinessName 旧的业务名称
     * @return 返回false表示业务名称重复，反之true
     */
    @Override
    public Boolean checkBusinessName(String businessName, String odlBusinessName) {
        List<HostEntity> hostEntities = hostRepo.findByBusinessNameAndDeleted(businessName,  ConstUtil.IS_NOT_DELETED);
        if (businessName.equals(odlBusinessName)) {
            return true;
        }
        if (hostEntities.size() > 0) {
            return false;
        }
        return true;
    }
}
