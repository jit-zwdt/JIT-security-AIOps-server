package com.jit.zabbix.client.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix User object.
 *
 * @author jian_liu
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/user/object#user">User object</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixMediatypes {

    protected String mediatypeid;

    protected String type;

    protected String name;

    protected String smtp_server;

    protected String smtp_helo;

    protected String smtp_email;

    protected String exec_path;

    protected String gsm_modem;

    protected String username;

    protected String passwd;

    protected String status;

    protected String smtp_port;

    protected String smtp_security;

    protected String smtp_verify_peer;

    protected String smtp_verify_host;

    protected String smtp_authentication;

    protected String exec_params;

    protected String maxsessions;

    protected String maxattempts;

    protected String attempt_interval;

    protected String content_type;

    protected Object script;

    protected String timeout;

    protected String process_tags;

    protected String show_event_menu;

    protected String event_menu_url;

    protected String event_menu_name;

    protected String description;

    protected List<String> parameters;

}
