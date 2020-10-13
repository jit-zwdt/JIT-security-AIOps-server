package com.jit.server.service;

import com.jit.server.pojo.SysCronExpressionEntity;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface SysCronExpressionService {


    Page<SysCronExpressionEntity> getCronExpressions(PageRequest<Map<String, Object>> params);
}
