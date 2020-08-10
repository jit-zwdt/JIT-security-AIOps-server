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
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.model.IZabbixMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

//    @PostMapping("/findByCondition")
//    public Result findByCondition(@RequestBody ProblemParams params, HttpServletResponse resp) throws IOException {
//        try {
//            List<ZabbixProblemDTO> result = problemService.findByCondition(params);
//            if (null != result && !CollectionUtils.isEmpty(result)) {
//                return Result.SUCCESS(result);
//            } else {
//                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
//        }
//    }

    @PostMapping("/findProblemHost")
    public Result findProblemHost(@RequestBody ProblemParams params, HttpServletResponse resp) throws IOException {
        System.out.println(params);
        try {
            List<ProblemHostDTO> result = problemService.findProblemHost(params);
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
    public Result findBySeverityLevel(@RequestBody ProblemClaimParams params, HttpServletResponse resp) throws IOException {
        try {
            if(params != null && params.getSeverities() != null) {
                List<ProblemClaimDTO> result = problemService.findBySeverityLevel(params);
                if(null != result && !CollectionUtils.isEmpty(result)) {
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addClaim")
    public Result addClaim(@RequestBody MonitorClaimEntity monitorClaimEntity) {
        try{
            if(monitorClaimEntity!=null){
                monitorClaimEntity.setClaimTime(LocalDateTime.now());
                problemService.addCalim(monitorClaimEntity);
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findClaimByUser")
    public Result findClaimByUser() {
        try{
            List<MonitorClaimEntity> list = problemService.findClaimByUser();
            return Result.SUCCESS(list);
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateClaimAfterRegister")
    public Result updateClaimAfterRegister(@RequestBody MonitorClaimEntity monitorClaimEntity) {
        try{
            problemService.updateClaimAfterRegister(monitorClaimEntity);
            return Result.SUCCESS(null);
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
    @PostMapping("/findByProblemId")
    public Result findByProblemId(@RequestParam(value = "problemId")  String problemId) {
        try{
            if(problemId!=null){
                Map<String,Object> map = new HashMap<>();
                MonitorClaimEntity monitorClaimEntity = problemService.findByProblemId(problemId);
                map.put("claimOpinion",monitorClaimEntity.getClaimOpinion());
                map.put("role",sysRoleRepo.getOne(monitorClaimEntity.getClaimRoleId()).getRoleName());
                map.put("user",sysUserRepo.getOne(monitorClaimEntity.getClaimUserId()).getUsername());
                return Result.SUCCESS(map);
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/problemSolveReport")
    public Result problemSolveReport(@RequestParam("problemType")String problemType,@RequestParam("problemName")String problemName) {
        try{
            List<MonitorClaimEntity> claimList = problemService.findByIsResolve();
            if(StringUtils.isNotEmpty(problemName)){
                claimList = problemService.findByIsResolveAndProblemNameLike(problemName);
            }
            List<ProblemSolveReportDTO> resultList = new ArrayList<>();
            if(CollectionUtils.isEmpty(claimList)){
                return Result.SUCCESS(null);
            }
            for(int i = 0; i < claimList.size();i ++){
                ProblemSolveReportDTO result = new ProblemSolveReportDTO();
                List<MonitorRegisterEntity> regList = registerService.findByClaimIdAndIsResolve(claimList.get(i).getId());
                if(StringUtils.isNotEmpty(problemType)){
                    regList = registerService.findByClaimIdAndProblemType(claimList.get(i).getId(),problemType);
                }
                if(regList != null && !CollectionUtils.isEmpty(regList)) {
                    for(MonitorRegisterEntity mm : regList){
                        result.setRegister(mm);
                    }
                } else {
                    continue;
                }
                result.setIndex(i + 1);
                result.setClaim(claimList.get(i));
                result.setUser(sysUserRepo.getOne(claimList.get(i).getClaimUserId()).getUsername());
                result.setRole(sysRoleRepo.getOne(claimList.get(i).getClaimRoleId()).getRoleName());
                resultList.add(result);
            }
            return Result.SUCCESS(resultList);
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getAlertdata")
    public Result getAlertdata(@RequestBody ProblemParams params, HttpServletResponse resp) throws IOException {
        try {
            List<ZabbixProblemDTO> result = problemService.getAlertdata(params);
            if (null != result && !CollectionUtils.isEmpty(result)) {
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
