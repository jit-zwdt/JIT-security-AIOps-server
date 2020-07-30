package com.jit.server.controller;

import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.ProblemParams;
import com.jit.server.service.ProblemService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @PostMapping("/findByCondition")
    public Result findByCondition(@RequestBody ProblemParams params, HttpServletResponse resp) throws IOException {
        try {
            if(params != null && params.getSeverity() != null) {
                List<ZabbixProblemDTO> result = problemService.findByCondition(params);
                if(null != result && !CollectionUtils.isEmpty(result)) {
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findProblemHost")
    public Result findProblemHost(@RequestBody ProblemParams params, HttpServletResponse resp) throws IOException {
        try {
            List<ProblemHostDTO> result = problemService.findProblemHost(params);
            if (null != result && !CollectionUtils.isEmpty(result)) {
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
