package com.jit.zabbix.client.model.mediaType;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for Media type.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/mediatype/">mediatype</a>
 **/
public enum MediaTypeMethod implements IZabbixMethod {

    CREATE("mediatype.create"),
    DELETE("mediatype.delete"),
    GET("mediatype.get"),
    UPDATE("mediatype.update");

    private String value;

    MediaTypeMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
