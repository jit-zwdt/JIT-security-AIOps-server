package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.request.HostParams;
import com.jit.server.request.HostViewInfoParams;
import com.jit.server.service.AssetsService;
import com.jit.server.service.HostService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.service.ZabbixApiService;
import com.jit.zabbix.client.service.ZabbixHostService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/17 17:42
 */

@RestController
@RequestMapping("/host")
public class HostController {
    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixHostService zabbixHostService;

    @Autowired
    HostService hostService;

    @PostMapping("/findByCondition")
    public Result findByCondition(@RequestBody PageRequest<HostParams> params, HttpServletResponse resp) throws IOException {
        try{
            if(params!=null){
                Page<HostEntity> pageResult= hostService.findByCondition(params.getParam(),params.getPage(),params.getSize());
                if (null != pageResult) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("totalRow", pageResult.getTotalElements());
                    result.put("totalPage", pageResult.getTotalPages());
                    result.put("dataList", pageResult.getContent());
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addHost")
    public Result addHost(@RequestBody HostParams params) {
        try{
            if(params!=null){
                HostEntity host = new HostEntity();
                BeanUtils.copyProperties(params, host);
                host.setGmtCreate(LocalDateTime.now());
                host.setGmtModified(LocalDateTime.now());
                host.setDeleted(false);
                if(StringUtils.isNotEmpty(hostService.addHost(host))){
                    return Result.SUCCESS(null);
                }else{
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PutMapping("/updateHost/{id}")
    public Result<HostEntity> updateHost(@RequestBody HostParams params, @PathVariable String id) {
        try{
            if(params!=null && StringUtils.isNotEmpty(id)){
                Optional<HostEntity> bean = hostService.findByHostId(id);
                if (bean.isPresent()) {
                    HostEntity host = bean.get();
                    BeanUtils.copyProperties(params, host);
                    host.setGmtModified(LocalDateTime.now());
                    if(StringUtils.isNotEmpty(hostService.updateHost(host))){
                        return Result.SUCCESS(host);
                    }else{
                        return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                    }
                }else{
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteHost/{id}")
    public Result deleteHost(@PathVariable String id) {
        try{
            Optional<HostEntity> bean = hostService.findByHostId(id);
            if (bean.isPresent()) {
                HostEntity host = bean.get();
                host.setGmtModified(LocalDateTime.now());
                host.setDeleted(true);
                if(StringUtils.isNotEmpty(hostService.deleteHost(host))){
                    return Result.SUCCESS(host);
                }else{
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PutMapping("/updateHostEnableMonitor/{id}")
    public Result updateHostEnableMonitor(@PathVariable String id, @RequestParam("enableMonitor") String enableMonitor) {
        try{
            Optional<HostEntity> bean = hostService.findByHostId(id);
            if (bean.isPresent() && StringUtils.isNotEmpty(enableMonitor)) {
                HostEntity host = bean.get();
                host.setGmtModified(LocalDateTime.now());
                host.setEnableMonitor(enableMonitor.trim());
                if(StringUtils.isNotEmpty(hostService.updateHostEnableMonitor(host))){
                    return Result.SUCCESS(host);
                }else{
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findById/{id}")
    public Result<HostEntity> findById(@PathVariable String id) {
        try{
            Optional<HostEntity> bean = hostService.findByHostId(id);
            if (bean.isPresent()) {
                return Result.SUCCESS(bean);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/hostinfo")
    public Result<Object> hostinfo(@RequestBody PageRequest<HostParams> params, HttpServletResponse resp) throws IOException{
        try{
            if(params!=null){
                Page<Object> pageResult = hostService.hostinfo(params.getParam(),params.getPage(),params.getSize());
                if (null != pageResult) {
                    List<HostViewInfoParams> views = new ArrayList<>();
                    List<Object> userPageList= pageResult.getContent();
                    long totalCount = pageResult.getTotalElements();
                    for (Object o : userPageList) {
                        Object[] rowArray = (Object[]) o;
                        HostViewInfoParams view = new HostViewInfoParams();
                        view.setId(rowArray[0]+"");
                        view.setHostid(rowArray[1]+"");
                        view.setHosttypeId(rowArray[2]+"");
                        view.setHostIp(StringUtils.isNotEmpty(rowArray[3]+"")?rowArray[3]+"":rowArray[4]+"");
                        view.setAgentIp(rowArray[3]+"");
                        view.setSnmpIp(rowArray[4]+"");
                        view.setEnableMonitor(rowArray[5]+"");
                        view.setGroupId(rowArray[6]+"");
                        view.setHosttypeId(rowArray[7]+"");
                        view.setObjectName(rowArray[8]+"");
                        view.setRemark(rowArray[9]+"");
                        view.setType(rowArray[10]+"");
                        view.setSubtype(rowArray[11]+"");
                        views.add(view);
                    }
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("totalRow", totalCount);
                    result.put("dataList", views);
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
