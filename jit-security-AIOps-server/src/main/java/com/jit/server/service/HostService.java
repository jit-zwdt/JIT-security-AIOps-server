package com.jit.server.service;

import com.jit.server.pojo.HostEntity;
import com.jit.server.request.HostParams;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface HostService {
    public Page<HostEntity> findByCondition(HostParams params, int page, int size) throws Exception;
    public void addHost(HostEntity host) throws Exception;
    public void deleteHost(String id) throws Exception;
    public Optional<HostEntity> findByHostId(String id)  throws Exception;
    public void updateHost(HostEntity host) throws Exception;
    public Page<Object> hostinfo(HostParams params, int page, int size) throws Exception;
}
