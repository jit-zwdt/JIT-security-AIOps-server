package com.jit.server.service;

import com.jit.server.pojo.MonitorAssetsEntity;
import com.jit.server.request.AssetsParams;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AssetsService {
    Page<MonitorAssetsEntity> findByCondition(AssetsParams params, int page, int size) throws Exception;

    void addAssets(MonitorAssetsEntity assets) throws Exception;

    void deleteAssets(String id) throws Exception;

    Optional<MonitorAssetsEntity> findByAssetsId(String id) throws Exception;

    void updateAssets(MonitorAssetsEntity assets) throws Exception;

    List<MonitorAssetsEntity> findByConditionInfo() throws Exception;

    List<Object> getHardwareInfo() throws Exception;
}
