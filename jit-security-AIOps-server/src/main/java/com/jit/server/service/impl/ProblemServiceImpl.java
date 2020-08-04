package com.jit.server.service.impl;

import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.repository.HostRepo;
import com.jit.server.repository.MonitorClaimRepo;
import com.jit.server.request.ProblemClaimParams;
import com.jit.server.request.ProblemParams;
import com.jit.server.service.ProblemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
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
    private MonitorClaimRepo monitorClaimRepo;

    @Autowired
    private HostRepo hostRepo;

    @Autowired
    private ZabbixProblemService zabbixProblemService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ZabbixTriggerService zabbixTriggerService;

    @Override
    public List<ZabbixProblemDTO> findByCondition(ProblemParams params) throws Exception {
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();
        if (params.getSeverity() != null) {
            Map mapFilter = new HashMap();
            mapFilter.put("severity", params.getSeverity());
            params_pro.setFilter(mapFilter);

        }
        if (params.getTimeFrom() != null) {
            params_pro.setTime_from(params.getTimeFrom());
        }
        if (params.getTimeTill() != null) {
            params_pro.setTime_till(params.getTimeTill());
        }

        return zabbixProblemService.get(params_pro, authToken);
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
                List<ZabbixTriggerDTO> trigger = zabbixTriggerService.get(_params, authToken);
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
                    problemHosts.add(temp);
                }
            }

            return problemHosts;
        }
    }

    @Override
    public List<ZabbixProblemDTO> findBySeverityLevel(ProblemClaimParams params) throws Exception {
        List<ZabbixProblemDTO> list = new ArrayList<>();
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        for(Integer integer:params.getSeverities()){
            ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();
            if (integer != null) {
                Map mapFilter = new HashMap();
                mapFilter.put("severity",integer);
                params_pro.setFilter(mapFilter);
                List<ZabbixProblemDTO> listZ = zabbixProblemService.get(params_pro, authToken);
                for(ZabbixProblemDTO zabbixProblemDTO:listZ) {
                    MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                    if (temp != null) {
                        if (zabbixProblemDTO.getId().equals(temp.getProblemId())) {
                            zabbixProblemDTO.setIsClaim(temp.getIsClaim());
                        }
                    }
                }
                list.addAll(listZ);
            }
        }
        return list;

    }

//    @Override
//    public List<ZabbixProblemDTO> findBySeverityLevel(ProblemClaimParams params) throws Exception {
//        List<ZabbixProblemDTO> list = new ArrayList<>();
//        String authToken = zabbixAuthService.getAuth();
//        if (StringUtils.isEmpty(authToken)) {
//            return null;
//        }
//
//        List<Object> listHostRepo = hostRepo.getHostIdsAndIp();
//        List<ProblemHostDTO> problemHostDTOS = new ArrayList<>();
//        List<Integer> integerList = new ArrayList<>();
//        for (int i = 0, len = listHostRepo.size(); i < len; i++) {
//            ProblemHostDTO problemHostDTO = new ProblemHostDTO();
//            Object[] objs = (Object[]) listHostRepo.get(i);
//            problemHostDTO.setHostId(objs[0].toString());
//            integerList.add(Integer.parseInt(objs[0].toString()));
//            problemHostDTO.setHostName(objs[1].toString());
//            problemHostDTO.setIp(objs[2].toString());
//            problemHostDTOS.add(problemHostDTO);
//        }
//        ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();
//        Map mapFilter = new HashMap();
//        if (params.getSeverities() != null) {
//            mapFilter.put("severity", params.getSeverities());
//        }
//        if (listHostRepo != null) {
//            mapFilter.put("hostids", integerList);
//        }
//        List<String> ll = new ArrayList<>();
//        ll.add("severity");
//        params_pro.setSortFields(ll);
//        List<String> ss = new ArrayList<>();
//        ss.add("DESC");
//        params_pro.setSortOrder(ss);
//        list = zabbixProblemService.get(params_pro, authToken);
//        for(ZabbixProblemDTO zabbixProblemDTO:list){
//            MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
//            if(temp!= null){
//                if(zabbixProblemDTO.getId().equals(temp.getProblemId())){
//                    zabbixProblemDTO.setIsClaim(temp.getIsClaim());
//                }
//            }
//        }
//        return list;
//
//    }

    @Override
    public void addCalim(MonitorClaimEntity monitorClaimEntity) throws Exception {
        monitorClaimRepo.save(monitorClaimEntity);
    }
}
