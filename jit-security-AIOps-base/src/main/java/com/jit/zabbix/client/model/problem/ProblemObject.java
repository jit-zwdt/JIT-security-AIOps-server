package com.jit.zabbix.client.model.problem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProblemObject {

    TRIGGER_EVENT(0),
    ITEM_EVENT(4),
    LLD_RULE_EVENT(5);

    private int value;

    ProblemObject(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public ProblemObject formValue(int value) {
        for(ProblemObject enumValue : values()) {
            if(enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
