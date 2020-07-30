package com.jit.zabbix.client.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.model.host.IPMIAuthAlgo;
import com.jit.zabbix.client.model.host.TLSEncryption;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixHostInfoDTO {

    @JsonProperty("hostid")
    protected String id;
    @JsonProperty("proxy_hostid")
    protected String proxyHostId;
    protected String host;
    protected Integer status;
    @JsonProperty("disable_until")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime disableUntil;
    protected String error;
    protected Integer available;
    @JsonProperty("errors_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime errorsFrom;
    @JsonProperty("lastaccess")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime lastAccess;
    @JsonProperty("ipmi_authtype")
    protected Integer ipmiAuthType;
    @JsonProperty("ipmi_privilege")
    protected Integer ipmiPrivilege;
    @JsonProperty("ipmi_username")
    protected String ipmiUsername;
    @JsonProperty("ipmi_password")
    protected String ipmiPassword;
    @JsonProperty("ipmi_disable_until")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime ipmiDisableUntil;
    @JsonProperty("ipmi_available")
    protected Integer ipmiAvailable;
    @JsonProperty("snmp_disable_until")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime snmpDisableUntil;
    @JsonProperty("snmp_available")
    protected Integer snmpAvailable;
    @JsonProperty("maintenanceid")
    protected String maintenanceId;
    @JsonProperty("maintenance_status")
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    protected boolean maintenanceStatus;
    @JsonProperty("maintenance_type")
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    protected boolean maintenanceType;
    @JsonProperty("maintenance_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime maintenanceFrom;
    @JsonProperty("ipmi_errors_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime ipmiErrorsFrom;
    @JsonProperty("snmp_errors_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime snmpErrorsFrom;
    @JsonProperty("ipmi_error")
    protected String impiError;
    @JsonProperty("snmp_error")
    protected String snmpError;
    @JsonProperty("jmx_disable_until")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime jmxDisableUntil;
    @JsonProperty("jmx_available")
    protected Integer jmxAvailable;
    @JsonProperty("jmx_errors_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime jmxErrorsFrom;
    @JsonProperty("jmx_error")
    protected String jmxError;
    protected String name;
    protected Integer flags;
    @JsonProperty("templateid")
    protected String templateId;
    protected String description;
    @JsonProperty("tls_connect")
    protected Integer tlsConnect;
    @JsonProperty("tls_accept")
    protected Integer tlsAccept;
    @JsonProperty("tls_issuer")
    protected String tlsIssuer;
    @JsonProperty("tls_subject")
    protected String tlsSubject;
    @JsonProperty("tls_psk_identity")
    protected String tlsPskIdentity;
    @JsonProperty("tls_psk")
    protected String tlsPsk;
    @JsonProperty("proxy_address")
    protected String proxyAddress;
    @JsonProperty("auto_compress")
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    protected boolean autoCompress;
    @JsonProperty("inventory_mode")
    protected Integer inventoryMode;
}
