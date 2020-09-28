package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.request.HostParams;
import com.jit.server.request.HostViewInfoParams;
import com.jit.server.service.HostService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.dto.ZabbixHostGroupDTO;
import com.jit.zabbix.client.request.ZabbixGetHostGroupParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
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
    HostService hostService;

    @PostMapping("/addHost")
    public Result addHost(@RequestBody HostParams params) {
        try{
            if(params!=null){
                HostEntity host = new HostEntity();
                BeanUtils.copyProperties(params, host);
                host.setGmtCreate(LocalDateTime.now());
                host.setGmtModified(LocalDateTime.now());
                host.setDeleted(ConstUtil.IS_NOT_DELETED);
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
                host.setDeleted(ConstUtil.IS_DELETED);
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

    @PostMapping("/findHostAvailable")
    public Result<HostEntity> findHostAvailable(@RequestBody String[] hostIds) {
        try{
            if(hostIds!=null && hostIds.length>0){
                List<ZabbixHostDTO> result = hostService.getHostAvailableFromZabbix(Arrays.asList(hostIds));
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
    public Result getZabbixHostGroupByHostType(@RequestParam("typeId") String typeId, @RequestParam(value = "groupName",required = false) String groupName) {
        try{
            if(typeId!=null){
                Map<String, Object> params = new HashMap<>();
                params.put("typeId", typeId);
                params.put("groupName", groupName);
                List<ZabbixHostGroupDTO> resultList= hostService.findHostGroupByTypeId(params);
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
    public Result getTop5ByItem(@RequestBody Map<String, Object> params) {
        //, @RequestParam("itemKey") String itemKey, @RequestParam(value = "typeId",required = false) String typeId, @RequestParam(value = "subtypeId",required = false) String subtypeId
        try{
            if(params!=null){
               /* params.put("typeId", typeId);
                params.put("subtypeId", subtypeId);
                params.put("itemKey", itemKey);
                params.put("valueType", valueType);*/
                List<Map<String, String>> resultList= hostService.getTop5ByItem(params);
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

    @PostMapping(value = "/getTop5ByTrigger")
    public Result getTop5ByTrigger(@RequestBody Map<String, Object> params) {
        //, @RequestParam("itemKey") String itemKey, @RequestParam(value = "typeId",required = false) String typeId, @RequestParam(value = "subtypeId",required = false) String subtypeId
        try{
            if(params!=null){
               /* params.put("typeId", typeId);
                params.put("subtypeId", subtypeId);
                params.put("valueType", valueType);*/
                List<Map<String, String>> resultList= hostService.getTop5ByTrigger(params);
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

    @PostMapping("/findHostIdinfo/{hostId}")
    public Result<HostEntity> findHostId(@PathVariable String hostId) {
        try{
            HostEntity bean = hostService.findHostIdinfo(hostId);
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
