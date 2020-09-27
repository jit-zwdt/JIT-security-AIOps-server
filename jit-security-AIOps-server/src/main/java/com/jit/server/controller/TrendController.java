package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorHostDetailBindGraphs;
import com.jit.server.pojo.MonitorHostDetailBindItems;
import com.jit.server.request.TrendParams;
import com.jit.server.service.MonitorHostDetailBindGraphsService;
import com.jit.server.service.MonitorHostDetailBindItemsService;
import com.jit.server.service.TrendService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixGetTrendDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: jian_liu
 * @Date: 2020/07/21 16:30
 */

@RestController
@RequestMapping("/trend")
public class TrendController {
    @Autowired
    private TrendService trendService;

    @Autowired
    private MonitorHostDetailBindItemsService monitorHostDetailBindItemsService;

    @Autowired
    private MonitorHostDetailBindGraphsService monitorHostDetailBindGraphsService;

    @PostMapping("/getItemInfoList")
    public Result getItemInfoList(@RequestBody TrendParams trendParams, HttpServletResponse resp) throws IOException {
        try {
            if (trendParams != null && trendParams.getItemids() != null) {
                List<ZabbixGetTrendDTO> result = trendService.getTrendInfoList(trendParams);
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

    @PostMapping("/addHostDetailItem")
    public Result addHostDetailItem(@RequestBody TrendParams trendParams) {
        try {
            if (trendParams != null) {
                MonitorHostDetailBindItems monitorHostDetailBindItems = new MonitorHostDetailBindItems();
                BeanUtils.copyProperties(trendParams, monitorHostDetailBindItems);
                monitorHostDetailBindItems.setGmtCreate(LocalDateTime.now());
                monitorHostDetailBindItems.setIsDeleted(0);
                monitorHostDetailBindItems = monitorHostDetailBindItemsService.saveOrUpdateMonitorHostDetailBindItems(monitorHostDetailBindItems);
                if (StringUtils.isNotBlank(monitorHostDetailBindItems.getId())) {
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addHostDetailGraph")
    public Result addHostDetailGraph(@RequestBody TrendParams trendParams) {
        try {
            if (trendParams != null) {
                MonitorHostDetailBindGraphs monitorHostDetailBindGraphs = new MonitorHostDetailBindGraphs();
                BeanUtils.copyProperties(trendParams, monitorHostDetailBindGraphs);
                monitorHostDetailBindGraphs.setGmtCreate(LocalDateTime.now());
                monitorHostDetailBindGraphs.setIsDeleted(0);
                monitorHostDetailBindGraphs = monitorHostDetailBindGraphsService.saveOrUpdateMonitorHostDetailBindGraphs(monitorHostDetailBindGraphs);
                if (StringUtils.isNotBlank(monitorHostDetailBindGraphs.getId())) {
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteHostDetailItem/{id}")
    public Result deleteHostDetailItem(@PathVariable String id) {
        try {
            MonitorHostDetailBindItems monitorHostDetailBindItems = monitorHostDetailBindItemsService.findById(id);
            if (monitorHostDetailBindItems != null) {
                monitorHostDetailBindItems.setGmtModified(LocalDateTime.now());
                monitorHostDetailBindItems.setIsDeleted(1);
                monitorHostDetailBindItems = monitorHostDetailBindItemsService.saveOrUpdateMonitorHostDetailBindItems(monitorHostDetailBindItems);
                if (StringUtils.isNotBlank(monitorHostDetailBindItems.getId())) {
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteHostDetailGraph/{id}")
    public Result deleteHostDetailGraph(@PathVariable String id) {
        try {
            MonitorHostDetailBindGraphs monitorHostDetailBindGraphs = monitorHostDetailBindGraphsService.findById(id);
            if (monitorHostDetailBindGraphs != null) {
                monitorHostDetailBindGraphs.setGmtModified(LocalDateTime.now());
                monitorHostDetailBindGraphs.setIsDeleted(1);
                monitorHostDetailBindGraphs = monitorHostDetailBindGraphsService.saveOrUpdateMonitorHostDetailBindGraphs(monitorHostDetailBindGraphs);
                if (StringUtils.isNotBlank(monitorHostDetailBindGraphs.getId())) {
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findHostDetailItems/{hostId}")
    public Result findHostDetailItems(@PathVariable String hostId) {
        try {
            return Result.SUCCESS(monitorHostDetailBindItemsService.findMonitorHostDetailBindItemsByHostId(hostId, 0));
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findHostDetailGraphs/{hostId}")
    public Result findHostDetailGraphs(@PathVariable String hostId) {
        try {
            return Result.SUCCESS(monitorHostDetailBindGraphsService.findMonitorHostDetailBindGraphsByHostId(hostId, 0));
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }


    @PostMapping("/checkHostDetailItem")
    public Result checkHostDetailItem(@RequestBody TrendParams trendParams) {
        try {
            if (trendParams != null && StringUtils.isNotBlank(trendParams.getHostId()) && StringUtils.isNotBlank(trendParams.getItemId())) {
                String hostId = trendParams.getHostId();
                String itemId = trendParams.getItemId();
                MonitorHostDetailBindItems monitorHostDetailBindItems = monitorHostDetailBindItemsService.findByHostIdAndItemIdAndIsDeleted(hostId, itemId, 0);
                if (monitorHostDetailBindItems != null) {
                    return Result.SUCCESS(true);
                } else {
                    return Result.SUCCESS(false);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/checkHostDetailGraph")
    public Result checkHostDetailGraph(@RequestBody TrendParams trendParams) {
        try {
            if (trendParams != null && StringUtils.isNotBlank(trendParams.getHostId()) && StringUtils.isNotBlank(trendParams.getGraphId())) {
                String hostId = trendParams.getHostId();
                String graphId = trendParams.getGraphId();
                MonitorHostDetailBindGraphs monitorHostDetailBindGraphs = monitorHostDetailBindGraphsService.findByHostIdAndGraphIdAndIsDeleted(hostId, graphId, 0);
                if (monitorHostDetailBindGraphs != null) {
                    return Result.SUCCESS(true);
                } else {
                    return Result.SUCCESS(false);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

}
