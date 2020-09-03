package com.jit.server.controller;

import com.jit.server.dto.SysMenuDTO;
import com.jit.server.dto.SysMenuListDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysMenuEntity;
import com.jit.server.request.MenuParams;
import com.jit.server.service.SysMenuService;
import com.jit.server.service.UserService;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private UserService userService;
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String ZERO = "0";
    public static final String ICON = "el-icon-monitor";

    @ResponseBody
    @GetMapping(value = "/getMenus")
    public Result getMenus() {
        try {
            List<SysMenuDTO> menus = sysMenuService.getMenus(userService.findIdByUsername());
            return Result.SUCCESS(menus);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getMenusList")
    public Result getMenusList() {
        try {
            List<SysMenuListDTO> menus = sysMenuService.getMenusList();
            return Result.SUCCESS(menus);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping(value = "/findBySysMenu/{id}")
    public Result findBySysMenu(@PathVariable String id) {
        try {
            if (StringUtils.isNotEmpty(id)) {
                Optional<SysMenuEntity> bean = sysMenuService.findBySysMenuId(id);
                if (bean.isPresent()) {
                    SysMenuEntity sysMenuEntity = bean.get();
                    return Result.SUCCESS(sysMenuEntity);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping(value = "/getSysMenuFirst")
    public Result getSysMenuFirst() {
        try {
            List<SysMenuListDTO> menus = sysMenuService.getMenusFirst();
            return Result.SUCCESS(menus);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping(value = "/addMenus")
    public Result addMenus(@RequestBody MenuParams menuParams) {
        try {
            if (menuParams != null) {
                SysMenuEntity sysMenuEntity = new SysMenuEntity();
                if (ONE.equals(menuParams.getStatus())) {
                    sysMenuEntity.setParentId(ZERO);
                } else if (TWO.equals(menuParams.getStatus())) {
                    sysMenuEntity.setParentId(menuParams.getPid());
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
                sysMenuEntity.setGmtCreate(new Timestamp(System.currentTimeMillis()));
                sysMenuEntity.setCreateBy(userService.findIdByUsername());
                sysMenuEntity.setName(menuParams.getName());
                sysMenuEntity.setTitle(menuParams.getTitle());
                sysMenuEntity.setPath(menuParams.getPath());
                sysMenuEntity.setComponent(menuParams.getComponent());
                if (StringUtils.isNotEmpty(menuParams.getIcon())) {
                    sysMenuEntity.setIcon(menuParams.getIcon());
                } else {
                    sysMenuEntity.setIcon(ICON);
                }
                sysMenuEntity.setIsShow(Integer.parseInt(menuParams.getIsShow()));
                sysMenuEntity.setOrderNum(Integer.parseInt(menuParams.getOrderNum()));
                sysMenuEntity.setRedirect(menuParams.getRedirect());
                sysMenuEntity.setIsRoute(Integer.parseInt(menuParams.getIsRoute()));
                sysMenuEntity.setIsDeleted(0);
                sysMenuService.addSysMenu(sysMenuEntity);
                return Result.SUCCESS(sysMenuEntity);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping(value = "/updateMenus")
    public Result updateMenus(@RequestBody SysMenuListDTO sysMenuListDTO) {
        try {
            if (sysMenuListDTO != null && StringUtils.isNotEmpty(sysMenuListDTO.getId())) {
                Optional<SysMenuEntity> bean = sysMenuService.findBySysMenuId(sysMenuListDTO.getId());
                if (bean.isPresent()) {
                    SysMenuEntity sysMenuEntity = bean.get();
                    BeanUtils.copyProperties(sysMenuListDTO, sysMenuEntity);
                    sysMenuEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                    sysMenuEntity.setUpdateBy(userService.findIdByUsername());
                    sysMenuService.updateSysMenu(sysMenuEntity);
                    return Result.SUCCESS(sysMenuEntity);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PutMapping("updateIsShow/{id}/{isShow}")
    public Result updateIsShow(@PathVariable String id ,@PathVariable int isShow){
        try {
            //调用方法更新数据
            SysMenuEntity sysMenuEntity = sysMenuService.updateIsShow(id, isShow);
            return Result.SUCCESS(sysMenuEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping(value = "/delMenus/{id}")
    public Result delMenus(@PathVariable String id) {
        try {
            Optional<SysMenuEntity> bean = sysMenuService.findBySysMenuId(id);
            if (bean.isPresent()) {
                SysMenuEntity sysMenuEntity = bean.get();
                sysMenuEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
                sysMenuEntity.setUpdateBy(userService.findIdByUsername());
                sysMenuEntity.setIsDeleted(1);
                sysMenuService.updateSysMenu(sysMenuEntity);
                return Result.SUCCESS(sysMenuEntity);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
