package com.jit.server.controller;

import com.jit.server.dto.CronExpressionDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysCronExpressionEntity;
import com.jit.server.service.SysCronExpressionService;
import com.jit.server.service.UserService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
        //首先先进行数据的查询校验是否有描述相同的数据
        boolean flag = sysCronExpressionService.checkAddCronExpressionDesc(cronExpression.getCronExpressionDesc());
        // 如果返回为 true 表示有表达式一样的数据
        if(flag){
            return Result.ERROR(ExceptionEnum.CRON_EXPRESSION_DESC_DATA_EXISTS);
        }
        //在进行查询是否有表达式一样的数据
        flag = sysCronExpressionService.checkAddCronExpression(cronExpression.getCronExpression());
        // 如果返回为 true 表示有表达式一样的数据
        if(flag){
            return Result.ERROR(ExceptionEnum.CRON_EXPRESSION_DATA_EXISTS);
        }
        // 进行数据的添加
        SysCronExpressionEntity cronExpressionData = sysCronExpressionService.addCronExpression(cronExpression);
        if(cronExpressionData.getId() != null){
            return Result.SUCCESS(cronExpressionData);
        }
        return Result.ERROR(ExceptionEnum.SCHEDULER_CREATE_EXCEPTION);
    }

    /**
     * 根据 ID 删除一个时间表达式对象
     * @param id 需要删除的时间表达式的 ID
     * @return 统一封装对象
     */
    @DeleteMapping("delCronExpression/{id}")
    public Result delCronExpression(@PathVariable String id){
        try {
            sysCronExpressionService.delCronExpression(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
        return Result.SUCCESS(id);
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
