package com.jit.server.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 0 - (default 默认) not classified; 未分类；
 * 1 - information; 信息；
 * 2 - warning; 警告；
 * 3 - average; 一般严重；
 * 4 - high; 严重；
 * 5 - disaster. 灾难。
 *
 * @author zengxin_miao
 **/
public enum SeverityEnum {

    未分类(0),
    信息(1),
    警告(2),
    一般严重(3),
    严重(4),
    灾难(5);

    private int value;

    SeverityEnum(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static SeverityEnum fromValue(int value) {
        for (SeverityEnum enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }

}
