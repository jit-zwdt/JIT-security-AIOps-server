package com.jit.zabbix.client.model.problem;

import com.jit.zabbix.client.model.IZabbixMethod;

public enum ProblemMethod implements IZabbixMethod {

    ADDDEPENDENCIES("problem.adddependencies"),
    CREATE("problem.create"),
    DELETE("problem.delete"),
    DELETEDEPENDENCIES("problem.deletedependencies"),
    GET("problem.get"),
    UPDATE("problem.update");

    private String value;

    ProblemMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
