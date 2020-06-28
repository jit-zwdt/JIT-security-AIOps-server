package com.jit.zabbix.client.model.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.model.host.OriginFlag;
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
 * Zabbix Item object.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/object#item">Item object</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixItem {

    @JsonProperty("itemid")
    protected String id;

    protected String delay;

    @JsonProperty("hostid")
    protected String hostId;

    @JsonProperty("interfaceid")
    protected String interfaceId;

    protected String key_;

    protected String name;

    private ItemType type;

    protected String url;

    @JsonProperty("value_type")
    protected ItemTypeInformation valueType;

    @JsonProperty("allow_traps")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    protected boolean allowTraps;

    /**
     * SSH agent authentication method possible values:
     * 0 - (default) password;
     * 1 - public key.
     * <p>
     * HTTP agent authentication method possible values:
     * 0 - (default) none
     * 1 - basic
     * 2 - NTLM
     */
    @JsonProperty("authtype")
    protected ItemAuthtype authType;

    protected String description;

    protected String error;

    protected OriginFlag flags;

    @JsonProperty("follow_redirects")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    @Builder.Default
    protected boolean followRedirects = true;

    protected Object headers;

    protected String history;

    @JsonProperty("http_proxy")
    protected String httpProxy;

    @JsonProperty("inventory_link")
    @Builder.Default
    protected int inventoryLink = 0;

    @JsonProperty("ipmi_sensor")
    protected String ipmiSensor;

    @JsonProperty("jmx_endpoint")
    protected String jmxEndpoint;

    @JsonProperty("lastclock")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime lastclock;

    protected int lastns;

    protected String lastvalue;

    @JsonProperty("logtimefmt")
    protected String logtimefmt;

    @JsonProperty("master_itemid")
    protected int masterItemid;

    @JsonProperty("mtime")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime mtime;

    @JsonProperty("output_format")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    protected boolean outputFormat;

    protected String params;

    protected String password;

    protected String port;

    @JsonProperty("post_type")
    @Builder.Default
    protected ItemPostType postType = ItemPostType.RAW_DATA;

    protected String posts;

    @JsonProperty("prevvalue")
    protected String prevValue;

    @JsonProperty("privatekey")
    protected String privateKey;

    @JsonProperty("publickey")
    protected String publicKey;

    @JsonProperty("query_fields")
    protected Object queryFields;

    @JsonProperty("request_method")
    @Builder.Default
    protected ItemRequestMethod requestMethod = ItemRequestMethod.POST;

    @JsonProperty("retrieve_mode")
    @Builder.Default
    protected ItemRetrieveMode retrieveMode = ItemRetrieveMode.BODY;

    @JsonProperty("snmp_community")
    protected String snmpCommunity;

    @JsonProperty("snmp_oid")
    protected String snmpOid;

    @JsonProperty("snmpv3_authpassphrase")
    protected String snmpv3Authpassphrase;

    @JsonProperty("snmpv3_authprotocol")
    protected int snmpv3Authprotocol;

    @JsonProperty("snmpv3_contextname")
    protected String snmpv3Contextname;

    @JsonProperty("snmpv3_privpassphrase")
    protected String snmpv3Privpassphrase;

    @JsonProperty("snmpv3_privprotocol")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    protected boolean snmpv3Privprotocol;

    @JsonProperty("snmpv3_securitylevel")
    protected ItemSnmpv3Securitylevel snmpv3Securitylevel;

    @JsonProperty("snmpv3_securityname")
    protected String snmpv3Securityname;

    @JsonProperty("ssl_cert_file")
    protected String sslCertFile;

    @JsonProperty("ssl_key_file")
    protected String sslKeyFile;

    @JsonProperty("ssl_key_password")
    protected String sslKeyPassword;

    @JsonProperty("state")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    protected boolean state;

    @JsonProperty("status")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    protected boolean status;

    @JsonProperty("status_codes")
    protected String statusCodes;

    @JsonProperty("templateid")
    protected String templateId;

    @JsonProperty("timeout")
    @Builder.Default
    protected String timeout = "3s";

    @JsonProperty("trapper_hosts")
    protected String trapperHosts;

    @JsonProperty("trends")
    @Builder.Default
    protected String trends = "365d";

    @JsonProperty("units")
    protected String units;

    @JsonProperty("username")
    protected String userName;

    @JsonProperty("valuemapid")
    protected String valuemapId;

    @JsonProperty("verify_host")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    protected boolean verifyHost;

    @JsonProperty("verifyPeer")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    protected boolean verifyPeer;

    protected ZabbixItemPreprocessing zabbixItemPreprocessing;

}
