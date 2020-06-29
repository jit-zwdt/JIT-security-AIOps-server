package com.jit.server.service.impl;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorTemplatesEntity;
import com.jit.server.repository.HostRepo;
import com.jit.server.request.HostParams;
import com.jit.server.service.HostService;
import com.jit.server.service.MonitorTemplatesService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.host.HostMacro;
import com.jit.zabbix.client.model.host.InterfaceType;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.model.template.ZabbixTemplate;
import com.jit.zabbix.client.request.ZabbixGetHostInterfaceParams;
import com.jit.zabbix.client.service.ZabbixHostInterfaceService;
import com.jit.zabbix.client.service.ZabbixHostService;
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
import java.math.BigInteger;
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
    public String addHost(HostEntity host) throws Exception {
        //调用zabbix接口进行保存
        String hostid = createHostToZabbix(host);
        if(StringUtils.isNotEmpty(hostid)){
            host.setHostId(hostid.trim());
            //保存到本地
            hostRepo.save(host);
        }
        return hostid;
    }

    @Override
    public String deleteHost(HostEntity host) throws Exception {
        //获得token
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)){
            return null;
        }
        //调用zabbix接口进行删除
        String hostid = zabbixHostService.delete(host.getHostId(), authToken);
        if(StringUtils.isNotEmpty(hostid)){
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
    public String updateHost(HostEntity host) throws Exception {
        //调用zabbix接口进行保存
        String hostid = updateHostToZabbix(host);
        if(StringUtils.isNotEmpty(hostid)){
            //更新本地
            hostRepo.save(host);
        }
        return hostid;
    }

    @Override
    public String updateHostEnableMonitor(HostEntity host) throws Exception {
        //调用zabbix接口进行保存
        String hostid = updateHostStatusToZabbix(host.getHostId(), host.getEnableMonitor());
        if(StringUtils.isNotEmpty(hostid)){
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

    /**
     * 自定义拼接条件查询
     */
    private Page<Object> predicateQuery(HostParams params, Pageable pageable) {
        String comditionalSQL = "";
        String orderbySQL = " order by hostentity.gmtModified desc,hostentity.id ";
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
                "hostentity.businessName " +
                "FROM " +
                "HostEntity hostentity " +
                "LEFT JOIN MonitorTypeEntity monitortem_ ON hostentity.typeId = monitortem_.id " +
                "LEFT JOIN MonitorTypeEntity monitortem2_ ON hostentity.subtypeId = monitortem2_.id " +
                "WHERE hostentity.deleted=0 ";
        String countSQL  = "SELECT count(1) " +
                "FROM " +
                "HostEntity hostentity " +
                "LEFT JOIN MonitorTypeEntity monitortem_ ON hostentity.typeId = monitortem_.id " +
                "LEFT JOIN MonitorTypeEntity monitortem2_ ON hostentity.subtypeId = monitortem2_.id " +
                "WHERE  hostentity.deleted=0 ";
        //map用来组装SQL占位符和对应的值
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(params.getHostObjectName())){
            comditionalSQL+=" and (hostentity.businessName like :hostObjectName or hostentity.remark like :hostObjectName)";
            map.put("hostObjectName", "%"+params.getHostObjectName().trim()+"%");
        }
        if(StringUtils.isNotEmpty(params.getHostIp())){
            comditionalSQL+=" and (hostentity.agentIp like :hostIp or hostentity.snmpIp like :hostIp)";
            map.put("hostIp", "%"+params.getHostIp().trim()+"%");
        }
        if(StringUtils.isNotEmpty(params.getTypeId())){
            comditionalSQL+=" and hostentity.typeId = :typeId";
            map.put("typeId", params.getTypeId().trim());
        }
        if(StringUtils.isNotEmpty(params.getSubtypeId())){
            comditionalSQL+=" and hostentity.subtypeId = :subtypeId";
            map.put("subtypeId", params.getSubtypeId().trim());
        }
        if(StringUtils.isNotEmpty(params.getEnableMonitor())){
            comditionalSQL+=" and hostentity.enableMonitor = :enableMonitor";
            map.put("enableMonitor", params.getEnableMonitor().trim());
        }
        if(StringUtils.isNotEmpty(params.getGroupId())){
            comditionalSQL+=" and hostentity.groupId like :groupId";
            map.put("groupId", "%"+params.getGroupId().trim()+"%");
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
     * @param host
     * @return
     * @throws ZabbixApiException
     */
    private String createHostToZabbix(HostEntity host) throws Exception {
        //主机名称、主机组、主机接口 必填项
        if(host==null || StringUtils.isEmpty(host.getObjectName())){
            return null;
        }
        //主机信息
        ZabbixHostDTO dto = new ZabbixHostDTO();
        dto.setHost(host.getObjectName());
        dto.setName(host.getBusinessName());
        dto.setDescription(host.getRemark());
        dto.setStatus("1".equals(host.getEnableMonitor())?false:true);
        //主机组信息
        String groupIds = host.getGroupId();
        if(StringUtils.isEmpty(groupIds)){
            return null;
        }
        List<ZabbixHostGroup> groups = new ArrayList<ZabbixHostGroup>();
        String[] _groupIds = groupIds.split(",");
        for(String groupId : _groupIds){
            if(StringUtils.isNotEmpty(groupId)){
                ZabbixHostGroup group = new ZabbixHostGroup();
                group.setId(groupId.trim());
                groups.add(group);
            }
        }
        if(CollectionUtils.isEmpty(groups)){
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
        if(StringUtils.isNotEmpty(agentType) && StringUtils.isNotEmpty(agentPort)){
            String agentDnsName = host.getAgentDnsName();
            String agentIp = host.getAgentIp();
            ZabbixHostInterface _interface = new ZabbixHostInterface();
            if(isIp.equals(agentType.trim()) && StringUtils.isNotEmpty(agentIp.trim())){
                //使用ip
                _interface.setDns(agentDnsName!=null?agentDnsName.trim():"");
                _interface.setIp(agentIp.trim());
                _interface.setUseIp(true);
            }else if(isDns.equals(agentType.trim()) && StringUtils.isNotEmpty(agentDnsName.trim())){
                //使用DNS
                _interface.setDns(agentDnsName.trim());
                _interface.setIp(agentIp!=null?agentIp.trim():"");
                _interface.setUseIp(false);
            }else{
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
        if(StringUtils.isNotEmpty(jmxType) && StringUtils.isNotEmpty(jmxPort)){
            String jmxDnsName = host.getJmxDnsName();
            String jmxIp = host.getJmxIp();
            ZabbixHostInterface _interface = new ZabbixHostInterface();
            if(isIp.equals(jmxType.trim()) && StringUtils.isNotEmpty(jmxIp.trim())){
                //使用ip
                _interface.setDns(jmxDnsName!=null?jmxDnsName.trim():"");
                _interface.setIp(jmxIp.trim());
                _interface.setUseIp(true);
            }else if(isDns.equals(jmxType.trim()) && StringUtils.isNotEmpty(jmxDnsName.trim())){
                //使用DNS
                _interface.setDns(jmxDnsName.trim());
                _interface.setIp(jmxIp!=null?jmxIp.trim():"");
                _interface.setUseIp(false);
            }else{
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
        if(StringUtils.isNotEmpty(snmpType) && StringUtils.isNotEmpty(snmpPort)){
            String snmpDnsName = host.getSnmpDnsName();
            String snmpIp = host.getSnmpIp();
            ZabbixHostInterface _interface = new ZabbixHostInterface();
            if(isIp.equals(snmpType.trim()) && StringUtils.isNotEmpty(snmpIp.trim())){
                //使用ip
                _interface.setDns(snmpDnsName!=null?snmpDnsName.trim():"");
                _interface.setIp(snmpIp.trim());
                _interface.setUseIp(true);
            }else if(isDns.equals(snmpType.trim()) && StringUtils.isNotEmpty(snmpDnsName.trim())){
                //使用DNS
                _interface.setDns(snmpDnsName.trim());
                _interface.setIp(snmpIp!=null?snmpIp.trim():"");
                _interface.setUseIp(false);
            }else{
                return null;
            }
            _interface.setMain(true);
            _interface.setPort(snmpPort.trim());
            _interface.setType(InterfaceType.SNMP);
            interfaces.add(_interface);
        }
        if(CollectionUtils.isEmpty(interfaces)){
            return null;
        }
        dto.setInterfaces(interfaces);
        //主机模板信息
        String templatesId = host.getTemplatesId();
        if(StringUtils.isNotEmpty(templatesId)){
            MonitorTemplatesEntity monitorTemplatesEntity = monitorTemplatesService.getMonitorTemplate(templatesId.trim());
            if(monitorTemplatesEntity!=null){
                String templateIds = monitorTemplatesEntity.getTemplates();
                if(StringUtils.isNotEmpty(templateIds)){
                    String[] _templateIds = templateIds.split(",");
                    List<ZabbixTemplate> templates = new ArrayList<ZabbixTemplate>();
                    for(String templateId : _templateIds){
                        if(StringUtils.isNotEmpty(templateId)){
                            ZabbixTemplate template = new ZabbixTemplate();
                            template.setId(templateId.trim());
                            templates.add(template);
                        }
                    }
                    if(!CollectionUtils.isEmpty(templates)){
                        dto.setTemplates(templates);
                    }
                }
            }
        }

        //主机宏信息
        List<HostMacro> macros = new ArrayList<HostMacro>();
        String macro_jmx = host.getJmxMacro();
        if(StringUtils.isNotEmpty(macro_jmx)){
            //JMX：主机宏(JMX路径)
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PATH}");
            macro.setValue(macro_jmx.trim());
            macros.add(macro);
        }
        String macro_snmp = host.getSnmpMacro();
        if(StringUtils.isNotEmpty(macro_snmp)){
            //SNMP：主机宏(SNMP团体名)
            HostMacro macro = new HostMacro();
            macro.setMacro("{$SNMP_COMMUNITY}");
            macro.setValue(macro_snmp.trim());
            macros.add(macro);
        }
        String macro_mssql_instance = host.getMssqlMacroInstance();
        if(StringUtils.isNotEmpty(macro_mssql_instance)){
            //MSSQLSERVICE主机宏：实例名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$INSTANCENAME}");
            macro.setValue(macro_mssql_instance.trim());
            macros.add(macro);
        }
        String macro_mssql_odbc = host.getMssqlMacroOdbc();
        if(StringUtils.isNotEmpty(macro_mssql_odbc)){
            //MSSQLSERVICE主机宏：ODBC源名称
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ODBC}");
            macro.setValue(macro_mssql_odbc.trim());
            macros.add(macro);
        }
        String macro_mssql_password = host.getMssqlMacroPassword();
        if(StringUtils.isNotEmpty(macro_mssql_password)){
            //MSSQLSERVICE主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_mssql_password.trim());
            macros.add(macro);
        }
        String macro_mssql_username = host.getMssqlMacroUsername();
        if(StringUtils.isNotEmpty(macro_mssql_username)){
            //MSSQLSERVICE主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USER}");
            macro.setValue(macro_mssql_username.trim());
            macros.add(macro);
        }
        String macro_oracle_ip = host.getOracleMacroIp();
        if(StringUtils.isNotEmpty(macro_oracle_ip)){
            //ORACLE主机宏：oracle所在主机IP
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ADDRESS}");
            macro.setValue(macro_oracle_ip.trim());
            macros.add(macro);
        }
        String macro_oracle_asm = host.getOracleMacroAsm();
        if(StringUtils.isNotEmpty(macro_oracle_asm)){
            //ORACLE主机宏：ASM卷名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ARCHIVE}");
            macro.setValue(macro_oracle_asm.trim());
            macros.add(macro);
        }
        String macro_oracle_dbname = host.getOracleMacroDbname();
        if(StringUtils.isNotEmpty(macro_oracle_dbname)){
            //ORACLE主机宏：数据库名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$DATABASE}");
            macro.setValue(macro_oracle_dbname.trim());
            macros.add(macro);
        }
        String macro_oracle_password = host.getOracleMacroPassword();
        if(StringUtils.isNotEmpty(macro_oracle_password)){
            //ORACLE主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_oracle_password.trim());
            macros.add(macro);
        }
        String macro_oracle_username = host.getOracleMacroUsername();
        if(StringUtils.isNotEmpty(macro_oracle_username)){
            //ORACLE主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USERNAME}");
            macro.setValue(macro_oracle_username.trim());
            macros.add(macro);
        }
        String macro_vm_cpu_frequency = host.getVmMacroCpuFrequency();
        if(StringUtils.isNotEmpty(macro_vm_cpu_frequency)){
            //虚拟化主机宏：CPU单核频率
            HostMacro macro = new HostMacro();
            macro.setMacro("{$FREQ}");
            macro.setValue(macro_vm_cpu_frequency.trim());
            macros.add(macro);
        }
        String macro_vm_password = host.getVmMacroPassword();
        if(StringUtils.isNotEmpty(macro_vm_password)){
            //虚拟化主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_vm_password.trim());
            macros.add(macro);
        }
        String macro_vm_sdk_link = host.getVmMacroSdkLink();
        if(StringUtils.isNotEmpty(macro_vm_sdk_link)){
            //虚拟化主机宏：SDK链接
            HostMacro macro = new HostMacro();
            macro.setMacro("{$URL}");
            macro.setValue(macro_vm_sdk_link.trim());
            macros.add(macro);
        }
        String macro_vm_username = host.getVmMacroUsername();
        if(StringUtils.isNotEmpty(macro_vm_username)){
            //虚拟化主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USERNAME}");
            macro.setValue(macro_vm_username.trim());
            macros.add(macro);
        }
        if(!CollectionUtils.isEmpty(macros)){
            dto.setMacros(macros);
        }

        //获得token
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)){
            return null;
        }
        return zabbixHostService.create(dto, authToken);
    }

    /**
     * 调用zabbix接口进行修改
     * @param host
     * @return
     * @throws ZabbixApiException
     */
    private String updateHostToZabbix(HostEntity host) throws Exception {
        //hostid 必填项
        if(host==null || StringUtils.isEmpty(host.getHostId())){
            return null;
        }
        //主机信息
        ZabbixHostDTO dto = new ZabbixHostDTO();
        dto.setHostId(host.getHostId().trim());
        if(host.getObjectName()!=null){
            dto.setHost(host.getObjectName());
        }
        if(host.getBusinessName()!=null){
            dto.setName(host.getBusinessName());
        }
        if(host.getRemark()!=null){
            dto.setDescription(host.getRemark());
        }
        if("0".equals(host.getEnableMonitor())||"1".equals(host.getEnableMonitor())){
            dto.setStatus("1".equals(host.getEnableMonitor())?false:true);
        }

        //主机组信息
        String groupIds = host.getGroupId();
        if(StringUtils.isNotEmpty(groupIds)){
            List<ZabbixHostGroup> groups = new ArrayList<ZabbixHostGroup>();
            String[] _groupIds = groupIds.split(",");
            for(String groupId : _groupIds){
                if(StringUtils.isNotEmpty(groupId)){
                    ZabbixHostGroup group = new ZabbixHostGroup();
                    group.setId(groupId.trim());
                    groups.add(group);
                }
            }
            if(!CollectionUtils.isEmpty(groups)){
                dto.setGroups(groups);
            }
        }

        //主机接口信息
        //获得token
        String _authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(_authToken)){
            return null;
        }
        ZabbixGetHostInterfaceParams params = new ZabbixGetHostInterfaceParams();
        params.setHostIds(Arrays.asList(new String[]{host.getHostId().trim()}));
        List<ZabbixHostInterface> zabbixHostInterfaceList = zabbixHostInterfaceService.get(params, _authToken);
        if(zabbixHostInterfaceList!=null && !CollectionUtils.isEmpty(zabbixHostInterfaceList)){
            String isIp = "1";
            String isDns = "2";
            for(ZabbixHostInterface zabbixInterface : zabbixHostInterfaceList) {
                if(InterfaceType.AGENT.equals(zabbixInterface.getType())){
                    //AGENT
                    String agentType = host.getAgentType();
                    String agentPort = host.getAgentPort();
                    if(StringUtils.isNotEmpty(agentType) && StringUtils.isNotEmpty(agentPort)){
                        String agentDnsName = host.getAgentDnsName();
                        String agentIp = host.getAgentIp();
                        if(isIp.equals(agentType.trim()) && StringUtils.isNotEmpty(agentIp.trim())){
                            //使用ip
                            zabbixInterface.setDns(agentDnsName!=null?agentDnsName.trim():"");
                            zabbixInterface.setIp(agentIp.trim());
                            zabbixInterface.setUseIp(true);
                        }else if(isDns.equals(agentType.trim()) && StringUtils.isNotEmpty(agentDnsName.trim())){
                            //使用DNS
                            zabbixInterface.setDns(agentDnsName.trim());
                            zabbixInterface.setIp(agentIp!=null?agentIp.trim():"");
                            zabbixInterface.setUseIp(false);
                        }else{
                            continue;
                        }
                        zabbixInterface.setPort(agentPort.trim());
                    }
                }else if(InterfaceType.JMX.equals(zabbixInterface.getType())){
                    //JMX
                    String jmxType = host.getJmxType();
                    String jmxPort = host.getJmxPort();
                    if(StringUtils.isNotEmpty(jmxType) && StringUtils.isNotEmpty(jmxPort)){
                        String jmxDnsName = host.getJmxDnsName();
                        String jmxIp = host.getJmxIp();
                        if(isIp.equals(jmxType.trim()) && StringUtils.isNotEmpty(jmxIp.trim())){
                            //使用ip
                            zabbixInterface.setDns(jmxDnsName!=null?jmxDnsName.trim():"");
                            zabbixInterface.setIp(jmxIp.trim());
                            zabbixInterface.setUseIp(true);
                        }else if(isDns.equals(jmxType.trim()) && StringUtils.isNotEmpty(jmxDnsName.trim())){
                            //使用DNS
                            zabbixInterface.setDns(jmxDnsName.trim());
                            zabbixInterface.setIp(jmxIp!=null?jmxIp.trim():"");
                            zabbixInterface.setUseIp(false);
                        }else{
                            continue;
                        }
                        zabbixInterface.setPort(jmxPort.trim());
                    }
                }else if(InterfaceType.SNMP.equals(zabbixInterface.getType())){
                    //SNMP
                    String snmpType = host.getSnmpType();
                    String snmpPort = host.getSnmpPort();
                    if(StringUtils.isNotEmpty(snmpType) && StringUtils.isNotEmpty(snmpPort)){
                        String snmpDnsName = host.getSnmpDnsName();
                        String snmpIp = host.getSnmpIp();
                        if(isIp.equals(snmpType.trim()) && StringUtils.isNotEmpty(snmpIp.trim())){
                            //使用ip
                            zabbixInterface.setDns(snmpDnsName!=null?snmpDnsName.trim():"");
                            zabbixInterface.setIp(snmpIp.trim());
                            zabbixInterface.setUseIp(true);
                        }else if(isDns.equals(snmpType.trim()) && StringUtils.isNotEmpty(snmpDnsName.trim())){
                            //使用DNS
                            zabbixInterface.setDns(snmpDnsName.trim());
                            zabbixInterface.setIp(snmpIp!=null?snmpIp.trim():"");
                            zabbixInterface.setUseIp(false);
                        }else{
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
        if(StringUtils.isNotEmpty(macro_jmx)){
            //JMX：主机宏(JMX路径)
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PATH}");
            macro.setValue(macro_jmx.trim());
            macros.add(macro);
        }
        String macro_snmp = host.getSnmpMacro();
        if(StringUtils.isNotEmpty(macro_snmp)){
            //SNMP：主机宏(SNMP团体名)
            HostMacro macro = new HostMacro();
            macro.setMacro("{$SNMP_COMMUNITY}");
            macro.setValue(macro_snmp.trim());
            macros.add(macro);
        }
        String macro_mssql_instance = host.getMssqlMacroInstance();
        if(StringUtils.isNotEmpty(macro_mssql_instance)){
            //MSSQLSERVICE主机宏：实例名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$INSTANCENAME}");
            macro.setValue(macro_mssql_instance.trim());
            macros.add(macro);
        }
        String macro_mssql_odbc = host.getMssqlMacroOdbc();
        if(StringUtils.isNotEmpty(macro_mssql_odbc)){
            //MSSQLSERVICE主机宏：ODBC源名称
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ODBC}");
            macro.setValue(macro_mssql_odbc.trim());
            macros.add(macro);
        }
        String macro_mssql_password = host.getMssqlMacroPassword();
        if(StringUtils.isNotEmpty(macro_mssql_password)){
            //MSSQLSERVICE主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_mssql_password.trim());
            macros.add(macro);
        }
        String macro_mssql_username = host.getMssqlMacroUsername();
        if(StringUtils.isNotEmpty(macro_mssql_username)){
            //MSSQLSERVICE主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USER}");
            macro.setValue(macro_mssql_username.trim());
            macros.add(macro);
        }
        String macro_oracle_ip = host.getOracleMacroIp();
        if(StringUtils.isNotEmpty(macro_oracle_ip)){
            //ORACLE主机宏：oracle所在主机IP
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ADDRESS}");
            macro.setValue(macro_oracle_ip.trim());
            macros.add(macro);
        }
        String macro_oracle_asm = host.getOracleMacroAsm();
        if(StringUtils.isNotEmpty(macro_oracle_asm)){
            //ORACLE主机宏：ASM卷名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$ARCHIVE}");
            macro.setValue(macro_oracle_asm.trim());
            macros.add(macro);
        }
        String macro_oracle_dbname = host.getOracleMacroDbname();
        if(StringUtils.isNotEmpty(macro_oracle_dbname)){
            //ORACLE主机宏：数据库名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$DATABASE}");
            macro.setValue(macro_oracle_dbname.trim());
            macros.add(macro);
        }
        String macro_oracle_password = host.getOracleMacroPassword();
        if(StringUtils.isNotEmpty(macro_oracle_password)){
            //ORACLE主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_oracle_password.trim());
            macros.add(macro);
        }
        String macro_oracle_username = host.getOracleMacroUsername();
        if(StringUtils.isNotEmpty(macro_oracle_username)){
            //ORACLE主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USERNAME}");
            macro.setValue(macro_oracle_username.trim());
            macros.add(macro);
        }
        String macro_vm_cpu_frequency = host.getVmMacroCpuFrequency();
        if(StringUtils.isNotEmpty(macro_vm_cpu_frequency)){
            //虚拟化主机宏：CPU单核频率
            HostMacro macro = new HostMacro();
            macro.setMacro("{$FREQ}");
            macro.setValue(macro_vm_cpu_frequency.trim());
            macros.add(macro);
        }
        String macro_vm_password = host.getVmMacroPassword();
        if(StringUtils.isNotEmpty(macro_vm_password)){
            //虚拟化主机宏：密码
            HostMacro macro = new HostMacro();
            macro.setMacro("{$PASSWORD}");
            macro.setValue(macro_vm_password.trim());
            macros.add(macro);
        }
        String macro_vm_sdk_link = host.getVmMacroSdkLink();
        if(StringUtils.isNotEmpty(macro_vm_sdk_link)){
            //虚拟化主机宏：SDK链接
            HostMacro macro = new HostMacro();
            macro.setMacro("{$URL}");
            macro.setValue(macro_vm_sdk_link.trim());
            macros.add(macro);
        }
        String macro_vm_username = host.getVmMacroUsername();
        if(StringUtils.isNotEmpty(macro_vm_username)){
            //虚拟化主机宏：用户名
            HostMacro macro = new HostMacro();
            macro.setMacro("{$USERNAME}");
            macro.setValue(macro_vm_username.trim());
            macros.add(macro);
        }
        if(!CollectionUtils.isEmpty(macros)){
            dto.setMacros(macros);
        }

        //获得token
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)){
            return null;
        }
        return zabbixHostService.update(dto, authToken);
    }

    /**
     * 调用zabbix接口进行修改Status
     * @param hostId
     * @param status
     * @return
     * @throws ZabbixApiException
     */
    private String updateHostStatusToZabbix(String hostId, String status) throws Exception {
        //hostid status 必填项
        if(StringUtils.isEmpty(hostId) || StringUtils.isEmpty(status)){
            return null;
        }
        if(!"0".equals(status.trim())&&!"1".equals(status.trim())){
            return null;
        }
        //主机信息
        ZabbixHostDTO dto = new ZabbixHostDTO();
        dto.setHostId(hostId.trim());
        dto.setStatus("1".equals(status.trim())?false:true);
        //获得token
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)){
            return null;
        }
        return zabbixHostService.update(dto, authToken);
    }
}
