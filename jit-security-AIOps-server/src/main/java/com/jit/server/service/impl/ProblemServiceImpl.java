package com.jit.server.service.impl;

import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.request.ProblemParams;
import com.jit.server.service.ProblemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.problem.ProblemSeverity;
import com.jit.zabbix.client.model.problem.ZabbixProblem;
import com.jit.zabbix.client.model.trigger.ZabbixTrigger;
import com.jit.zabbix.client.request.ZabbixGetProblemParams;
import com.jit.zabbix.client.request.ZabbixGetTriggerParams;
import com.jit.zabbix.client.service.ZabbixProblemService;
import com.jit.zabbix.client.service.ZabbixTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProblemServiceImpl implements ProblemService {

    public static final String EXTEND = "extend";

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ZabbixProblemService zabbixProblemService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ZabbixTriggerService zabbixTriggerService;

    @Override
    public List<ZabbixProblemDTO> findByCondition(ProblemParams params) throws Exception {
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)) {
            return null;
        }

        ProblemSeverity severity = ProblemSeverity.fromValue(params.getSeverity());
        ZabbixGetProblemParams _params = new ZabbixGetProblemParams();
        if(severity != null) {
            _params.setSeverity(severity);
        }

        return zabbixProblemService.get(_params, authToken);
    }

    @Override
    public List<ProblemHostDTO> findProblemHost(ProblemParams params) throws Exception {
        // get token
        String authToken = zabbixAuthService.getAuth();
        if(StringUtils.isEmpty(authToken)) {
            return null;
        }

        List<ProblemHostDTO> problemHosts = new ArrayList<>();
        ZabbixGetTriggerParams _params = new ZabbixGetTriggerParams();
        _params.setSelectFunctions(EXTEND);
        _params.setOutput(EXTEND);
        _params.setSelectHosts(EXTEND);

        // get problems
        List<ZabbixProblemDTO> problems = problemService.findByCondition(params);
        if(problems == null || problems.size() == 0) {
            return null;
        }

        // for each problem, find a trigger and register a ProblemHost object
        else{
            for(ZabbixProblemDTO problem : problems) {
                ProblemHostDTO temp = new ProblemHostDTO();

                // 封装triggerService 参数
//                _params.setTriggerIds(Arrays.asList(problem.getObjectId()));
                Map<String, Object> filter = new HashMap<>();
                filter.put("triggerid", problem.getObjectId());
                _params.setFilter(filter);

                // 获取trigger
                System.out.println("mark1");
                List<ZabbixTriggerDTO> trigger = zabbixTriggerService.get(_params, authToken);
                System.out.println("mark2");
                if(trigger == null || trigger.size() == 0) {
                    continue;
                }
                else {
                    // 增加到ProblemHostDTO列表
//                    temp.setHostId("test");
//                    temp.setHostName("test");
                    temp.setZabbixProblemDTO(problem);
                    temp.setHostId(trigger.get(0).getZabbixHost().get(0).getId());
                    temp.setHostName(trigger.get(0).getZabbixHost().get(0).getName());
                    System.out.println(trigger.get(0).getZabbixHost().toString());
                    problemHosts.add(temp);
                }
            }

            return problemHosts;
        }
    }
}
