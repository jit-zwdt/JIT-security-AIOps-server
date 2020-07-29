package com.jit.zabbix.client.model.problem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProblemSource {

    TRIGGER_EVENT(0),
    INTERNAL_EVENT(3);

    private int value;

    ProblemSource(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static ProblemSource fromValue(int value) {
        for(ProblemSource enumValue : values()) {
            if(enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
