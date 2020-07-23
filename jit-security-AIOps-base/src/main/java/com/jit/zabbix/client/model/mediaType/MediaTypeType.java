package com.jit.zabbix.client.model.mediaType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Transport used by the media type.
 * Possible values:
 * 0 - e-mail;
 * 1 - script;
 * 2 - SMS;
 * 3 - Jabber;
 * 4 - Webhook;
 * 100 - Ez Texting. 媒介类型的传输方式
 * 可能的值：
 * 0-电子邮件 1-脚本 2-SMS 3-Jabber 100-Ez Texting。
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/mediatype/object#mediatype">mediatype object</a>
 **/
public enum MediaTypeType {

    EMAIL(0),
    SCRIPT(1),
    SMS(2),
    JABBER(3),
    WEBHOOK(4);

    private int value;

    MediaTypeType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static MediaTypeType fromValue(int value) {
        for (MediaTypeType enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
