package com.jit.server.service;

import com.jit.server.dto.MonitorClaimDTO;
import com.jit.server.dto.ProblemClaimDTO;
import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.request.ProblemClaimParams;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.request.ProblemParams;
import com.jit.server.util.PageRequest;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ProblemService {
    List<ZabbixProblemDTO> findByCondition(ProblemParams params, String authToken) throws Exception;

    List<ProblemHostDTO> findProblemHost(ProblemParams params ,String auth) throws Exception;

    List<ProblemClaimDTO> findBySeverityLevel(ProblemClaimParams params, String auth ) throws Exception;

    void addCalim(MonitorClaimEntity monitorClaimEntity) throws Exception;

    Page<MonitorClaimDTO> findClaimByUser(PageRequest<Map<String, Object>> params);

    void updateClaimAfterRegister(MonitorClaimEntity monitorClaimEntity);

    MonitorClaimEntity findByProblemId(String problemId);

    List<ZabbixProblemDTO> getAlertdata(ProblemParams params,String auth ) throws Exception;

    List<MonitorClaimEntity> findAllClaim();

    List<MonitorClaimEntity> findByIsResolve();

    List<MonitorClaimEntity> findByIsResolve(String resolveTimeStart, String resolveTimeEnd) throws Exception;

    List<MonitorClaimEntity> findByIsResolveAndProblemName(String problemName);

    List<MonitorClaimEntity> findByIsResolveAndProblemName(String problemName, String resolveTimeStart, String resolveTimeEnd);

    List<ZabbixProblemDTO> findProblemById(String[] params,String auth) throws Exception;

    /**
     * 根据传入的故障解决数据构建 xls 文件
     * @param dataArray 故障解决数据 json 格式的字符串
     * @return xls 文件对象
     */
    HSSFWorkbook downLoadFailureToSolve(String[][] dataArray);
}
