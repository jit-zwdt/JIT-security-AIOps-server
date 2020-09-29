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

    public String addHost(HostEntity host,HttpServletRequest req) throws Exception;

    public String deleteHost(HostEntity host, HttpServletRequest req) throws Exception;

    public Optional<HostEntity> findByHostId(String id) throws Exception;

    public String updateHost(HostEntity host,HttpServletRequest req) throws Exception;

    public String updateHostEnableMonitor(HostEntity host,HttpServletRequest req) throws Exception;

    public Page<Object> hostinfo(HostParams params, int page, int size) throws Exception;

    public List<ZabbixHostDTO> getHostAvailableFromZabbix(List<String> hostIds,HttpServletRequest req) throws Exception;

    public List<ZabbixHostGroupDTO> findHostGroupByTypeId(Map<String, Object> params,HttpServletRequest req) throws Exception;

    public List<Map<String, String>> getTop5ByItem(Map<String, Object> params,HttpServletRequest req) throws Exception;

    public List<Map<String, String>> getTop5ByTrigger(Map<String, Object> params,HttpServletRequest req) throws Exception;

    public HostEntity findHostIdinfo(String id) throws Exception;

    List<HostEntity> getHosts() throws Exception;

    List<Object> getHostIds() throws Exception;

    Boolean checkObjectName(String objectName, String odlObjectName) throws Exception;

    Boolean checkBusinessName(String businessName, String odlBusinessName);
}
