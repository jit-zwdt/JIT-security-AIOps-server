package com.jit.server.service;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.dto.ProblemHostDTO;

import java.util.List;

public interface HomePageService {

    List<Object> getHostByType() throws Exception;

    /**
     * 根据查询的所有的主机信息进行状态的构建统计
     * @param problemHosts 主机信息
     * @return 统计信息
     */
    JSONObject getStatisticalJson(List<ProblemHostDTO> problemHosts);
}
