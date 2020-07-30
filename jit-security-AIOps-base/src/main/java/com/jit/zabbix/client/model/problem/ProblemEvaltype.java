package com.jit.zabbix.client.model.problem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Rules for tag searching.
 * <p>
 * Possible values:
 * 0 - (default) And/Or;
 * 2 - Or. 规则标签搜索。
 * \\可能的值：
 * 0 - (默认)与/或 ；2 - 或
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/problem/get">problem.get</a>
 **/
public enum ProblemEvaltype {

    AND_OR(0),
    OR(2);

    private int value;

    ProblemEvaltype(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public ProblemEvaltype formValue(int value) {
        for (ProblemEvaltype enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
