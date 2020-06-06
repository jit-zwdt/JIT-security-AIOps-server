package com.jit.zabbix.client.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ZabbixApiProperties}.
 *
 * @author Mamadou Lamine NIANG
 **/
public class ZabbixApiPropertiesTests {

    private final ZabbixApiProperties zabbixApiProperties = new ZabbixApiProperties();

    @Test
    public void trailingSlashIsRemovedFromUrl() {
        zabbixApiProperties.setUrl("https://github.com/");
        assertThat(zabbixApiProperties.getUrl()).isEqualTo("https://github.com");
    }
}
