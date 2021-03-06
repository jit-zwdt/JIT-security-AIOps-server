package com.jit.zabbix.client.model.host;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import static com.jit.zabbix.client.utils.CustomJsonSerializer.BooleanNumericDeserializer;
import static com.jit.zabbix.client.utils.CustomJsonSerializer.BooleanNumericSerializer;

/**
 * Zabbix Host object.
 *
 *@see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/object#host">Host object</a>
 * @author Mamadou Lamine NIANG
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixHost {

    @JsonProperty("hostid")
    protected String id;
    @JsonProperty("host")
    protected String technicalName;
    @Builder.Default
    protected Availabity available = Availabity.UNKNOWN;
    protected String description;
    @JsonProperty("disable_until")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime disableUntil;
    protected String error;
    @JsonProperty("errors_from")
    @JsonFormat(shape = Shape.NUMBER_INT)
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
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime ipmiDisableUntil;
    @JsonProperty("ipmi_error")
    protected String ipmiError;
    @JsonProperty("ipmi_errors_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
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
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime jmxDisableUntil;
    @JsonProperty("jmx_error")
    protected String jmxError;
    @JsonProperty("jmx_errors_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime jmxErrorsFrom;
    @JsonProperty("maintenance_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime maintenanceFrom;
    @JsonProperty("maintenance_status")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    @Builder.Default
    protected boolean maintenanceInEffect = true;
    @JsonProperty("maintenance_type")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
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
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime snmpDisableUntil;
    @JsonProperty("snmp_error")
    protected String snmpError;
    @JsonProperty("snmp_errors_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime snmpErrorsFrom;
    @JsonProperty("status")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    protected boolean unmonitored;
    @JsonProperty("tls_connect")
    @Builder.Default
    protected TLSEncryption tlsConnect = TLSEncryption.NO_ENCRYPTION;
    @JsonProperty("tls_accept")
    @Builder.Default
    protected TLSEncryption tlsAccept= TLSEncryption.NO_ENCRYPTION;
    @JsonProperty("tls_issuer")
    protected String tlsIssuer;
    @JsonProperty("tls_subject")
    protected String tlsSubject;
    @JsonProperty("tls_psk_identity")
    protected String tlsPskIdentity;
    @JsonProperty("tls_psk")
    protected String tlsPsk;
}
