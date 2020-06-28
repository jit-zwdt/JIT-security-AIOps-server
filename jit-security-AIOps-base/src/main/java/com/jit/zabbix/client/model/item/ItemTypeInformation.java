package com.jit.zabbix.client.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of the item
 * <p>
 * Possible values:
 * 0 - numeric float;
 * 1 - character;
 * 2 - log;
 * 3 - numeric unsigned;
 * 4 - text.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/object#item">Item object</a>
 **/
public enum ItemTypeInformation {

    NUMERIC_FLOAT(0),
    CHARACTER(1),
    LOG(2),
    NUMERIC_UNSIGNED(3),
    TEXT(4);

    private int value;

    ItemTypeInformation(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ItemTypeInformation fromValue(int value) {
        for (ItemTypeInformation enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
