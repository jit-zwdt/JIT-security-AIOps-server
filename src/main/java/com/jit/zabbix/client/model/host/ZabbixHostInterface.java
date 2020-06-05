package com.jit.zabbix.client.model.host;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;

/**
 * Zabbix host interface object.
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostinterface/object#host_interface">Host interface</a>
 **/
public class ZabbixHostInterface {
    @JsonProperty("interfaceid")
    private String id;

    private String dns;

    @JsonProperty("hostid")
    private String hostId;

    private String ip;

    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    private boolean main;

    private String port;

    private InterfaceType type;

    @JsonProperty("useip")
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    private boolean useIp;

    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    private boolean bulk;

    @JsonProperty("interfaceid")
    public void setId(String id) {
        this.id = id;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    @JsonProperty("hostid")
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setType(InterfaceType type) {
        this.type = type;
    }

    @JsonProperty("useip")
    public void setUseIp(boolean useIp) {
        this.useIp = useIp;
    }

    public void setBulk(boolean bulk) {
        this.bulk = bulk;
    }

    @Override
    public String toString() {
        return "ZabbixHostInterface(id=" + getId() + ", dns=" + getDns() + ", hostId=" + getHostId() + ", ip=" + getIp() + ", main=" + isMain() + ", port=" + getPort() + ", type=" + getType() + ", useIp=" + isUseIp() + ", bulk=" + isBulk() + ")";
    }

    public ZabbixHostInterface() {
        this.bulk = defaultBulk();
    }

    public ZabbixHostInterface(String id, String dns, String hostId, String ip, boolean main, String port, InterfaceType type, boolean useIp, boolean bulk) {
        this.id = id;
        this.dns = dns;
        this.hostId = hostId;
        this.ip = ip;
        this.main = main;
        this.port = port;
        this.type = type;
        this.useIp = useIp;
        this.bulk = bulk;
    }

    private static boolean defaultBulk() {
        return true;
    }

    public static ZabbixHostInterfaceBuilder builder() {
        return new ZabbixHostInterfaceBuilder();
    }

    public static class ZabbixHostInterfaceBuilder {
        private String id;

        private String dns;

        private String hostId;

        private String ip;

        private boolean main;

        private String port;

        private InterfaceType type;

        private boolean useIp;

        private boolean bulk$set;

        private boolean bulk;

        @JsonProperty("interfaceid")
        public ZabbixHostInterfaceBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ZabbixHostInterfaceBuilder dns(String dns) {
            this.dns = dns;
            return this;
        }

        @JsonProperty("hostid")
        public ZabbixHostInterfaceBuilder hostId(String hostId) {
            this.hostId = hostId;
            return this;
        }

        public ZabbixHostInterfaceBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public ZabbixHostInterfaceBuilder main(boolean main) {
            this.main = main;
            return this;
        }

        public ZabbixHostInterfaceBuilder port(String port) {
            this.port = port;
            return this;
        }

        public ZabbixHostInterfaceBuilder type(InterfaceType type) {
            this.type = type;
            return this;
        }

        @JsonProperty("useip")
        public ZabbixHostInterfaceBuilder useIp(boolean useIp) {
            this.useIp = useIp;
            return this;
        }

        public ZabbixHostInterfaceBuilder bulk(boolean bulk) {
            this.bulk = bulk;
            this.bulk$set = true;
            return this;
        }

        public ZabbixHostInterface build() {
            boolean bulk = this.bulk;
            if (!this.bulk$set) {
                bulk = ZabbixHostInterface.defaultBulk();
            }
            return new ZabbixHostInterface(this.id, this.dns, this.hostId, this.ip, this.main, this.port, this.type, this.useIp, bulk);
        }

        @Override
        public String toString() {
            return "ZabbixHostInterface.ZabbixHostInterfaceBuilder(id=" + this.id + ", dns=" + this.dns + ", hostId=" + this.hostId + ", ip=" + this.ip + ", main=" + this.main + ", port=" + this.port + ", type=" + this.type + ", useIp=" + this.useIp + ", bulk=" + this.bulk + ")";
        }
    }

    public String getId() {
        return this.id;
    }

    public String getDns() {
        return this.dns;
    }

    public String getHostId() {
        return this.hostId;
    }

    public String getIp() {
        return this.ip;
    }

    public boolean isMain() {
        return this.main;
    }

    public String getPort() {
        return this.port;
    }

    public InterfaceType getType() {
        return this.type;
    }

    public boolean isUseIp() {
        return this.useIp;
    }

    public boolean isBulk() {
        return this.bulk;
    }

}
