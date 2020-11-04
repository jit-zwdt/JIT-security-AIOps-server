package com.jit.server.service;

import com.jit.server.pojo.HostEntity;
import com.jit.server.request.HostParams;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.dto.ZabbixHostGroupDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface HostService {
    public List<HostEntity> findByCondition(HostParams params) throws Exception;

    public String addHost(HostEntity host, String auth) throws Exception;

    public String deleteHost(HostEntity host, String auth) throws Exception;

    public Optional<HostEntity> findByHostId(String id) throws Exception;

    public String updateHost(HostEntity host,String auth) throws Exception;

    public String updateHostEnableMonitor(HostEntity host,String auth) throws Exception;

    public Page<Object> hostinfo(HostParams params, int page, int size) throws Exception;

    public List<ZabbixHostDTO> getHostAvailableFromZabbix(List<String> hostIds, String auth) throws Exception;

    public List<ZabbixHostGroupDTO> findHostGroupByTypeId(Map<String, Object> params, String auth) throws Exception;

    public List<Map<String, String>> getTop5ByItem(Map<String, Object> params,String auth) throws Exception;

    public List<Map<String, String>> getTop5ByTrigger(Map<String, Object> params, String auth) throws Exception;

    public HostEntity findHostIdinfo(String id) throws Exception;

    List<HostEntity> getHosts() throws Exception;

    List<Object> getHostIds() throws Exception;

    Boolean checkObjectName(String objectName, String odlObjectName) throws Exception;

    Boolean checkBusinessName(String businessName, String odlBusinessName);

    /**
     * 根据 JMX IP 查询是否有对应的数据 如果有的话返回 true 没有返回 false
     * @param ip JMX IP
     * @return 有: true 没有: false
     */
    boolean findByHostJmxIp(String ip);
}
