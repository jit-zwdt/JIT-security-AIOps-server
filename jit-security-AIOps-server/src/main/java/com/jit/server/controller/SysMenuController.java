package com.jit.server.controller;

import com.jit.server.dto.SysMenuDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.service.SysMenuService;
import com.jit.server.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @ResponseBody
    @GetMapping(value = "/getMenus")
    public Result getMenus() {
        try {
            List<SysMenuDTO> menus = sysMenuService.getMenus();
            return Result.SUCCESS(menus);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
