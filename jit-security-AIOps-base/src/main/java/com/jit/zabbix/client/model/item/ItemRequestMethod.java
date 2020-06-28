package com.jit.zabbix.client.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * HTTP agent item field. Type of request method.
 * <p>
 * 0 - GET
 * 1 - (default) POST
 * 2 - PUT
 * 3 - HEAD
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/object#item">Item object</a>
 **/
public enum ItemRequestMethod {

    GET(0),
    POST(1),
    PUT(2),
    HEAD(3);

    private int value;

    ItemRequestMethod(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ItemRequestMethod fromValue(int value) {
        for (ItemRequestMethod enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
