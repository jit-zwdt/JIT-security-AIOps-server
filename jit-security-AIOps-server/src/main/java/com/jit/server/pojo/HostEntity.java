package com.jit.server.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @Description: table assets entity
 * @Author: yongbin_jiang
 * @Date: 2020/06/19 11:01
 */
@Data
@Entity
@Table(name = "monitor_host")
public class HostEntity {
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "hostid")
    private String hostId;

    @Column(name = "host_object_name")
    private String objectName;

    @Column(name = "host_business_name")
    private String businessName;

    @Column(name = "host_assets_id")
    private String assetsId;

    @Column(name = "host_proxy_monitor")
    private String proxyMonitor;

    @Column(name = "host_enable_monitor")
    private String enableMonitor;

    @Column(name = "host_templates_id")
    private String templatesId;

    @Column(name = "host_type_id")
    private String typeId;

    @Column(name = "host_subtype_id")
    private String subtypeId;

    @Column(name = "host_group_id")
    private String groupId;

    @Column(name = "host_remark")
    private String remark;

    @Column(name = "host_label")
    private String label;

    @Column(name = "host_agent_type")
    private String agentType;

    @Column(name = "host_agent_ip")
    private String agentIp;

    @Column(name = "host_agent_dns_name")
    private String agentDnsName;

    @Column(name = "host_agent_port")
    private String agentPort;

    @Column(name = "host_jmx_type")
    private String jmxType;

    @Column(name = "host_jmx_ip")
    private String jmxIp;

    @Column(name = "host_jmx_dns_name")
    private String jmxDnsName;

    @Column(name = "host_jmx_port")
    private String jmxPort;

    @Column(name = "host_jmx_macro")
    private String jmxMacro;

    @Column(name = "host_snmp_type")
    private String snmpType;

    @Column(name = "host_snmp_ip")
    private String snmpIp;

    @Column(name = "host_snmp_dns_name")
    private String snmpDnsName;

    @Column(name = "host_snmp_port")
    private String snmpPort;

    @Column(name = "host_snmp_macro")
    private String snmpMacro;

    @Column(name = "host_ipmi_type")
    private String ipmiType;

    @Column(name = "host_ipmi_ip")
    private String ipmiIp;

    @Column(name = "host_ipmi_dns_name")
    private String ipmiDnsName;

    @Column(name = "host_ipmi_port")
    private String ipmiPort;

    @Column(name = "host_ipmi_macro")
    private String ipmiMacro;

    @Column(name = "host_mssql_macro_instance")
    private String mssqlMacroInstance;

    @Column(name = "host_mssql_macro_odbc")
    private String mssqlMacroOdbc;

    @Column(name = "host_mssql_macro_password")
    private String mssqlMacroPassword;

    @Column(name = "host_mssql_macro_username")
    private String mssqlMacroUsername;

    @Column(name = "host_oracle_macro_ip")
    private String oracleMacroIp;

    @Column(name = "host_oracle_macro_asm")
    private String oracleMacroAsm;

    @Column(name = "host_oracle_macro_dbname")
    private String oracleMacroDbname;

    @Column(name = "host_oracle_macro_password")
    private String oracleMacroPassword;

    @Column(name = "host_oracle_macro_username")
    private String oracleMacroUsername;

    @Column(name = "host_vm_macro_cpu_frequency")
    private String vmMacroCpuFrequency;

    @Column(name = "host_vm_macro_password")
    private String vmMacroPassword;

    @Column(name = "host_vm_macro_sdk_link")
    private String vmMacroSdkLink;

    @Column(name = "host_vm_macro_username")
    private String vmMacroUsername;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Column(name = "gmt_create")
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;
}
