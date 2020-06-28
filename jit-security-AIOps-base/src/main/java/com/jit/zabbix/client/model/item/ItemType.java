package com.jit.zabbix.client.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of the item.
 * <p>
 * Possible values:
 * 0 - Zabbix agent;
 * 1 - SNMPv1 agent;
 * 2 - Zabbix trapper;
 * 3 - simple check;
 * 4 - SNMPv2 agent;
 * 5 - Zabbix internal;
 * 6 - SNMPv3 agent;
 * 7 - Zabbix agent (active);
 * 8 - Zabbix aggregate;
 * 9 - web item;
 * 10 - external check;
 * 11 - database monitor;
 * 12 - IPMI agent;
 * 13 - SSH agent;
 * 14 - TELNET agent;
 * 15 - calculated;
 * 16 - JMX agent;
 * 17 - SNMP trap;
 * 18 - Dependent item;
 * 19 - HTTP agent;
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/object#item">Item object</a>
 **/
public enum ItemType {

    ZABBIX_AGENT(0),
    SNMPV1_AGENT(1),
    ZABBIX_TRAPPER(2),
    SIMPLE_CHECK(3),
    SNMPV2_AGENT(4),
    ZABBIX_INTERNAL(5),
    SNMPV3_AGENT(6),
    ZABBIX_AGENT_ACTIVE(7),
    ZABBIX_AGGREGATE(8),
    WEB_ITEM(9),
    EXTERNAL_CHECK(10),
    DATABASE_MONITOR(11),
    IPMI_AGENT(12),
    SSH_AGENT(13),
    TELNET_AGENT(14),
    CALCULATED(15),
    JMX_AGENT(16),
    SNMP_TRAP(17),
    DEPENDENT_ITEM(18),
    HTTP_AGENT(19);

    private int value;

    ItemType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ItemType fromValue(int value) {
        for (ItemType enumValue : values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        return null;
    }
}
