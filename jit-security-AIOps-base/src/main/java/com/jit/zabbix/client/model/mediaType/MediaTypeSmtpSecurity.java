package com.jit.zabbix.client.model.mediaType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Smtp Security.
 * Possible values:
 * 0 - 无;
 * 1 - STARTTLS(纯文本通信协议扩展);
 * 2 - SSL/TLS;
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/mediatype/object#mediatype">mediatype object</a>
 **/
public enum MediaTypeSmtpSecurity {

    NONE(0),
    STARTTLS(1),
    SSL_TLS(2);

    private int value;

    MediaTypeSmtpSecurity(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static MediaTypeSmtpSecurity fromValue(int value) {
        for (MediaTypeSmtpSecurity enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
