package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.ServerResource;
import com.jit.server.repository.ServerResourceRepo;
import com.jit.server.service.impl.AuthServiceImpl;
import com.jit.server.util.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@Api(value = "LoginController")
public class LoginController {
    @Autowired
    private ServerResourceRepo serverResourceRepo;

    @Autowired
    AuthServiceImpl authService;

    @PostMapping("/login")
    public Result login(@RequestParam(value = "username", required = true) String username,
                        @RequestParam(value = "password", required = true) String password, HttpServletResponse resp) throws IOException {
        String token = authService.login(username, password);
        if (null != token && !"".equals(token)) {
            return Result.SUCCESS(token);
        } else {
            return Result.ERROR(ExceptionEnum.LOGIN_EXCEPTION);
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
