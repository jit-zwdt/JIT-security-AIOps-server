package com.jit.server.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jit.server.annotation.AutoLog;
import com.jit.server.dto.MonitorRegisterEntityDTO;
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
import com.jit.server.service.DictionaryService;
import com.jit.server.service.MonitorRegisterService;
import com.jit.server.service.ProblemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
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

    @Autowired
    private DictionaryService dictionaryService;

    @PostMapping("/getProblemHosts")
    @AutoLog(value = "统一告警查询-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getProblemHosts(@RequestBody ProblemParams params, HttpServletRequest req) throws IOException {
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


    @PostMapping("/getBySeverityLevels")
    @AutoLog(value = "故障认领-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getBySeverityLevels(@RequestBody ProblemClaimParams params, HttpServletRequest req) throws IOException {
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
    @AutoLog(value = "故障认领-认领", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addClaim(@RequestBody MonitorClaimEntity monitorClaimEntity) {
        try {
            if (monitorClaimEntity != null) {
                monitorClaimEntity.setGmtCreate(LocalDateTime.now());
                monitorClaimEntity.setClaimTime(LocalDateTime.now());
                problemService.addCalim(monitorClaimEntity);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getClaimByUsers")
    @AutoLog(value = "故障处理登记-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getClaimByUsers(@RequestParam(value = "problemName") String problemName, @RequestParam(value = "resolveType") String resolveType) {
        try {
            if (resolveType == null || ("").equals(resolveType)) {
                resolveType = "-1";
            }
            List<MonitorClaimEntity> list = problemService.findClaimByUser(problemName, Integer.parseInt(resolveType));
            return Result.SUCCESS(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateClaimAfterRegister")
    public Result updateClaimAfterRegister(@RequestBody MonitorClaimEntity monitorClaimEntity) {
        try {
            problemService.updateClaimAfterRegister(monitorClaimEntity);
            return Result.SUCCESS(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getByProblemIds")
    @AutoLog(value = "故障认领-查询已认领信息", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getByProblemIds(@RequestParam(value = "problemId") String problemId) {
        try {
            if (problemId != null) {
                Map<String, Object> map = new HashMap<>();
                MonitorClaimEntity monitorClaimEntity = problemService.findByProblemId(problemId);
                map.put("claimOpinion", monitorClaimEntity.getClaimOpinion());
                map.put("role", sysRoleRepo.getOne(monitorClaimEntity.getClaimRoleId()).getRoleName());
                map.put("user", sysUserRepo.findNameById(monitorClaimEntity.getClaimUserId()));
                return Result.SUCCESS(map);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getProblemSolveReports")
    @AutoLog(value = "故障解决统计报表-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getProblemSolveReports(@RequestParam("problemType") String problemType, @RequestParam("problemName") String problemName, @RequestParam("resolveTimeStart") String resolveTimeStart, @RequestParam("resolveTimeEnd") String resolveTimeEnd, @RequestParam("dictCode") String dictCode) {
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
                    MonitorRegisterEntityDTO monitorRegisterEntityDTO = null;
                    for (MonitorRegisterEntity mm : regList) {
                        monitorRegisterEntityDTO = new MonitorRegisterEntityDTO();
                        monitorRegisterEntityDTO.setProblemType(dictionaryService.getItemTextByDictCodeAndItemValue(dictCode, mm.getProblemType()));
                        monitorRegisterEntityDTO.setProblemProcess(mm.getProblemProcess());
                        monitorRegisterEntityDTO.setProblemReason(mm.getProblemReason());
                        monitorRegisterEntityDTO.setProblemSolution(mm.getProblemSolution());
                        result.setRegister(monitorRegisterEntityDTO);
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
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    /**
     * 根据传入的数据进行 Xls文件的构建生成下载
     *
     * @param tableData 需要生成 xls 的数据 json 格式的字符串
     * @param response  response 对象
     * @throws IOException IO异常
     */
    @PostMapping("/exportFailureToSolve")
    @AutoLog(value = "故障解决统计报表-导出", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public void exportFailureToSolve(@RequestBody String tableData, HttpServletResponse response) {
        //首先对 JSON 格式的数据进行转换
        JSONArray jsonArray = JSONArray.parseArray(tableData);
        //将前端传来的数据转换成为 二维数组
        String[][] dataArray = new String[jsonArray.size()][];
        for (int i = 0; i < jsonArray.size(); i++) {
            //取出里面的对象字符串并转换成为 JSON 对象
            JSONObject faultElement = jsonArray.getJSONObject(i);
            //取出里面的元素进行拼接数组对象
            String index = faultElement.get("index") + "";
            String user = (String) faultElement.get("user");
            String role = (String) faultElement.get("role");
            //取出里面的 claim 对象
            JSONObject claim = faultElement.getJSONObject("claim");
            String resolveTime = (String) claim.get("resolveTime");
            String problemName = (String) claim.get("problemName");
            String ns = (String) claim.get("ns");
            String problemHandleTime = (String) claim.get("problemHandleTime");
            //取出里面的 register 对象
            JSONObject register = faultElement.getJSONObject("register");
            String problemType = (String) register.get("problemType");
            String problemReason = (String) register.get("problemReason");
            String problemProcess = (String) register.get("problemProcess");
            String problemSolution = (String) register.get("problemSolution");
            //构建数组对象
            String[] faultArray = {index, resolveTime, problemName, problemType, role, user, problemReason, problemProcess, problemSolution, ns, problemHandleTime};
            //放入二维数组
            dataArray[i] = faultArray;
        }
        OutputStream out = null;
        try {
            //获取导出的 Xls 文件
            HSSFWorkbook workbook = problemService.downLoadFailureToSolve(dataArray);
            //获取响应流
            out = response.getOutputStream();
            //设置响应协议为响应xls文件
            response.setContentType("application/octet-stream");
            //设置弹出框
            response.setHeader("Content-Disposition", "attachment; fileName=" + UUID.randomUUID() + ".xls");
            //写出
            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
