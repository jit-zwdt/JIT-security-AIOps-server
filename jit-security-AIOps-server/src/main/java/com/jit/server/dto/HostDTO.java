package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: HostDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostDTO {

    private String id;

    private String hostId;

    private String objectName;

    private String businessName;

    private String assetsId;

    private String proxyMonitor;

    private String enableMonitor;

    private String templatesId;

    private String typeId;

    private String subtypeId;

    private String groupId;

    private String remark;

    private String label;

    private String agentType;

    private String agentIp;

    private String agentDnsName;

    private String agentPort;

    private String jmxType;

    private String jmxIp;

    private String jmxDnsName;

    private String jmxPort;

    private String jmxMacro;

    private String snmpType;

    private String snmpIp;

    private String snmpDnsName;

    private String snmpPort;

    private String snmpMacro;

    private String ipmiType;

    private String ipmiIp;

    private String ipmiDnsName;

    private String ipmiPort;

    private String ipmiMacro;

    private String mssqlMacroInstance;

    private String mssqlMacroOdbc;

    private String mssqlMacroPassword;

    private String mssqlMacroUsername;

    private String oracleMacroIp;

    private String oracleMacroPort;

    private String oracleMacroAsm;

    private String oracleMacroDbname;

    private String oracleMacroPassword;

    private String oracleMacroUsername;

    private String vmMacroCpuFrequency;

    private String vmMacroPassword;

    private String vmMacroSdkLink;

    private String vmMacroUsername;

}
