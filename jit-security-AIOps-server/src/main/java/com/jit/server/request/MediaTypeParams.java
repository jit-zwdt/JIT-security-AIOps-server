package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: MediaTypeParams
 * @Author: zengxin_miao
 * @Date: 2020.07.22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaTypeParams {
    private String id;
    private int type;
    private String name;
    private String smtpServer;
    private String smtpHelo;
    private String smtpEmail;
    private String execPath;
    private String gsmModem;
    private String username;
    private String passwd;
    private boolean status;
    private int smtpPort;
    private int smtpSecurity;
    private boolean smtpVerifyPeer;
    private boolean smtpVerifyHost;
    private int smtpAuthentication;
    private String execParams;
    private int maxsessions;
    private int maxattempts;
    private String attemptInterval;
    private int contentType;
    private String script;
    private String timeout;
    private int process_tags;
    private int showEventMenu;
    private String eventMenuUrl;
    private String eventMenuName;
    private String description;
    private boolean flag;//判断init
}
