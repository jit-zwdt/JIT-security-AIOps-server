package com.jit.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 获取配置文件中Ftp片段值转化成FtpConfig对象
 *
 * @author zengxin_miao
 */

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "ftp")
public class FtpConfig {
    private String hostName;
    private int port;
    private String userName;
    private String passWord;
}
