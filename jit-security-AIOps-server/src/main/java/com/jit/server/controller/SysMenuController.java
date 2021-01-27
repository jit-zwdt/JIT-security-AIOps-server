package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.dto.SysMenuDTO;
import com.jit.server.dto.SysMenuInfoDTO;
import com.jit.server.dto.SysMenuListDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysMenuEntity;
import com.jit.server.request.MenuParams;
import com.jit.server.service.SysMenuService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
    @AutoLog(value = "菜单管理-菜单列表", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getMenusList() {
        try {
            List<SysMenuListDTO> menus = sysMenuService.getMenusList();
            return Result.SUCCESS(menus);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping(value = "/getBySysMenu/{id}")
    @AutoLog(value = "菜单管理-详情", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getBySysMenu(@PathVariable String id) {
        try {
            if (StringUtils.isNotEmpty(id)) {
                SysMenuInfoDTO sysMenuInfoDTO = sysMenuService.findSysMenuInfoBySysMenuId(id);
                if (sysMenuInfoDTO != null) {
                    return Result.SUCCESS(sysMenuInfoDTO);
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
    @AutoLog(value = "菜单管理-新增", logType = ConstLogUtil.LOG_TYPE_OPERATION)
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
                sysMenuEntity.setGmtCreate(LocalDateTime.now());
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
                sysMenuEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
                sysMenuService.addSysMenu(sysMenuEntity);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping(value = "/updateMenus")
    @AutoLog(value = "菜单管理-编辑", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateMenus(@RequestBody SysMenuListDTO sysMenuListDTO) {
        try {
            if (sysMenuListDTO != null && StringUtils.isNotEmpty(sysMenuListDTO.getId())) {
                SysMenuEntity sysMenuEntity = sysMenuService.findBySysMenuId(sysMenuListDTO.getId());
                if (sysMenuEntity != null) {
                    BeanUtils.copyProperties(sysMenuListDTO, sysMenuEntity);
                    // 添加自己手动转类型的方法
                    sysMenuEntity.setIsShow(Integer.parseInt(sysMenuListDTO.getIsShow()));
                    sysMenuEntity.setIsRoute(Integer.parseInt(sysMenuListDTO.getIsRoute()));
                    sysMenuEntity.setOrderNum(Integer.parseInt(sysMenuListDTO.getOrderNum()));
                    sysMenuEntity.setParentId(sysMenuListDTO.getParentId());
                    if (StringUtils.isNotEmpty(sysMenuListDTO.getIcon())) {
                        sysMenuEntity.setIcon(sysMenuListDTO.getIcon());
                    } else {
                        sysMenuEntity.setIcon(ICON);
                    }
                    sysMenuEntity.setGmtModified(LocalDateTime.now());
                    sysMenuEntity.setUpdateBy(userService.findIdByUsername());
                    sysMenuService.updateSysMenu(sysMenuEntity);
                    return Result.SUCCESS(null);
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
    @AutoLog(value = "菜单管理-隐藏", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateIsShow(@PathVariable String id, @PathVariable int isShow) {
        try {
            //调用方法更新数据
            sysMenuService.updateIsShow(id, isShow);
            return Result.SUCCESS(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping(value = "/deleteMenus/{id}")
    @AutoLog(value = "菜单管理-删除", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteMenus(@PathVariable String id) {
        try {
            SysMenuEntity sysMenuEntity = sysMenuService.findBySysMenuId(id);
            if (sysMenuEntity != null) {
                sysMenuEntity.setGmtModified(LocalDateTime.now());
                sysMenuEntity.setUpdateBy(userService.findIdByUsername());
                sysMenuEntity.setIsDeleted(ConstUtil.IS_DELETED);
                sysMenuService.updateSysMenu(sysMenuEntity);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    /**
     * 根据该菜单名称是否有相同的菜单名称 如果有则返回 false 没有返回 true
     *
     * @param path    菜单路径
     * @param oldPath 旧的菜单路径
     * @return false 有 true 没有
     */
    @GetMapping("/getValidationPath")
    public Result getValidationPath(String path, String oldPath) {
        Boolean flag = sysMenuService.getValidationName(path, oldPath);
        return Result.SUCCESS(flag);
    }

    /**
     * 根据该组件名称是否有相同的组件名称 如果有则返回 false 没有返回 true
     *
     * @param name    组件名称
     * @param oldName 旧的组件名称
     * @return false 有 true 没有
     */
    @GetMapping("/getValidationName")
    public Result getValidationName(String name, String oldName) {
        Boolean flag = sysMenuService.getValidationTitle(name, oldName);
        return Result.SUCCESS(flag);
    }

    /**
     * 根据该组件路径是否有相同的组件路径 如果有则返回 false 没有返回 true
     *
     * @param component    组件路径
     * @param oldComponent 旧的组件路径
     * @return false 有 true 没有
     */
    @GetMapping("/getValidationComponent")
    public Result getValidationComponent(String component, String oldComponent) {
        Boolean flag = sysMenuService.getValidationComponent(component, oldComponent);
        return Result.SUCCESS(flag);
    }

    /**
     * 判断一级目录下是否有二级目录
     *
     * @param id 主键
     * @return
     */
    @PostMapping(value = "/judgeOfChild/{id}")
    public Result judgeOfChild(@PathVariable String id) {
        try {
            if (StringUtils.isNotEmpty(id)) {
                SysMenuEntity sysMenuEntity = sysMenuService.findBySysMenuId(id);
                if (sysMenuEntity != null) {
                    // 如果parenid 等于0 ，证明是一级菜单 ，需判断是否有二级菜单
                    if (sysMenuEntity.getParentId().equals("0")) {
                        // 当id 与 parenid 有相等的证明有二级菜单
                        List<SysMenuEntity> sysMenuEntities = sysMenuService.findByParentId(id);
                        if (sysMenuEntities != null && sysMenuEntities.size() > 0) {
                            return Result.SUCCESS(true);
                        }
                        return Result.SUCCESS(false);
                    } else {
                        return Result.SUCCESS(false);
                    }
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

    /**
     * 获取所有未删除的title
     *
     * @return
     */
    @GetMapping("/getMenuTitle")
    public Result getMenuTitle() {
        List<SysMenuEntity> sysMenuEntities = sysMenuService.getMenuTitle();
        return Result.SUCCESS(sysMenuEntities);
    }
}
