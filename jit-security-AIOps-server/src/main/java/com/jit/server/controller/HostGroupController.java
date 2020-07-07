package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixHostGroupDTO;
import com.jit.zabbix.client.request.ZabbixGetHostGroupParams;
import com.jit.zabbix.client.service.ZabbixHostGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.06.29
 */

@RestController
@RequestMapping("/hostGroup")
public class HostGroupController {

    @Autowired
    private ZabbixHostGroupService zabbixHostGroupService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;


    /**
     * get zabbix hostGroup
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getZabbixHostGroup")
    public Result getZabbixHostGroup() {
        try {
            String auth = zabbixAuthService.getAuth();
            ZabbixGetHostGroupParams params = new ZabbixGetHostGroupParams();
            params.setOutput("extend");
            List<ZabbixHostGroupDTO> zabbixHostGroupDTOList = zabbixHostGroupService.get(params, auth);
            return Result.SUCCESS(zabbixHostGroupDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }
}