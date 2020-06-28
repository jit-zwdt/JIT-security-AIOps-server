package com.jit.zabbix.client.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * SNMPv3 security level. Used only by SNMPv3 items.
 * <p>
 * Possible values:
 * 0 - noAuthNoPriv;
 * 1 - authNoPriv;
 * 2 - authPriv.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/object#item">Item object</a>
 **/
public enum ItemSnmpv3Securitylevel {

    NOAUTHNOPRIV(0),
    AUTHNOPRIV(1),
    AUTHPRIV(2);

    private int value;

    ItemSnmpv3Securitylevel(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ItemSnmpv3Securitylevel fromValue(int value) {
        for (ItemSnmpv3Securitylevel enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
