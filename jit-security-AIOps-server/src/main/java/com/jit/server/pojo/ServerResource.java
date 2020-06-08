package com.jit.server.pojo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="jit_server_resource")
public class ServerResource {
    @Id
    private long  serverId;

    private String serverName;

    private String serverInnerIp;

    private String serverOuterIp;

    private int serverType;
    @Lob
    @Column(columnDefinition="text")
    private String serverUse;

    private long regionId;

    private String regionName;

    private Integer hasOta;

    private Integer hasCw;

    private String otaPort;

    private String cwPort;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date commonDate;

    public long  getServerId() {
        return serverId;
    }

    public void setServerId(long  serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerInnerIp() {
        return serverInnerIp;
    }

    public void setServerInnerIp(String serverInnerIp) {
        this.serverInnerIp = serverInnerIp;
    }

    public String getServerOuterIp() {
        return serverOuterIp;
    }

    public void setServerOuterIp(String serverOuterIp) {
        this.serverOuterIp = serverOuterIp;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getServerUse() {
        return serverUse;
    }

    public void setServerUse(String serverUse) {
        this.serverUse = serverUse;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public Integer getHasOta() {
        return hasOta;
    }

    public void setHasOta(Integer hasOta) {
        this.hasOta = hasOta;
    }

    public Integer getHasCw() {
        return hasCw;
    }

    public void setHasCw(Integer hasCw) {
        this.hasCw = hasCw;
    }

    public String getOtaPort() {
        return otaPort;
    }

    public void setOtaPort(String otaPort) {
        this.otaPort = otaPort;
    }

    public String getCwPort() {
        return cwPort;
    }

    public void setCwPort(String cwPort) {
        this.cwPort = cwPort;
    }

    public Date getCommonDate() {
        return commonDate;
    }

    public void setCommonDate(Date commonDate) {
        this.commonDate = commonDate;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
