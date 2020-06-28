package com.jit.zabbix.client.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The preprocessing option type.
 * <p>
 * Possible values:
 * 1 - Custom multiplier;
 * 2 - Right trim;
 * 3 - Left trim;
 * 4 - Trim;
 * 5 - Regular expression matching;
 * 6 - Boolean to decimal;
 * 7 - Octal to decimal;
 * 8 - Hexadecimal to decimal;
 * 9 - Simple change;
 * 10 - Change per second.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/object#item">Item object</a>
 **/
public enum ItemPreprocessingType {

    CUSTOM_MULTIPLIER(1),
    RIGHT_TRIM(2),
    LEFT_TRIM(3),
    TRIM(4),
    REGULAR_EXPRESSION_MATCHING(5),
    BOOLEAN_TO_DECIMAL(6),
    OCTAL_TO_DECIMAL(7),
    HEXADECIMAL_TO_DECIMAL(8),
    SIMPLE_CHANGE(9),
    CHANGE_PER_SECOND(10);

    private int value;

    ItemPreprocessingType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ItemPreprocessingType fromValue(int value) {
        for (ItemPreprocessingType enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
