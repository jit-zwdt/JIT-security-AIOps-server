package com.jit.server.controller;

import com.jit.server.dto.ProblemClaimDTO;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.dto.ProblemSolveReportDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.pojo.MonitorRegisterEntity;
import com.jit.server.repository.SysRoleRepo;
import com.jit.server.repository.SysUserRepo;
import com.jit.server.request.ProblemClaimParams;
import com.jit.server.request.ProblemParams;
import com.jit.server.service.MonitorRegisterService;
import com.jit.server.service.ProblemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;
    @Autowired
    private SysRoleRepo sysRoleRepo;
    @Autowired
    private SysUserRepo sysUserRepo;
    @Autowired
    private MonitorRegisterService registerService;
    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @PostMapping("/findProblemHost")
    public Result findProblemHost(@RequestBody ProblemParams params, HttpServletRequest req) throws IOException {
        try {
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            List<ProblemHostDTO> result = problemService.findProblemHost(params, auth);
            if (null != result && !CollectionUtils.isEmpty(result)) {
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }


    @PostMapping("/findBySeverityLevel")
    public Result findBySeverityLevel(@RequestBody ProblemClaimParams params, HttpServletRequest req) throws IOException {
        try {
            if (params != null && params.getSeverities() != null) {
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<ProblemClaimDTO> result = problemService.findBySeverityLevel(params, auth);
                if (null != result && !CollectionUtils.isEmpty(result)) {
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addClaim")
    public Result addClaim(@RequestBody MonitorClaimEntity monitorClaimEntity) {
        try {
            if (monitorClaimEntity != null) {
                monitorClaimEntity.setClaimTime(LocalDateTime.now());
                problemService.addCalim(monitorClaimEntity);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findClaimByUser")
    public Result findClaimByUser(@RequestParam(value = "problemName") String problemName, @RequestParam(value = "resolveType") String resolveType) {
        try {
            if (resolveType == null || ("").equals(resolveType)) {
                resolveType = "-1";
            }
            List<MonitorClaimEntity> list = problemService.findClaimByUser(problemName, Integer.parseInt(resolveType));
            return Result.SUCCESS(list);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateClaimAfterRegister")
    public Result updateClaimAfterRegister(@RequestBody MonitorClaimEntity monitorClaimEntity) {
        try {
            problemService.updateClaimAfterRegister(monitorClaimEntity);
            return Result.SUCCESS(null);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findByProblemId")
    public Result findByProblemId(@RequestParam(value = "problemId") String problemId) {
        try {
            if (problemId != null) {
                Map<String, Object> map = new HashMap<>();
                MonitorClaimEntity monitorClaimEntity = problemService.findByProblemId(problemId);
                map.put("claimOpinion", monitorClaimEntity.getClaimOpinion());
                map.put("role", sysRoleRepo.getOne(monitorClaimEntity.getClaimRoleId()).getRoleName());
                map.put("user", sysUserRepo.getOne(monitorClaimEntity.getClaimUserId()).getUsername());
                return Result.SUCCESS(map);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/problemSolveReport")
    public Result problemSolveReport(@RequestParam("problemType") String problemType, @RequestParam("problemName") String problemName, @RequestParam("resolveTimeStart") String resolveTimeStart, @RequestParam("resolveTimeEnd") String resolveTimeEnd) {
        try {
            List<MonitorClaimEntity> claimList = null;
            if (StringUtils.isNotEmpty(resolveTimeStart) && StringUtils.isNotEmpty(resolveTimeEnd)) {
                claimList = problemService.findByIsResolve(resolveTimeStart, resolveTimeEnd);
                if (StringUtils.isNotEmpty(problemName)) {
                    claimList = problemService.findByIsResolveAndProblemName(problemName, resolveTimeStart, resolveTimeEnd);
                }
            }
            List<ProblemSolveReportDTO> resultList = new ArrayList<>();
            if (CollectionUtils.isEmpty(claimList)) {
                return Result.SUCCESS(null);
            }
            for (int i = 0; i < claimList.size(); i++) {
                ProblemSolveReportDTO result = new ProblemSolveReportDTO();
                List<MonitorRegisterEntity> regList = registerService.findByClaimIdAndIsResolve(claimList.get(i).getId());
                if (StringUtils.isNotEmpty(problemType)) {
                    regList = registerService.findByClaimIdAndProblemType(claimList.get(i).getId(), problemType);
                }
                if (regList != null && !CollectionUtils.isEmpty(regList)) {
                    for (MonitorRegisterEntity mm : regList) {
                        result.setRegister(mm);
                    }
                } else {
                    continue;
                }
                result.setIndex(i + 1);
                result.setClaim(claimList.get(i));
                result.setUser(sysUserRepo.getOne(claimList.get(i).getClaimUserId()).getName());
                result.setRole(sysRoleRepo.getOne(claimList.get(i).getClaimRoleId()).getRoleName());
                resultList.add(result);
            }
            return Result.SUCCESS(resultList);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getAlertdata")
    public Result getAlertdata(@RequestBody ProblemParams params, HttpServletRequest req) throws IOException {
        try {
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            List<ZabbixProblemDTO> result = problemService.getAlertdata(params, auth);
            if (null != result && !CollectionUtils.isEmpty(result)) {
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
