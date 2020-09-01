package com.jit.server.service;

import com.jit.server.pojo.MonitorAssetsEntity;
import com.jit.server.request.AssetsParams;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AssetsService {
    public Page<MonitorAssetsEntity> findByCondition(AssetsParams params, int page, int size) throws Exception;
    public void addAssets(MonitorAssetsEntity assets) throws Exception;
    public void deleteAssets(String id) throws Exception;
    public Optional<MonitorAssetsEntity> findByAssetsId(String id)  throws Exception;
    public void updateAssets(MonitorAssetsEntity assets) throws Exception;
    public List<MonitorAssetsEntity> findByConditionInfo() throws Exception;
}
