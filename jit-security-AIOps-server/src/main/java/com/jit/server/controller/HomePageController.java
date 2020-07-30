package com.jit.server.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.service.HomePageService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.model.problem.ProblemSeverity;
import com.jit.zabbix.client.request.ZabbixGetProblemParams;
import com.jit.zabbix.client.service.ZabbixProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/07/30 15:46
 */
@RestController
@RequestMapping("/homePage")
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private ZabbixProblemService zabbixProblemService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;


    @ResponseBody
    @PostMapping(value = "/getMonitorTypeUsedInfo")
    public Result getMonitorTypeUsedInfo(HttpServletRequest req) {
        try {
            List<Object> objectList = homePageService.getHostByType();
            JSONArray jsonArray = new JSONArray();
            if (objectList != null && !objectList.isEmpty()) {
                String hostId = "";
                String type = "";
                String typeId = "";
                List<String> types = new ArrayList<>();
                List<String> typeIds = new ArrayList<>();
                List<String> hostIds = null;
                JSONObject jsonObject;
                for (int i = 0, len = objectList.size(); i < len; i++) {
                    Object[] objs = (Object[]) objectList.get(i);
                    hostId = objs[0].toString();
                    type = objs[1].toString();
                    typeId = objs[2].toString();
                    if (typeIds.contains(typeId)) {
                        hostIds.add(hostId);
                    } else {
                        types.add(type);
                        typeIds.add(typeId);
                        if (i > 0) {//exclude hostIds null
                            jsonObject = new JSONObject(5);
                            jsonObject.put("type", types.get(typeIds.indexOf(typeId) - 1));
                            jsonObject.put("typeId", typeIds.get(typeIds.indexOf(typeId) - 1));
                            jsonObject.put("hostIds", hostIds);
                            jsonObject.put("hostCount", hostIds.size());
                            jsonObject.put("problemsSeverityCount", countProblemsGroupBySeverity(hostIds));
                            jsonArray.add(jsonObject);
                        }
                        hostIds = new ArrayList<>();
                        hostIds.add(hostId);
                    }
                }
                if (!hostIds.isEmpty()) {// the last param
                    jsonObject = new JSONObject();
                    jsonObject.put("type", types.get(typeIds.indexOf(typeId) - 1));
                    jsonObject.put("typeId", typeIds.get(typeIds.indexOf(typeId) - 1));
                    jsonObject.put("hostIds", hostIds);
                    jsonObject.put("hostCount", hostIds.size());
                    jsonObject.put("problemsSeverityCount", countProblemsGroupBySeverity(hostIds));
                    jsonArray.add(jsonObject);
                }
            }
            return Result.SUCCESS(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    private Map<String, Integer> countProblemsGroupBySeverity(List<String> hostIds) throws Exception {
        Map<String, Integer> res = new HashMap<>();
        res.put("0", 0);
        res.put("1", 0);
        res.put("2", 0);
        res.put("3", 0);
        res.put("4", 0);
        res.put("5", 0);
        if (hostIds != null && !hostIds.isEmpty()) {
            String auth = zabbixAuthService.getAuth();
            ZabbixGetProblemParams zabbixGetProblemParams = new ZabbixGetProblemParams();
            zabbixGetProblemParams.setHostids(hostIds);
            zabbixGetProblemParams.setOutput("extend");
            List<ZabbixProblemDTO> zabbixProblemDTOList = zabbixProblemService.get(zabbixGetProblemParams, auth);
            if (zabbixProblemDTOList != null && !zabbixProblemDTOList.isEmpty()) {
                int notClassifiedCount = 0;
                int informationCount = 0;
                int warningCount = 0;
                int average = 0;
                int high = 0;
                int disaster = 0;
                for (ZabbixProblemDTO zabbixProblemDTO : zabbixProblemDTOList) {
                    if (ProblemSeverity.NOT_CLASSIFIED.equals(zabbixProblemDTO.getSeverity())) {
                        ++notClassifiedCount;
                    }
                    if (ProblemSeverity.INFORMATION.equals(zabbixProblemDTO.getSeverity())) {
                        ++informationCount;
                    }
                    if (ProblemSeverity.WARNING.equals(zabbixProblemDTO.getSeverity())) {
                        ++warningCount;
                    }
                    if (ProblemSeverity.AVERAGE.equals(zabbixProblemDTO.getSeverity())) {
                        ++average;
                    }
                    if (ProblemSeverity.HIGH.equals(zabbixProblemDTO.getSeverity())) {
                        ++high;
                    }
                    if (ProblemSeverity.DISASTER.equals(zabbixProblemDTO.getSeverity())) {
                        ++disaster;
                    }
                }
                res.put("0", notClassifiedCount);
                res.put("1", informationCount);
                res.put("2", warningCount);
                res.put("3", average);
                res.put("4", high);
                res.put("5", disaster);
            }
            return res;
        } else {
            return res;
        }
    }

}
