package com.jit.zabbix.client.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Used only by SSH agent items or HTTP agent items.
 * <p>
 * SSH agent authentication method possible values:
 * 0 - (default) password;
 * 1 - public key.
 * <p>
 * HTTP agent authentication method possible values:
 * 0 - (default) none
 * 1 - basic
 * 2 - NTLM
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/object#item">Item object</a>
 **/
public enum ItemAuthtype {

    PASSWORD_OR_NONE(0),
    PUBLIC_KEY_OR_BASIC(1),
    NTLM(2);

    private int value;

    ItemAuthtype(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ItemAuthtype fromValue(int value) {
        for (ItemAuthtype enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
