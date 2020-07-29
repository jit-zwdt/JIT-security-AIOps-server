package com.jit.server.service;

import com.jit.server.request.ProblemParams;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;

import java.util.List;

public interface ProblemService {
    public List<ZabbixProblemDTO> findByCondition(ProblemParams params) throws Exception;
}
