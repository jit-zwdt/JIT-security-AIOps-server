package com.jit.server.service.impl;

import com.jit.server.dto.ProblemClaimDTO;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.pojo.SysUserEntity;
import com.jit.server.repository.HostRepo;
import com.jit.server.repository.MonitorClaimRepo;
import com.jit.server.repository.SysUserRepo;
import com.jit.server.request.ProblemClaimParams;
import com.jit.server.request.ProblemParams;
import com.jit.server.service.ProblemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.model.problem.ZabbixProblem;
import com.jit.zabbix.client.request.ZabbixGetProblemParams;
import com.jit.zabbix.client.request.ZabbixGetTriggerParams;
import com.jit.zabbix.client.service.ZabbixProblemService;
import com.jit.zabbix.client.service.ZabbixTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProblemServiceImpl implements ProblemService {

    public static final String EXTEND = "extend";

    @Autowired
    private MonitorClaimRepo monitorClaimRepo;

    @Autowired
    private HostRepo hostRepo;

    @Autowired
    private ZabbixProblemService zabbixProblemService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private SysUserRepo sysUserRepo;

    @Override
    public List<ZabbixProblemDTO> findByCondition(ProblemParams params, String authToken) throws Exception {
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();

        // set severity
        if (params.getSeverity() != null) {
            Map<String, Object> mapFilter = new HashMap();
            mapFilter.put("severity", params.getSeverity());
            params_pro.setFilter(mapFilter);
        }

        // set hostId
        if (params.getHostId() != null) {
            List<String> hostIds = new ArrayList<>();
            hostIds.add(params.getHostId());
            params_pro.setHostids(hostIds);
        }

        // set timeFrom
        if (params.getTimeFrom() != null && !params.getTimeFrom().equals("NaN")) {
            params_pro.setTime_from(params.getTimeFrom());
        }

        // set timeTill
        if (params.getTimeTill() != null && !params.getTimeTill().equals("NaN")) {
            params_pro.setTime_till(params.getTimeTill());
        }

        // set name
        if (params.getName() != null && params.getName().length() > 0) {
            Map<String, Object> mapSearch = new HashMap();
            mapSearch.put("name", params.getName());
            params_pro.setSearch(mapSearch);
        }
        params_pro.setSortFields(Arrays.asList(new String[]{"eventid"}));
        params_pro.setSortOrder(Arrays.asList(new String[]{"DESC"}));

        return zabbixProblemService.get(params_pro, authToken);
    }

    @Override
    public List<ProblemHostDTO> findProblemHost(ProblemParams params) throws Exception {
        // get token
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        // get host info
        List<Object> hostInfo = hostRepo.getHostIdsAndIp();

        // create a map to store host information, using host id as key
        Map<String, Object[]> mapHostInfo = new HashMap<>();
        for (int i = 0; i < hostInfo.size(); i++) {
            Object[] host = (Object[]) hostInfo.get(i);
            mapHostInfo.put(host[0].toString(), host);
        }

        List<ProblemHostDTO> problemHostDTOs = new ArrayList<>();

        // for each hostId, find problem
        for (String hostId : mapHostInfo.keySet()) {
            params.setHostId(hostId);
            List<ZabbixProblemDTO> problems = findByCondition(params, authToken);
            if (problems != null) {
                for (ZabbixProblemDTO problem : problems) {
                    ProblemHostDTO problemHostDTO = new ProblemHostDTO();
                    problemHostDTO.setZabbixProblemDTO(problem);
                    Object[] obj = mapHostInfo.get(hostId);
                    problemHostDTO.setHostId(obj[0].toString());
                    problemHostDTO.setHostName(obj[1].toString());
                    problemHostDTO.setIp(obj[2].toString());
                    problemHostDTOs.add(problemHostDTO);
                }
            }

        }
        return problemHostDTOs;
    }

    @Override
    public List<ProblemClaimDTO> findBySeverityLevel(ProblemClaimParams params) throws Exception {
        List<ProblemClaimDTO> list = new ArrayList<>();
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }
        String claimType = params.getClaimType();
        if (claimType == null || ("").equals(claimType)) {
            claimType = "-1";
        }
        for (Integer integer : params.getSeverities()) {
            ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();
            if (integer != null) {
                Map mapFilter = new HashMap();
                mapFilter.put("severity", integer);
                params_pro.setFilter(mapFilter);
                List<ZabbixProblemDTO> listZ = zabbixProblemService.get(params_pro, authToken);
                List<ProblemClaimDTO> problemClaimDTOS = new ArrayList<>();
                for (ZabbixProblemDTO zabbixProblemDTO : listZ) {
                    if ("0".equals(claimType)) {
                        ProblemClaimDTO problemClaimDTO = new ProblemClaimDTO();
                        problemClaimDTO.setZabbixProblemDTO(zabbixProblemDTO);
                        MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                        if (temp == null) {
                            problemClaimDTO.setIsClaim(0);
                            problemClaimDTOS.add(problemClaimDTO);
                        }
                    } else if ("1".equals(claimType)) {
                        ProblemClaimDTO problemClaimDTO = new ProblemClaimDTO();
                        problemClaimDTO.setZabbixProblemDTO(zabbixProblemDTO);
                        MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                        if (temp != null) {
                            problemClaimDTO.setIsClaim(temp.getIsClaim());
                            problemClaimDTOS.add(problemClaimDTO);
                        }
                    } else {
                        ProblemClaimDTO problemClaimDTO = new ProblemClaimDTO();
                        problemClaimDTO.setZabbixProblemDTO(zabbixProblemDTO);
                        MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                        if (temp != null) {
                            problemClaimDTO.setIsClaim(temp.getIsClaim());
                        }
                        problemClaimDTOS.add(problemClaimDTO);
                    }
                }
                list.addAll(problemClaimDTOS);
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

    @Override
    public List<MonitorClaimEntity> findClaimByUser(String problemName, int resolveType) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUserEntity user = sysUserRepo.findByUsername(username);
        List<MonitorClaimEntity> list = monitorClaimRepo.findClaimByUser(user.getId(), problemName, resolveType);
        return list;
    }

    @Override
    public void updateClaimAfterRegister(MonitorClaimEntity monitorClaimEntity) {
        if (monitorClaimEntity.getIsResolve() == ConstUtil.RESOLVED) {
            monitorClaimRepo.updateClaimAfterRegister(monitorClaimEntity.getId(), monitorClaimEntity.getIsRegister(), monitorClaimEntity.getIsResolve(), monitorClaimEntity.getProblemHandleTime(), new Timestamp(System.currentTimeMillis()));
        } else {
            monitorClaimRepo.updateClaimAfterRegister(monitorClaimEntity.getId(), monitorClaimEntity.getIsRegister(), monitorClaimEntity.getIsResolve(), monitorClaimEntity.getProblemHandleTime());
        }
    }

    @Override
    public MonitorClaimEntity findByProblemId(String problemId) {
        return monitorClaimRepo.getMonitorClaimEntityById(problemId);
    }

    @Override
    public List<ZabbixProblemDTO> getAlertdata(ProblemParams params) throws Exception {
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();

        // set severity
        if (params.getSeverity() != null) {
            Map<String, Object> mapFilter = new HashMap();
            mapFilter.put("severity", params.getSeverity());
            params_pro.setFilter(mapFilter);
        }

        // set hostId
        if (params.getHostId() != null) {
            List<String> hostIds = new ArrayList<>();
            hostIds.add(params.getHostId());
            params_pro.setHostids(hostIds);
        }

        // set timeFrom
        if (params.getTimeFrom() != null && !params.getTimeFrom().equals("NaN")) {
            params_pro.setTime_from(params.getTimeFrom());
        }

        // set timeTill
        if (params.getTimeTill() != null && !params.getTimeTill().equals("NaN")) {
            params_pro.setTime_till(params.getTimeTill());
        }

        // set name
        if (params.getName() != null && params.getName().length() > 0) {
            Map<String, Object> mapSearch = new HashMap();
            mapSearch.put("name", params.getName());
            params_pro.setSearch(mapSearch);
        }

        return zabbixProblemService.get(params_pro, authToken);
    }

    @Override
    public List<MonitorClaimEntity> findAllClaim() {
        return monitorClaimRepo.findAll();
    }

    @Override
    public List<MonitorClaimEntity> findByIsResolve() {
        return monitorClaimRepo.findByIsResolve(1);
    }

    @Override
    public List<MonitorClaimEntity> findByIsResolveAndProblemName(String problemName) {
        return monitorClaimRepo.findByIsResolveAndProblemName(1, problemName);
    }

    @Override
    public List<MonitorClaimEntity> findByIsResolveAndProblemName(String problemName, String resolveTimeStart, String resolveTimeEnd) {
        LocalDateTime resolveTimeStart1 = LocalDateTime.parse(resolveTimeStart + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime resolveTimeEnd1 = LocalDateTime.parse(resolveTimeEnd + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return monitorClaimRepo.findByIsResolveAndProblemName(1, problemName, resolveTimeStart1, resolveTimeEnd1);
    }

    @Override
    public List<MonitorClaimEntity> findByIsResolve(String resolveTimeStart, String resolveTimeEnd) throws Exception {
        LocalDateTime resolveTimeStart1 = LocalDateTime.parse(resolveTimeStart + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime resolveTimeEnd1 = LocalDateTime.parse(resolveTimeEnd + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return monitorClaimRepo.findByIsResolve(resolveTimeStart1, resolveTimeEnd1);
    }
}
