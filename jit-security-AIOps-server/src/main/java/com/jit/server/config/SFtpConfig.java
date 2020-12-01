package com.jit.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 获取配置文件中SFtp片段值转化成SFtpConfig对象
 *
 * @author jian_liu
 */

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "sftp")
public class SFtpConfig {
    private String hostName;
    private int port;
    private String userName;
    private String passWord;
    private String remoteRootPath;
    private int timeOut;
    private int useShow;
}
