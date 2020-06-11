package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.ServerResource;
import com.jit.server.repository.ServerResourceRepo;
import com.jit.server.service.AuthService;
import com.jit.server.util.JwtTokenDto;
import com.jit.server.util.Result;
import io.jsonwebtoken.JwtException;
<<<<<<<<< Temporary merge branch 1
=========
import io.swagger.annotations.Api;
>>>>>>>>> Temporary merge branch 2
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
<<<<<<<<< Temporary merge branch 1
=========
@Api(value = "LoginController", tags = "LoginController")
>>>>>>>>> Temporary merge branch 2
public class LoginController {
    @Autowired
    private ServerResourceRepo serverResourceRepo;
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

    @PostMapping("/server")
    public Result create(@RequestBody ServerResource serverResource) {
        ServerResource result = serverResourceRepo.save(serverResource);
        if (result != null) {
            return Result.SUCCESS(null);
        } else {
            return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
        }
    }

    @GetMapping("/server")
    public Result<List<ServerResource>> getServerList() {
        List<ServerResource> list = serverResourceRepo.findAll();
        return Result.SUCCESS(list);
    }

    @DeleteMapping("/server/{id}")
    public Result delete(@PathVariable Long id) {
        if (id != null) {
            this.serverResourceRepo.deleteById(id);
            return Result.SUCCESS(null);
        } else {
            return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
        }
    }

    @GetMapping("/server/{id}")
    public Result<ServerResource> getServer(@PathVariable Long id) {
        if (id != null) {
            ServerResource item = this.serverResourceRepo.findByServerId(id);
            return Result.SUCCESS(item);
        } else {
            return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
        }
    }

    @PutMapping("/server/{id}")
    public Result updateServer(@RequestBody ServerResource serverResource, @PathVariable Long id) {
        if (id != null && serverResource != null) {
            this.serverResourceRepo.save(serverResource);
            return Result.SUCCESS(null);
        } else {
            return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
        }

    }


}
