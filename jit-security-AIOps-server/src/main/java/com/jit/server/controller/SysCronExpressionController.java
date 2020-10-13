package com.jit.server.controller;

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

}
