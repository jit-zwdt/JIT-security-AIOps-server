package com.jit.server.service;

import com.alibaba.fastjson.JSONArray;
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

    /**
     * 根据所有的问题信息进行常见问题信息 Top5 排名信息的构建
     * @param problemHosts 所有的问题信息
     * @return 统计信息
     */
    JSONArray getFAQJson(List<ProblemHostDTO> problemHosts);

    /**
     * 根据所有的问题信息进行设备异常服务器 TOP10 排名信息的构建
     * @param problemHosts 所有的问题信息
     * @return 统计信息
     */
    JSONArray getHostErrorJson(List<ProblemHostDTO> problemHosts);

    /**
     * 根据返回的条数数据进行数据的拼接 Json 串
     * @param sumResult 查询的数据
     * @return 统计信息
     */
    JSONObject getAssetsSumJson(List<Object[]> sumResult);
}
