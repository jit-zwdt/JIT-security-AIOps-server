package com.jit.zabbix.client.model.problem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jit.zabbix.client.model.trigger.TriggerPriority;

public enum ProblemSeverity {

    NOT_CLASSIFIED(0),
    INFORMATION(1),
    WARNING(2),
    AVERAGE(3),
    HIGH(4),
    DISASTER(5);

    private int value;

    ProblemSeverity(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ProblemSeverity fromValue(int value) {
        for (ProblemSeverity enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
