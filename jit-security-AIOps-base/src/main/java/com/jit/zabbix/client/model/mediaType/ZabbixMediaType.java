package com.jit.zabbix.client.model.mediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.jit.zabbix.client.utils.CustomJsonSerializer.BooleanNumericDeserializer;
import static com.jit.zabbix.client.utils.CustomJsonSerializer.BooleanNumericSerializer;

/**
 * Zabbix MediaType object.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/mediatype/object#mediatype">mediatype object</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixMediaType {
    @JsonProperty("mediatypeid")
    private String id;
    private MediaTypeType type;
    private String name;
    @JsonProperty("smtp_server")
    private String smtpServer;
    @JsonProperty("smtp_helo")
    private String smtpHelo;
    @JsonProperty("smtp_email")
    private String smtpEmail;
    @JsonProperty("exec_path")
    private String execPath;
    @JsonProperty("gsm_modem")
    private String gsmModem;
    private String username;
    private String passwd;
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    private boolean status;
    @JsonProperty("smtp_port")
    private int smtpPort;
    @JsonProperty("smtp_security")
    private MediaTypeSmtpSecurity smtpSecurity;
    @JsonProperty("smtp_verify_peer")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    private boolean smtpVerifyPeer;
    @JsonProperty("smtp_verify_host")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    private boolean smtpVerifyHost;
    @JsonProperty("smtp_authentication")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    private boolean smtpAuthentication;
    @JsonProperty("exec_params")
    private String execParams;
    private int maxsessions;
    private int maxattempts;
    @JsonProperty("attempt_interval")
    private String attemptInterval;
    @JsonProperty("content_type")
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    @Builder.Default
    private boolean contentType = true;
    private String script;
    private String timeout;
    @JsonProperty("processTags")
    private int process_tags;
    @JsonProperty("show_event_menu")
    private int showEventMenu;
    @JsonProperty("event_menu_url")
    private String eventMenuUrl;
    @JsonProperty("event_menu_name")
    private String eventMenuName;
    private String description;
}
