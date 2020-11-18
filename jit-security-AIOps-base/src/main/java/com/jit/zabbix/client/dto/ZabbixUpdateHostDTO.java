package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.model.host.*;
import com.jit.zabbix.client.model.template.ZabbixTemplate;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Zabbix Host DTO used as parameter in method host.create and returned in host.get.
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/create">method host.create</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixUpdateHostDTO {

    /**
     * add host info
     */
    @JsonProperty("hostid")
    protected String id;
    @JsonProperty("host")
    protected String technicalName;
    protected String description;
    @JsonProperty("disable_until")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime disableUntil;
    protected String error;
    @JsonProperty("errors_from")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    protected String errorFrom;
    @JsonProperty("flags")
    protected OriginFlag flag;
    @JsonProperty("inventory_mode")
    @Builder.Default
    protected InventoryMode inventoryMode = InventoryMode.MANUAL;
    @JsonProperty("ipmi_authtype")
    @Builder.Default
    protected IPMIAuthAlgo ipmiAuthType = IPMIAuthAlgo.DEFAULT;
    @JsonProperty("ipmi_available")
    @Builder.Default
    protected Availabity ipmiAvailable = Availabity.UNKNOWN;
    @JsonProperty("ipmi_disable_until")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime ipmiDisableUntil;
    @JsonProperty("ipmi_error")
    protected String impiError;
    @JsonProperty("ipmi_errors_from")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime ipmiErrorsFrom;
    @JsonProperty("ipmi_password")
    protected String ipmiPassword;
    @JsonProperty("ipmi_privilege")
    @Builder.Default
    protected IPMIPrivilege ipmiPrivilege = IPMIPrivilege.USER;
    @JsonProperty("ipmi_username")
    protected String ipmiUsername;
    @JsonProperty("jmx_available")
    protected Availabity jmxAvailable;
    @JsonProperty("jmx_disable_until")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime jmxDisableUntil;
    @JsonProperty("jmx_error")
    protected String jmxError;
    @JsonProperty("jmx_errors_from")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime jmxErrorsFrom;
    @JsonProperty("maintenance_from")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime maintenanceFrom;
    @JsonProperty("maintenance_status")
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    @Builder.Default
    protected boolean maintenanceInEffect = true;
    @JsonProperty("maintenance_type")
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    protected boolean maintenanceWithoutDataCollection;
    @JsonProperty("maintenanceid")
    protected String maintenanceId;
    protected String name;
    @JsonProperty("proxy_hostid")
    protected String proxyHostId;
    @JsonProperty("snmp_available")
    @Builder.Default
    protected Availabity snmpAvailable = Availabity.UNKNOWN;
    @JsonProperty("snmp_disable_until")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime snmpDisableUntil;
    @JsonProperty("snmp_error")
    protected String snmpError;
    @JsonProperty("snmp_errors_from")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime snmpErrorsFrom;
    @JsonProperty("status")
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    protected boolean unmonitored;
    @JsonProperty("tls_connect")
    @Builder.Default
    protected TLSEncryption tlsConnect = TLSEncryption.NO_ENCRYPTION;
    @JsonProperty("tls_accept")
    @Builder.Default
    protected TLSEncryption tlsAccept = TLSEncryption.NO_ENCRYPTION;
    @JsonProperty("tls_issuer")
    protected String tlsIssuer;
    @JsonProperty("tls_subject")
    protected String tlsSubject;
    @JsonProperty("tls_psk_identity")
    protected String tlsPskIdentity;
    @JsonProperty("tls_psk")
    protected String tlsPsk;

    @Singular
    private List<ZabbixHostGroup> groups;
    @Singular
    private List<ZabbixHostInterface> interfaces;
    @JsonProperty("templates")
    @JsonAlias("parentTemplates")
    @Singular
    private List<ZabbixTemplate> templates;
    @Singular
    private List<HostMacro> macros;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("inventory")
    private Map<HostInventoryProperty, String> inventory;
    @JsonProperty("templates_clear")
    @Singular("templateToClear")
    private List<ZabbixTemplate> templatesToClear;
}
