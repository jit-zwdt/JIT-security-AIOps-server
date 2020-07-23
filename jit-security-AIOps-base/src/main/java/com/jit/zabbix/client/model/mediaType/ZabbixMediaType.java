package com.jit.zabbix.client.model.mediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
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
    private String name;
    private String description;
    private MediaTypeType type;
    @JsonProperty("exec_path")
    private String execPath;
    @JsonProperty("gsm_modem")
    private String gsmModem;
    private String passwd;
    @JsonProperty("smtp_email")
    private String smtpEmail;
    @JsonProperty("smtp_helo")
    private String smtpHelo;
    @JsonProperty("smtp_server")
    private String smtpServer;
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    private boolean status;
    private String username;
    @JsonProperty("exec_params")
    private String execParams;
    private int maxsessions;
    private int maxattempts;
    @JsonProperty("attempt_interval")
    private String attemptInterval;
}
