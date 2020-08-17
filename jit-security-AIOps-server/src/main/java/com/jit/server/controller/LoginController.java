package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.service.AuthService;
import com.jit.server.util.JwtTokenDto;
import com.jit.server.util.Result;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LoginController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public Result login(@RequestParam(value = "username", required = true) String username,
                        @RequestParam(value = "password", required = true) String password, HttpServletResponse resp) throws IOException {
        JwtTokenDto token = authService.login(username, password);
        if (null != token) {
            return Result.SUCCESS(token);
        } else {
            return Result.ERROR(ExceptionEnum.LOGIN_EXCEPTION);
        }
    }

    @PostMapping("/refreshToken")
    public Result<JwtTokenDto> refreshToken(@RequestParam(value = "refresh_token", required = true) String refresh_token) {
        try {
            JwtTokenDto dto = authService.refreshToken(refresh_token);
            return Result.SUCCESS(dto);
        } catch (JwtException e) {
            return Result.ERROR(ExceptionEnum.TOKEN_EXCEPTION);
        }
    }

}
