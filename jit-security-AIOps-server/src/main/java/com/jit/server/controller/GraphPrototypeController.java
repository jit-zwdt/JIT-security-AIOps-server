package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorHostDetailBindGraphs;
import com.jit.server.request.CreateGraphPrototypeParams;
import com.jit.server.request.GraphPrototypeParams;
import com.jit.server.request.ItemParams;
import com.jit.server.request.TrendParams;
import com.jit.server.service.GraphPrototypeService;
import com.jit.server.service.ItemService;
import com.jit.server.service.MonitorHostDetailBindGraphsService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lancelot
 * @Date: 2020/07/28 10:51
 */

@Slf4j
@RestController
@RequestMapping("/gPrototype")
public class GraphPrototypeController {
    @Autowired
    private GraphPrototypeService graphPrototypeService;

    @Autowired
    private MonitorHostDetailBindGraphsService monitorHostDetailBindGraphsService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @PostMapping("/getGProInfoList")
    @AutoLog(value = "监控信息-查询所有图形", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getGProInfoList(@RequestBody GraphPrototypeParams graphPrototypeParams, HttpServletRequest req) throws IOException {
        try {
            if (graphPrototypeParams != null) {
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<ZabbixGetGraphPrototypeDTO> result = graphPrototypeService.getGProList(graphPrototypeParams,auth);
                if (result != null && !CollectionUtils.isEmpty(result)) {
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

    @PostMapping("/createGpro")
    @AutoLog(value = "监控信息-添加图形", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result createGpro(@RequestBody ZabbixCreateGraphPrototypeParams zabbixCreateGraphPrototypeParams,HttpServletRequest req
    ) throws IOException {
        try {
            if(zabbixCreateGraphPrototypeParams != null && zabbixCreateGraphPrototypeParams.getGitems() !=null){
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<String> graphids = graphPrototypeService.createGPro(zabbixCreateGraphPrototypeParams, auth);
                if(graphids != null && !CollectionUtils.isEmpty(graphids)){
                    return Result.SUCCESS(graphids);
                }else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e){
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateGpro")
    @AutoLog(value = "监控信息-编辑图形", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateGpro(@RequestBody ZabbixCreateGraphPrototypeParams zabbixCreateGraphPrototypeParams,HttpServletRequest req
    ) throws IOException {
        try {
            if(zabbixCreateGraphPrototypeParams != null && zabbixCreateGraphPrototypeParams.getGitems() !=null){
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<String> graphids = graphPrototypeService.updateGPro(zabbixCreateGraphPrototypeParams, auth);
                if(graphids != null && !CollectionUtils.isEmpty(graphids)){
                    return Result.SUCCESS(graphids);
                }else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e){
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/deleteGpro")
    @AutoLog(value = "监控信息-删除图形", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteGpro(@RequestBody TrendParams trendParams, HttpServletRequest req
    ) throws IOException {
        try {
            if(!trendParams.getGraphId().isEmpty()){
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<String> graphids = graphPrototypeService.deleteGPro(trendParams.getGraphId(), auth);
                if(graphids != null && !CollectionUtils.isEmpty(graphids)){
                    MonitorHostDetailBindGraphs monitorHostDetailBindGraphs = monitorHostDetailBindGraphsService.findByHostIdAndGraphIdAndIsDeleted(trendParams.getHostId(), trendParams.getGraphId(), ConstUtil.IS_NOT_DELETED);
                    if (monitorHostDetailBindGraphs != null) {
                        monitorHostDetailBindGraphs.setGmtModified(LocalDateTime.now());
                        monitorHostDetailBindGraphs.setIsDeleted(ConstUtil.IS_DELETED);
                        monitorHostDetailBindGraphsService.saveOrUpdateMonitorHostDetailBindGraphs(monitorHostDetailBindGraphs);
                    }
                    return Result.SUCCESS(graphids);
                }else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e){
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
