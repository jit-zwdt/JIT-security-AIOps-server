package com.jit.server.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.repository.HostRepo;
import com.jit.server.service.HomePageService;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.model.problem.ProblemSeverity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    private HostRepo hostRepo;

    @Override
    public List<Object> getHostByType() throws Exception {
        return hostRepo.getHostByType();
    }

    /**
     * 根据查询的所有的主机信息进行状态的构建统计
     * @param problemHosts 主机信息
     * @return 统计信息
     */
    @Override
    public JSONObject getStatisticalJson(List<ProblemHostDTO> problemHosts) {
        //创建 JSONObject 对象
        JSONObject jsonObject = new JSONObject();
        //未定义信息统计
        int not_classified = 0;
        //信息信息统计
        int information = 0;
        //警告信息统计
        int warning = 0;
        //一般严重信息统计
        int average = 0;
        //严重信息统计
        int high = 0;
        //灾难信息统计
        int disaster = 0;
        // 首先遍历数据集合
        for(int i = 0 ; i < problemHosts.size() ; i++){
            //获取 problemHost 对象
            ProblemHostDTO problemHost = problemHosts.get(i);
            //获取里面的真实数据
            ZabbixProblemDTO zabbixProblem = problemHost.getZabbixProblemDTO();
            //进行数据的桶位相加
            if(zabbixProblem.getSeverity() == ProblemSeverity.NOT_CLASSIFIED){
                //未定义
                not_classified++;
            }else if(zabbixProblem.getSeverity() == ProblemSeverity.INFORMATION){
                //信息
                information++;
            }else if(zabbixProblem.getSeverity() == ProblemSeverity.WARNING){
                //警告
                warning++;
            }else if(zabbixProblem.getSeverity() == ProblemSeverity.AVERAGE){
                //一般严重
                average++;
            }else if(zabbixProblem.getSeverity() == ProblemSeverity.HIGH){
                //严重
                high++;
            }else if(zabbixProblem.getSeverity() == ProblemSeverity.DISASTER){
                //灾难
                disaster++;
            }
        }
        //未定义信息统计添加
//        jsonObject.put("not_classified" , not_classified);
        //信息信息统计添加
        jsonObject.put("information" , information);
        //警告信息统计添加
        jsonObject.put("warning" , warning);
        //一般严重信息统计添加
        jsonObject.put("average" , average);
        //严重信息统计添加
        jsonObject.put("high" , high);
        //灾难信息统计添加
        jsonObject.put("disaster" , disaster);
        //返回
        return jsonObject;
    }
}
