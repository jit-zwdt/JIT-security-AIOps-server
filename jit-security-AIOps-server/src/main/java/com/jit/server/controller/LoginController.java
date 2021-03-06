package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.exception.AccountDisabledException;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.service.AuthService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.*;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private AuthService authService;

    @Resource
    private CacheManager cacheManager;

    @PostMapping("/login")
    @AutoLog(value = "用户登录（带验证码）", logType = ConstLogUtil.LOG_TYPE_LOGIN)
    public Result login(@RequestParam(value = "username", required = true) String username,
                        @RequestParam(value = "password", required = true) String password,
                        @RequestParam(value = "verificationCode", required = true) String verificationCode,
                        @RequestParam(value = "verificationCodeKey", required = true) String verificationCodeKey,
                        HttpServletResponse resp) {
        try {
            //check verification code
            Cache cache = cacheManager.getCache("verificationCodeCache");
            String res = cache.get(verificationCodeKey, String.class);
            if (res != null) {
                verificationCode = verificationCode != null ? verificationCode.toLowerCase() : "NULL";
                if (!verificationCode.equals(res.toLowerCase())) {
                    return Result.ERROR(ExceptionEnum.LOGIN_VERIFICATION_CODE_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.LOGIN_VERIFICATION_CODE_EXPIRES_EXCEPTION);
            }
            // check token
            JwtTokenDto token = authService.login(username, password);
            if (null != token) {
                zabbixAuthService.setAuthToApplicationContext(token.getAccess_token());
                return Result.SUCCESS(token);
            } else {
                return Result.ERROR(ExceptionEnum.LOGIN_PASSWORD_OR_USRNAME_EXCEPTION);
            }
        } catch (AccountDisabledException e) {
            return Result.ERROR(ExceptionEnum.ACCOUNT_DISABLED_EXPIRES_EXCEPTION);
        } catch (BadCredentialsException e) {
            return Result.ERROR(ExceptionEnum.LOGIN_PASSWORD_OR_USRNAME_EXCEPTION);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.LOGIN_EXCEPTION);
        }
    }

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @PostMapping("/loginWithOutVerificationCode")
    @AutoLog(value = "用户登录（不带验证码）", logType = ConstLogUtil.LOG_TYPE_LOGIN)
    public Result loginWithOutVerificationCode(@RequestParam(value = "username", required = true) String username,
                                               @RequestParam(value = "password", required = true) String password,
                                               HttpServletResponse resp) {
        try {
            JwtTokenDto token = authService.login(username, password);
            if (null != token) {
                zabbixAuthService.setAuthToApplicationContext(token.getAccess_token());
                return Result.SUCCESS(token);
            } else {
                return Result.ERROR(ExceptionEnum.LOGIN_PASSWORD_OR_USRNAME_EXCEPTION);
            }
        } catch (AccountDisabledException e) {
            return Result.ERROR(ExceptionEnum.ACCOUNT_DISABLED_EXPIRES_EXCEPTION);
        } catch (BadCredentialsException e) {
            return Result.ERROR(ExceptionEnum.LOGIN_PASSWORD_OR_USRNAME_EXCEPTION);
        } catch (Exception e) {
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

    /**
     * 获取校验码
     */
    @GetMapping(value = "/getCheckCode")
    public Result<Map<String, String>> getCheckCode() {
        Map<String, String> map = new HashMap<>(2);
        try {
            Cache cache = cacheManager.getCache("verificationCodeCache");
            String code = RandomUtil.randomString(4);
            String key = MD5Util.MD5Encode(code + System.currentTimeMillis(), "utf-8");
            map.put("key", key);
            map.put("code", code);
            cache.put(key, code);
            return Result.SUCCESS(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

}
