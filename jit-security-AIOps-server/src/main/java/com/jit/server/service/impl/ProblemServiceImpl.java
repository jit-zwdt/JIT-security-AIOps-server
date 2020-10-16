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
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.request.ZabbixGetProblemParams;
import com.jit.zabbix.client.service.ZabbixProblemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        if (StringUtils.isBlank(authToken)) {
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
        if (params.getHostIds() != null) {
            params_pro.setHostids(Arrays.asList(params.getHostIds().split(",")));
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
    public List<ProblemHostDTO> findProblemHost(ProblemParams params, String auth) throws Exception {
        // get host info
        List<Object> hostInfo = hostRepo.getHostIdsAndIp();

        if (hostInfo != null && !hostInfo.isEmpty()) {
            List<String> hostIds = new ArrayList<>(hostInfo.size());
            // create a map to store host information, using host id as key
            Map<String, Object[]> mapHostInfo = new HashMap<>(hostInfo.size());
            String hostid;
            Object[] host;
            for (int i = 0; i < hostInfo.size(); i++) {
                host = (Object[]) hostInfo.get(i);
                hostid = host[0] != null ? host[0].toString() : "";
                mapHostInfo.put(hostid, host);
                hostIds.add(hostid);
            }

            // get problems
            List<ProblemHostDTO> problemHostDTOs = new ArrayList<>();
            if (StringUtils.isBlank(params.getHostIds())) {
                params.setHostIds(StringUtils.join(hostIds, ","));
            }
            // for each hostId, find problem
            List<ZabbixProblemDTO> problems = findByCondition(params, auth);
            if (problems != null) {
                for (ZabbixProblemDTO problem : problems) {
                    ProblemHostDTO problemHostDTO = new ProblemHostDTO();
                    problemHostDTO.setZabbixProblemDTO(problem);
                    Object[] obj = mapHostInfo.get(problem.getHostid());
                    problemHostDTO.setHostId(obj[0].toString());
                    problemHostDTO.setHostName(obj[1].toString());
                    problemHostDTO.setIp(obj[2].toString());
                    problemHostDTOs.add(problemHostDTO);
                }
            }
            return problemHostDTOs;
        } else {
            return null;
        }
    }

    @Override
    public List<ProblemClaimDTO> findBySeverityLevel(ProblemClaimParams params, String auth) throws Exception {
        List<ProblemClaimDTO> list = new ArrayList<>();
        String claimType = params.getClaimType();
        if (claimType == null || "".equals(claimType)) {
            claimType = "-1";
        }
        List<Object> hostInfo = hostRepo.getHostIdsAndIp();
        if (hostInfo != null && !hostInfo.isEmpty()) {
            List<String> hostIds = new ArrayList<>(hostInfo.size());
            String hostid;
            Object[] host;
            for (int i = 0; i < hostInfo.size(); i++) {
                host = (Object[]) hostInfo.get(i);
                hostid = host[0] != null ? host[0].toString() : "";
                hostIds.add(hostid);
            }

            ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();
            List<Integer> severities = params.getSeverities();
            if (severities != null && !severities.isEmpty()) {
                params_pro.setSeverities(severities);
                params_pro.setHostids(hostIds);
                params_pro.setSortFields(Arrays.asList(new String[]{"eventid"}));
                params_pro.setSortOrder(Arrays.asList(new String[]{"DESC"}));
                List<ZabbixProblemDTO> listZ = zabbixProblemService.get(params_pro, auth);
                List<ProblemClaimDTO> problemClaimDTOS = new ArrayList<>();
                for (ZabbixProblemDTO zabbixProblemDTO : listZ) {
                    if ("0".equals(claimType)) {
                        ProblemClaimDTO problemClaimDTO = new ProblemClaimDTO();
                        problemClaimDTO.setZabbixProblemDTO(zabbixProblemDTO);
                        MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                        if (temp == null) {
                            problemClaimDTO.setIsClaim(0);
                            problemClaimDTO.setClaimUser("-");
                            problemClaimDTOS.add(problemClaimDTO);
                        }
                    } else if ("1".equals(claimType)) {
                        ProblemClaimDTO problemClaimDTO = new ProblemClaimDTO();
                        problemClaimDTO.setZabbixProblemDTO(zabbixProblemDTO);
                        MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                        if (temp != null) {
                            problemClaimDTO.setIsClaim(temp.getIsClaim());
                            problemClaimDTO.setClaimUser(sysUserRepo.findNameById(temp.getClaimUserId()));
                            problemClaimDTOS.add(problemClaimDTO);
                        }
                    } else {
                        ProblemClaimDTO problemClaimDTO = new ProblemClaimDTO();
                        problemClaimDTO.setZabbixProblemDTO(zabbixProblemDTO);
                        MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                        if (temp != null) {
                            problemClaimDTO.setIsClaim(temp.getIsClaim());
                            problemClaimDTO.setClaimUser(sysUserRepo.findNameById(temp.getClaimUserId()));
                        } else {
                            problemClaimDTO.setClaimUser("-");
                        }
                        problemClaimDTOS.add(problemClaimDTO);
                    }
                }
                list.addAll(problemClaimDTOS);
            }
            return list;
        } else {
            return null;
        }
    }

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
            monitorClaimRepo.updateClaimAfterRegister(monitorClaimEntity.getId(), monitorClaimEntity.getIsRegister(), monitorClaimEntity.getIsResolve(), monitorClaimEntity.getProblemHandleTime(), LocalDateTime.now());
        } else {
            monitorClaimRepo.updateClaimAfterRegister(monitorClaimEntity.getId(), monitorClaimEntity.getIsRegister(), monitorClaimEntity.getIsResolve(), monitorClaimEntity.getProblemHandleTime());
        }
    }

    @Override
    public MonitorClaimEntity findByProblemId(String problemId) {
        return monitorClaimRepo.getMonitorClaimEntityById(problemId);
    }

    @Override
    public List<ZabbixProblemDTO> getAlertdata(ProblemParams params, String auth) throws Exception {
        ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();

        // set severity
        if (params.getSeverity() != null) {
            Map<String, Object> mapFilter = new HashMap();
            mapFilter.put("severity", params.getSeverity());
            params_pro.setFilter(mapFilter);
        }

        // set hostId
        if (params.getHostIds() != null) {
            params_pro.setHostids(Arrays.asList(params.getHostIds().split(",")));
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

        return zabbixProblemService.get(params_pro, auth);
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

    @Override
    public List<ZabbixProblemDTO> findProblemById(String[] params,String auth) throws Exception {
        ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();
        if (params!= null ) {
            Map<String, Object> mapFilter = new HashMap();
            mapFilter.put("objectid", params);
            params_pro.setFilter(mapFilter);
        }
        return zabbixProblemService.get(params_pro, auth);
    }
}
