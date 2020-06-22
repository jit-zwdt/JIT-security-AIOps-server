package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.template.ZabbixTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Zabbix Template DTO used as parameter in method template.get.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/template/get">method template.get</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixGetTemplateDTO extends ZabbixTemplate {

    @JsonProperty("proxy_hostid")
    protected String proxyHostId;

    @JsonProperty("disable_until")
    protected String disableUntil;

    @JsonProperty("error")
    protected String error;

    @JsonProperty("available")
    protected String available;

    @JsonProperty("errors_from")
    protected String errorsFrom;

    @JsonProperty("lastaccess")
    protected String lastaccess;

    @JsonProperty("ipmi_authtype")
    protected String ipmiAuthtype;

    @JsonProperty("ipmi_privilege")
    protected String ipmiPrivilege;

    @JsonProperty("ipmi_username")
    protected String ipmiUsername;

    @JsonProperty("ipmi_password")
    protected String ipmiPassword;

    @JsonProperty("ipmi_disable_until")
    protected String ipmiDisableUntil;

    @JsonProperty("ipmi_available")
    protected String ipmiAvailable;

    @JsonProperty("snmp_disable_until")
    protected String snmpDisableUntil;

    @JsonProperty("snmp_available")
    protected String snmpAvailable;

    @JsonProperty("maintenanceid")
    protected String maintenanceid;

    @JsonProperty("maintenance_status")
    protected String maintenanceStatus;

    @JsonProperty("maintenance_type")
    protected String maintenanceType;

    @JsonProperty("maintenance_from")
    protected String maintenanceFrom;

    @JsonProperty("ipmi_errors_from")
    protected String ipmiErrorsFrom;

    @JsonProperty("snmp_errors_from")
    protected String snmpErrorsFrom;

    @JsonProperty("ipmi_error")
    protected String ipmiError;


    @JsonProperty("snmp_error")
    protected String snmpError;

    @JsonProperty("jmx_disable_until")
    protected String jmxDisableUntil;

    @JsonProperty("jmx_available")
    protected String jmxAvailable;

    @JsonProperty("jmx_errors_from")
    protected String jmxErrorsFrom;

    @JsonProperty("jmx_error")
    protected String jmxError;

    @JsonProperty("flags")
    protected String flags;

    @JsonProperty("tls_connect")
    protected String tlsConnect;

    @JsonProperty("tls_accept")
    protected String tlsAccept;

    @JsonProperty("tls_issuer")
    protected String tlsIssuer;

    @JsonProperty("tls_subject")
    protected String tlsSubject;

    @JsonProperty("tls_psk_identity")
    protected String tlsPskIdentity;

    @JsonProperty("tls_psk")
    protected String tlsPsk;

}
