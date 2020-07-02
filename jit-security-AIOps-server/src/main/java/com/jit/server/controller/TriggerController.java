package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.request.HostParams;
import com.jit.server.request.HostViewInfoParams;
import com.jit.server.request.TriggerParams;
import com.jit.server.service.HostService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.model.trigger.ZabbixTrigger;
import com.jit.zabbix.client.request.ZabbixGetTriggerParams;
import com.jit.zabbix.client.service.ZabbixApiService;
import com.jit.zabbix.client.service.ZabbixHostService;
import com.jit.zabbix.client.service.ZabbixTriggerService;
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
 * @Date: 2020/07/02 8:42
 */

@RestController
@RequestMapping("/trigger")
public class TriggerController {
    @Autowired
    private ZabbixAuthService zabbixAuthService;
    @Autowired
    private ZabbixTriggerService zabbixTriggerService;

    @PostMapping("/findByCondition")
    public Result findByCondition(@RequestBody PageRequest<TriggerParams> params, HttpServletResponse resp) throws IOException {
        try{
            if(params!=null&&params.getParam().getHostId()!=null){
                //获得token
                String authToken = zabbixAuthService.getAuth();
                if(StringUtils.isEmpty(authToken)){
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
                ZabbixGetTriggerParams _params = new ZabbixGetTriggerParams();
                _params.setHostIds(Arrays.asList(new String[]{params.getParam().getHostId()}));
                if(params.getParam().getDescription()!=null&&!"".equals(params.getParam().getDescription().trim())){
                    Map<String, Object> search = new HashMap<>();
                    search.put("description",params.getParam().getDescription().trim());
                    _params.setSearch(search);
                }
                if(params.getParam().getStatus()!=null&&("0".equals(params.getParam().getStatus().trim())||"1".equals(params.getParam().getStatus().trim()))){
                    Map<String, Object> filter = new HashMap<>();
                    filter.put("status",Integer.parseInt(params.getParam().getStatus().trim()));
                    _params.setFilter(filter);
                }

                List<ZabbixTrigger> _result= zabbixTriggerService.get(_params, authToken);
                if (null != _result && !CollectionUtils.isEmpty(_result)) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    //result.put("totalRow", 15);
                    //result.put("totalPage", 1);
                    result.put("dataList", _result);
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
