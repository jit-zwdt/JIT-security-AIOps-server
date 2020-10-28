package com.jit.server.controller;

import com.jit.server.dto.CronExpressionDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysCronExpressionEntity;
import com.jit.server.service.SysCronExpressionService;
import com.jit.server.service.UserService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/cronExpression")
public class SysCronExpressionController {

    @Autowired
    private SysCronExpressionService sysCronExpressionService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping(value = "/getCronExpressions")
    public Result getCronExpressions(@RequestBody PageRequest<Map<String, Object>> params) {
        try {
            Page<SysCronExpressionEntity> sysCronExpressionEntities = sysCronExpressionService.getCronExpressions(params);
            Map<String, Object> result = new HashMap<>(5);
            result.put("page", params.getPage());
            result.put("size", params.getSize());
            result.put("totalRow", sysCronExpressionEntities.getTotalElements());
            result.put("totalPage", sysCronExpressionEntities.getTotalPages());
            result.put("dataList", sysCronExpressionEntities.getContent());
            return Result.SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    /**
     * 添加一个时间表达式对象
     * @param cronExpression 时间表达式对戏
     * @return 统一封装对象
     */
    @PostMapping("/addCronExpression")
    public Result addCronExpression(@RequestBody SysCronExpressionEntity cronExpression){
        System.out.println(cronExpression);
        SysCronExpressionEntity cronExpressionData = sysCronExpressionService.addCronExpression(cronExpression);
        if(cronExpressionData.getId() != null){
            return Result.SUCCESS(cronExpressionData);
        }
        return Result.ERROR(ExceptionEnum.SCHEDULER_CREATE_EXCEPTION);
    }

    /**
     * 获取所有的巡检时间数据
     * @return 统一返回对象 封装了巡检时间数据对象集合
     */
    @PostMapping("/getCronExpressionObject")
    public Result getCronExpressionObject(){
        try {
            List<CronExpressionDTO> cronExpressionObject = sysCronExpressionService.getCronExpressionObject();
            return Result.SUCCESS(cronExpressionObject);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }
}
