package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.dto.HostDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.request.HostParams;
import com.jit.server.request.HostViewInfoParams;
import com.jit.server.service.HostService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.*;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.dto.ZabbixHostGroupDTO;
import com.jit.zabbix.client.request.ZabbixGetHostGroupParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/17 17:42
 */

@Slf4j
@RestController
@RequestMapping("/host")
public class HostController {
    @Autowired
    HostService hostService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @PostMapping("/addHost")
    @AutoLog(value = "监控列表-添加", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addHost(@RequestBody HostParams params, HttpServletRequest req) {
        try{
            if(params!=null){
                HostEntity host = new HostEntity();
                BeanUtils.copyProperties(params, host);
                host.setGmtCreate(LocalDateTime.now());
                host.setGmtModified(LocalDateTime.now());
                host.setDeleted(ConstUtil.IS_NOT_DELETED);
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                if(StringUtils.isNotEmpty(hostService.addHost(host, auth))){
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
    @AutoLog(value = "监控列表-编辑", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result<HostEntity> updateHost(@RequestBody HostParams params, @PathVariable String id, HttpServletRequest req) {
        try{
            if(params!=null && StringUtils.isNotEmpty(id)){
                Optional<HostEntity> bean = hostService.findByHostId(id);
                if (bean.isPresent()) {
                    HostEntity host = bean.get();
                    BeanUtils.copyProperties(params, host);
                    host.setGmtModified(LocalDateTime.now());
                    String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                    if(StringUtils.isNotEmpty(hostService.updateHost(host, auth))){
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
    @AutoLog(value = "监控列表-删除", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteHost(@PathVariable String id, HttpServletRequest req) {
        try{
            Optional<HostEntity> bean = hostService.findByHostId(id);
            if (bean.isPresent()) {
                HostEntity host = bean.get();
                host.setGmtModified(LocalDateTime.now());
                host.setDeleted(ConstUtil.IS_DELETED);
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                if(StringUtils.isNotEmpty(hostService.deleteHost(host,auth))){
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
    @AutoLog(value = "监控列表-[启动,停用]监控状态", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateHostEnableMonitor(@PathVariable String id, @RequestParam("enableMonitor") String enableMonitor, HttpServletRequest req) {
        try{
            Optional<HostEntity> bean = hostService.findByHostId(id);
            if (bean.isPresent() && StringUtils.isNotEmpty(enableMonitor)) {
                HostEntity host = bean.get();
                host.setGmtModified(LocalDateTime.now());
                host.setEnableMonitor(enableMonitor.trim());
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                if(StringUtils.isNotEmpty(hostService.updateHostEnableMonitor(host, auth))){
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

    @PostMapping("/getHost/{id}")
    public Result<HostEntity> getHost(@PathVariable String id) {
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

    @PostMapping("/getHosts")
    @AutoLog(value = "监控列表-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result<Object> getHosts(@RequestBody PageRequest<HostParams> params, HttpServletResponse resp) throws IOException{
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
                        view.setTypeId(rowArray[2]+"");
                        view.setHostIp(StringUtils.isNotEmpty(rowArray[3]+"")?rowArray[3]+"":rowArray[4]+"");
                        view.setAgentIp(rowArray[3]+"");
                        view.setSnmpIp(rowArray[4]+"");
                        view.setEnableMonitor(rowArray[5]+"");
                        view.setGroupId(rowArray[6]+"");
                        view.setHostLabel(rowArray[7]+"");
                        view.setObjectName(rowArray[8]+"");
                        view.setRemark(rowArray[9]+"");
                        view.setType(rowArray[10]+"");
                        view.setSubtype(rowArray[11]+"");
                        view.setBusinessName(rowArray[12]+"");
                        view.setTemplatesId(rowArray[13]+"");
                        view.setSubtypeId(rowArray[14]+"");
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

    @PostMapping("/getHostAvailable")
    public Result<HostEntity> getHostAvailable(@RequestBody String[] hostIds, HttpServletRequest req) {
        try{
            if(hostIds!=null && hostIds.length>0){
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<ZabbixHostDTO> result = hostService.getHostAvailableFromZabbix(Arrays.asList(hostIds), auth);
                if (result!=null && !CollectionUtils.isEmpty(result)) {
                    return Result.SUCCESS(result);
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
    /**
     * get zabbix hostGroup by host type
     *
     * @return
     */
    @PostMapping(value = "/getZabbixHostGroupByHostType")
    public Result getZabbixHostGroupByHostType(@RequestParam("typeId") String typeId, @RequestParam(value = "groupName",required = false) String groupName, HttpServletRequest req) {
        try{
            if(typeId!=null){
                Map<String, Object> params = new HashMap<>();
                params.put("typeId", typeId);
                params.put("groupName", groupName);
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<ZabbixHostGroupDTO> resultList= hostService.findHostGroupByTypeId(params, auth);
                if (null != resultList && !CollectionUtils.isEmpty(resultList)) {
                    return Result.SUCCESS(resultList);
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

    @PostMapping(value = "/getTop5ByItem")
    public Result getTop5ByItem(@RequestBody Map<String, Object> params, HttpServletRequest req) {
        //, @RequestParam("itemKey") String itemKey, @RequestParam(value = "typeId",required = false) String typeId, @RequestParam(value = "subtypeId",required = false) String subtypeId
        try{
            if(params!=null){
               /* params.put("typeId", typeId);
                params.put("subtypeId", subtypeId);
                params.put("itemKey", itemKey);
                params.put("valueType", valueType);*/
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<Map<String, String>> resultList= hostService.getTop5ByItem(params, auth);
                if (null != resultList && !CollectionUtils.isEmpty(resultList)) {
                    return Result.SUCCESS(resultList);
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

    @PostMapping(value = "/getTop5ByTriggers")
    @AutoLog(value = "监控-top5 [异常,告警]信息查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getTop5ByTriggers(@RequestBody Map<String, Object> params, HttpServletRequest req) {
        //, @RequestParam("itemKey") String itemKey, @RequestParam(value = "typeId",required = false) String typeId, @RequestParam(value = "subtypeId",required = false) String subtypeId
        try{
            if(params!=null){
               /* params.put("typeId", typeId);
                params.put("subtypeId", subtypeId);
                params.put("valueType", valueType);*/
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<Map<String, String>> resultList= hostService.getTop5ByTrigger(params, auth);
                if (null != resultList && !CollectionUtils.isEmpty(resultList)) {
                    return Result.SUCCESS(resultList);
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

    @PostMapping("/getHostIdInfo/{hostId}")
    public Result<HostEntity> getHostIdInfo(@PathVariable String hostId) {
        try{
            HostDTO bean = hostService.findHostIdinfo(hostId);
            if (bean!=null) {
                return Result.SUCCESS(bean);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    /**
     * 判断主机名称是否重复
     * @param objectName 主机名称
     * @param odlObjectName 旧的主机名称
     * @return 返回false表示主机名称重复，反之true
     */
    @GetMapping("/checkObjectName")
    public Result checkObjectName(String objectName , String odlObjectName){
//        if (StringUtils.isNotEmpty(objectName) ) {
        Boolean flag = null;
        try {
            flag = hostService.checkObjectName(objectName , odlObjectName);
            if (flag != null){
                return Result.SUCCESS(flag);
            }else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }
        catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    /**
     * 判断业务名称是否重复
     * @param businessName 业务名称
     * @param odlBusinessName 旧的业务名称
     * @return 返回false表示业务名称重复，反之true
     */
    @GetMapping("/checkBusinessName")
    public Result checkBusinessName(String businessName , String odlBusinessName){
//        if (StringUtils.isNotEmpty(objectName) ) {
        Boolean flag = null;
        try {
            flag = hostService.checkBusinessName(businessName , odlBusinessName);
            if (flag != null){
                return Result.SUCCESS(flag);
            }else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }
        catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
