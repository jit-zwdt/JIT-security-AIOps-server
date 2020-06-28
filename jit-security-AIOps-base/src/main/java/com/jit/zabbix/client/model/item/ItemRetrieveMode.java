package com.jit.zabbix.client.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * HTTP agent item field. What part of response should be stored.
 * <p>
 * 0 - (default) Body.
 * 1 - Headers.
 * 2 - Both body and headers will be stored.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/object#item">Item object</a>
 **/
public enum ItemRetrieveMode {

    BODY(0),
    HEADERS(1),
    BODY_AND_HEADERS(2);

    private int value;

    ItemRetrieveMode(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ItemRetrieveMode fromValue(int value) {
        for (ItemRetrieveMode enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
