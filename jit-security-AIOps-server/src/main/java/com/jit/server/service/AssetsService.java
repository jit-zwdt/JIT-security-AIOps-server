package com.jit.server.service;

import com.jit.server.pojo.AssetsEntity;
import com.jit.server.request.AssetsParams;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface AssetsService {
    public Page<AssetsEntity> findByCondition(AssetsParams params, int page, int size) throws Exception;
}
