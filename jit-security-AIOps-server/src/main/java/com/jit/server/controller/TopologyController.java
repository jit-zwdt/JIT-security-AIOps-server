package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.annotation.AutoLog;
import com.jit.server.dto.MonitorTopologyDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorTopologyEntity;
import com.jit.server.request.TopologyParams;
import com.jit.server.service.HostService;
import com.jit.server.service.TopologyService;
import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author jian_liu
 * @version 1.0
 * @date 2020.12.23
 */

@Slf4j
@RestController
@RequestMapping("/topology")
public class TopologyController {

    @Autowired
    TopologyService topologyService;

    @Autowired
    private UserService userService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    HostService hostService;

    /**
     * saveTopologyInfo
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/addTopologyInfo")
    @AutoLog(value = "网络拓扑图-网络拓扑图保存", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addTopologyInfo(@RequestBody TopologyParams topologyParams) {
        try {
            MonitorTopologyEntity topologyEntity = new MonitorTopologyEntity();
            if (topologyEntity != null) {
                JSONObject jsonObject = JSONObject.parseObject(topologyParams.getJsonParam());
                String infoName = jsonObject.get("infoName").toString();
                String infoId = jsonObject.get("infoId").toString();
                if (infoId == null || infoId.equals("")) {
                    topologyEntity.setGmtCreate(LocalDateTime.now());
                    topologyEntity.setGmtModified(LocalDateTime.now());
                    topologyEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
                    topologyEntity.setJsonParam(topologyParams.getJsonParam());
                    topologyEntity.setUpdateBy(userService.findIdByUsername());
                    topologyEntity.setCreateBy(userService.findIdByUsername());
                    topologyEntity.setInfoName(infoName);
                    topologyService.addTopology(topologyEntity);
                } else {
                    MonitorTopologyEntity topology = topologyService.getMonitorTopologyInfoById(infoId);
                    if (topology != null) {
                        topology.setGmtModified(LocalDateTime.now());
                        topology.setJsonParam(topologyParams.getJsonParam());
                        topology.setUpdateBy(userService.findIdByUsername());
                        topology.setInfoName(infoName);
                        topologyService.addTopology(topology);
                    }
                }
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    /**
     * getTopologyOneInfo
     *
     * @return
     */
    @PostMapping("/getTopologyOneInfo")
    @ResponseBody
    @AutoLog(value = "网络拓扑图-获取当前目标网络拓扑图", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result<MonitorTopologyEntity> getTopologyOneInfo(@RequestBody TopologyParams topologyParams) {
        try {
            if (topologyParams.getId() == null) {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
            MonitorTopologyDTO bean = topologyService.getMonitorTopologyInfo(topologyParams.getId());
            if (bean != null) {
                return Result.SUCCESS(bean);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    /**
     * getTopologyAllInfo
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/getTopologyAllInfo")
    @AutoLog(value = "网络拓扑图-获取全部网络拓扑图", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result<MonitorTopologyEntity> getTopologyAllInfo(@RequestBody TopologyParams topologyParams) {
        try {
            List<MonitorTopologyDTO> bean = topologyService.getMonitorTopologyAllInfo(topologyParams.getInfoName());
            if (bean != null) {
                return Result.SUCCESS(bean);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    /**
     * getTopologyItemInfo
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/getTopologyItemInfo")
    @AutoLog(value = "网络拓扑图-获取当前目标监控数据", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result<MonitorTopologyEntity> getTopologyItemInfo(@RequestBody TopologyParams topologyParams, HttpServletRequest req) {
        try {
            List<Object> bean = topologyService.getTopologyItemInfo(topologyParams.getIp());
            if (bean != null) {
                String info = "";
                if (bean.size() == 0) {
                    info = "未找到主机！";
                    return Result.SUCCESS(info);
                }
                for (Object obj : bean) {
                    String hostId = obj + "";
                    String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                    List<ZabbixHostDTO> result = hostService.getHostAvailableFromZabbix(Arrays.asList(hostId), auth);
                    if (result != null && result.size() > 0) {
                        ZabbixHostDTO zbhost = result.get(0);
                        if (zbhost.getError() != null && !"".equals(zbhost.getError())) {
                            info = info + ";" + zbhost.getError();
                        }
                        if (zbhost.getJmxError() != null && !"".equals(zbhost.getJmxError())) {
                            info = info + ";" + zbhost.getJmxError();
                        }
                        if (zbhost.getIpmiError() != null && !"".equals(zbhost.getIpmiError())) {
                            info = info + ";" + zbhost.getIpmiError();
                        }
                        if (zbhost.getSnmpError() != null && !"".equals(zbhost.getSnmpError())) {
                            info = info + ";" + zbhost.getSnmpError();
                        }
                    }
                }
                if (!"".equals(info)) {
                    info = info.substring(1, info.length());
                }
                return Result.SUCCESS(info);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteTopology/{id}")
    @AutoLog(value = "网络拓扑图-删除", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteTopology(@PathVariable String id, HttpServletRequest req) {
        try {
            if (StringUtils.isNotBlank(id)) {
                MonitorTopologyEntity topology = topologyService.getMonitorTopologyInfoById(id);
                if (topology != null) {
                    if (topology.getHomePageDisplay() ==ConstUtil.HOME_PAGE_DISPLAY) {
                        return Result.ERROR(ExceptionEnum.HOME_PAGE_DISPLAY_EXISTS);
                    }
                    topology.setGmtModified(LocalDateTime.now());
                    topology.setUpdateBy(userService.findIdByUsername());
                    topology.setIsDeleted(ConstUtil.IS_DELETED);
                    topologyService.addTopology(topology);
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }

    }

    /**
     * updateTopologyHomePageDisplay
     *
     * @return
     */
    @PostMapping("/updateTopologyHomePageDisplay")
    @ResponseBody
    @AutoLog(value = "网络拓扑图-设定首页展示图", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result<MonitorTopologyEntity> updateTopologyHomePageDisplay(@RequestBody Map<String, String> param) {
        try {
            if (param.get("id") == null) {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
            topologyService.updateTopologyHomePageDisplay(param.get("id"));
            return Result.SUCCESS(null);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

}
