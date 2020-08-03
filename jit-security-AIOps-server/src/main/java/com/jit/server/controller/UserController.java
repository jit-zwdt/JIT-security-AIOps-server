package com.jit.server.controller;

import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.ProblemParams;
import com.jit.server.service.ProblemService;
import com.jit.server.service.UserService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.dto.ZabbixUserDTO;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/getUserInfo")
    public Result getUserInfo() throws IOException {
        try {
            List<ZabbixUserDTO> result = userService.getUserInfo();
            if (null != result && !CollectionUtils.isEmpty(result)) {
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
