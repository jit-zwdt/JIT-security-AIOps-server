package com.jit.zabbix.client.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * HTTP agent item field. Type of post data body stored in posts property.
 * <p>
 * 0 - (default) Raw data.
 * 2 - JSON data.
 * 3 - XML data.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/object#item">Item object</a>
 **/
public enum ItemPostType {

    RAW_DATA(0),
    JSON_DATA(2),
    XML_DATA(3);

    private int value;

    ItemPostType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ItemPostType fromValue(int value) {
        for (ItemPostType enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
