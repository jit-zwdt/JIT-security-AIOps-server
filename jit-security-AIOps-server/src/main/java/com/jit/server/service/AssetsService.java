package com.jit.server.service;

import com.jit.server.pojo.AssetsEntity;
import com.jit.server.request.AssetsParams;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AssetsService {
    public Page<AssetsEntity> findByCondition(AssetsParams params, int page, int size) throws Exception;
    public void addAssets(AssetsEntity assets) throws Exception;
    public void deleteAssets(String id) throws Exception;
    public Optional<AssetsEntity> findByAssetsId(String id)  throws Exception;
    public void updateAssets(AssetsEntity assets) throws Exception;
    public List<AssetsEntity> findByConditionInfo() throws Exception;
}
