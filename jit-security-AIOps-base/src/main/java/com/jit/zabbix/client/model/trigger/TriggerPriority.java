package com.jit.zabbix.client.model.trigger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Severity of the trigger prototype. 触发器原型的严重级别。
 * Possible values: 许可值：
 * 0 - (default 默认) not classified; 未分类；
 * 1 - information; 信息；
 * 2 - warning; 警告；
 * 3 - average; 一般严重；
 * 4 - high; 严重；
 * 5 - disaster. 灾难。
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/triggerprototype/object#triggerprototype">Trigger prototype object</a>
 **/
public enum TriggerPriority {

    NOT_CLASSIFIED(0),
    INFORMATION(1),
    WARNING(2),
    AVERAGE(3),
    HIGH(4),
    DISASTER(5);

    private int value;

    TriggerPriority(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static TriggerPriority fromValue(int value) {
        for (TriggerPriority enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
