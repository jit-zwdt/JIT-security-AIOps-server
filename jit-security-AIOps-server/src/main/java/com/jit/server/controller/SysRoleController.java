package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.dto.TransferDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysRoleEntity;
import com.jit.server.pojo.SysRoleMenuEntity;
import com.jit.server.pojo.SysUserRoleEntity;
import com.jit.server.request.RoleParams;
import com.jit.server.service.SysRoleService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping(value = "/getPageRoles")
    @AutoLog(value = "角色维护-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getPageRoles(@RequestBody PageRequest<Map<String, Object>> params) {
        try {
            Page<SysRoleEntity> sysUserEntities = sysRoleService.getRoles(params);
            Map<String, Object> result = new HashMap<>(5);
            result.put("page", params.getPage());
            result.put("size", params.getSize());
            result.put("totalRow", sysUserEntities.getTotalElements());
            result.put("totalPage", sysUserEntities.getTotalPages());
            result.put("dataList", sysUserEntities.getContent());
            return Result.SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/checkRoleName/{name}")
    public Result checkRoleName(@PathVariable String name) {
        try {
            if (StringUtils.isNotBlank(name)) {
                SysRoleEntity sysRoleEntity = sysRoleService.getRoleByRoleName(name);
                if (sysRoleEntity == null) {
                    return Result.SUCCESS(false);
                }
                return Result.SUCCESS(true);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }


    @ResponseBody
    @GetMapping(value = "/checkRoleSign/{sign}")
    public Result checkRoleSign(@PathVariable String sign) {
        try {
            if (StringUtils.isNotBlank(sign)) {
                SysRoleEntity sysRoleEntity = sysRoleService.getRoleByRoleSign(sign);
                if (sysRoleEntity == null) {
                    return Result.SUCCESS(false);
                }
                return Result.SUCCESS(true);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/addRole")
    @AutoLog(value = "角色维护-新增", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addRole(@RequestBody RoleParams roleParams) {
        try {
            if (roleParams != null) {
                String id = saveRole(roleParams);
                if (id != null && !id.isEmpty()) {
                    return Result.SUCCESS("success");
                } else {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateRole")
    @AutoLog(value = "角色维护-编辑", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateRole(@RequestBody RoleParams roleParams) {
        try {
            if (roleParams != null) {
                String id = saveRole(roleParams);
                if (id != null && !id.isEmpty()) {
                    return Result.SUCCESS("success");
                } else {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getRole/{id}")
    public Result getRole(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                SysRoleEntity sysRoleEntity = sysRoleService.findByIdAndIsDeleted(id);
                if (sysRoleEntity == null) {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
                return Result.SUCCESS(sysRoleEntity);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteRole/{id}")
    @AutoLog(value = "角色维护-删除菜单", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteRole(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                SysRoleEntity sysRoleEntity = sysRoleService.findByIdAndIsDeleted(id);
                if (sysRoleEntity == null) {
                    return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
                } else {
                    sysRoleEntity.setIsDeleted(ConstUtil.IS_DELETED);
                    sysRoleEntity.setGmtModified(LocalDateTime.now());
                    sysRoleEntity.setUpdateBy(userService.findIdByUsername());
                    sysRoleService.saveOrUpdateRole(sysRoleEntity);
                    return Result.SUCCESS(true);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getUsers")
    public Result getUsers() {
        try {
            List<TransferDTO> object = sysRoleService.getUsers();
            if (object == null) {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
            return Result.SUCCESS(object);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getRoleUsers/{id}")
    public Result getRoleUsers(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                List<String> object = sysRoleService.getRoleUsers(id);
                return Result.SUCCESS(object);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PostMapping(value = "/bindingUsers")
    @AutoLog(value = "角色维护-绑定人员", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result bindingUsers(@RequestBody Map<String, Object> params) {
        try {
            String roleId = params.get("roleId") != null ? params.get("roleId").toString() : "";
            List<String> value = params.get("value") != null ? (List<String>) params.get("value") : null;
            if (params != null) {
                if (StringUtils.isNotBlank(roleId)) {
                    //del removed users
                    List<SysUserRoleEntity> sysUserRoleEntityList = sysRoleService.getSysUserRolesByRoleId(roleId);
                    List<String> exList = new ArrayList<>();
                    if (sysUserRoleEntityList != null && !sysUserRoleEntityList.isEmpty()) {
                        String userId = "";
                        for (SysUserRoleEntity sysUserRoleEntity : sysUserRoleEntityList) {
                            userId = sysUserRoleEntity.getUserId();
                            if (!value.contains(userId)) {
                                sysUserRoleEntity.setIsDeleted(ConstUtil.IS_DELETED);
                                sysUserRoleEntity.setGmtModified(LocalDateTime.now());
                                sysUserRoleEntity.setUpdateBy(userService.findIdByUsername());
                                sysRoleService.saveOrUpdateUserRole(sysUserRoleEntity);
                            } else {
                                exList.add(userId);
                            }
                        }
                    }
                    if (value != null && !value.isEmpty()) {
                        value.removeAll(exList);
                        //add new users
                        for (String userId : value) {
                            SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
                            sysUserRoleEntity.setRoleId(roleId);
                            sysUserRoleEntity.setUserId(userId);
                            sysUserRoleEntity.setCreateBy(userService.findIdByUsername());
                            sysUserRoleEntity.setGmtCreate(LocalDateTime.now());
                            sysUserRoleEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
                            sysRoleService.saveOrUpdateUserRole(sysUserRoleEntity);
                        }
                    }
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getMenus")
    public Result geMenus() {
        try {
            return Result.SUCCESS(sysRoleService.getMenus());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getRoleMenus/{id}")
    public Result getRoleMenus(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                return Result.SUCCESS(sysRoleService.getRoleMenus(id));
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PostMapping(value = "/bindingMenus")
    @AutoLog(value = "角色维护-绑定菜单", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result bindingMenus(@RequestBody Map<String, Object> params) {
        try {
            String roleId = params.get("roleId") != null ? params.get("roleId").toString() : "";
            List<String> keys = params.get("keys") != null ? (List<String>) params.get("keys") : null;
            if (params != null) {
                if (StringUtils.isNotBlank(roleId)) {
                    //get Level 1 menu sids
                    List<String> sids = sysRoleService.getLevelOneMenuSids();
                    //remove  Level 1 menu sids
                    sids.retainAll(keys);
                    keys.removeAll(sids);
                    //add
                    SysRoleMenuEntity sysRoleMenuEntity = sysRoleService.getRoleMenuByRoleId(roleId);
                    if (sysRoleMenuEntity != null) {
                        sysRoleMenuEntity.setMenuId(keys != null ? StringUtils.join(keys, ",") : "");
                        sysRoleMenuEntity.setGmtModified(LocalDateTime.now());
                        sysRoleMenuEntity.setUpdateBy(userService.findIdByUsername());
                    } else {
                        sysRoleMenuEntity = new SysRoleMenuEntity();
                        sysRoleMenuEntity.setRoleId(roleId);
                        sysRoleMenuEntity.setMenuId(keys != null ? StringUtils.join(keys, ",") : "");
                        sysRoleMenuEntity.setGmtCreate(LocalDateTime.now());
                        sysRoleMenuEntity.setCreateBy(userService.findIdByUsername());
                    }
                    sysRoleService.saveOrUpdateRoleMenu(sysRoleMenuEntity);
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/getRoles")
    public Result getRoles() {
        try {
            return Result.SUCCESS(sysRoleService.findByIsDeleted());

        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    /**
     * 保存角色 包括添加和修改的方法
     * @param roleParams role 参数
     * @return role id
     * @throws Exception
     */
    private String saveRole(RoleParams roleParams) throws Exception {
        String id = roleParams.getId();
        if (StringUtils.isBlank(id)) {
            SysRoleEntity role = new SysRoleEntity();
            role.setRoleName(roleParams.getRoleName());
            role.setRoleSign(roleParams.getRoleSign());
            role.setRemark(roleParams.getRemark());
            role.setIsDeleted(ConstUtil.IS_NOT_DELETED);
            role.setGmtCreate(LocalDateTime.now());
            role.setCreateBy(userService.findIdByUsername());
            id = sysRoleService.saveOrUpdateRole(role).getId();
        } else {
            SysRoleEntity role = sysRoleService.findByIdAndIsDeleted(id);
            if (role != null) {
                role.setRoleName(roleParams.getRoleName());
                role.setRoleSign(roleParams.getRoleSign());
                role.setRemark(roleParams.getRemark());
                role.setGmtModified(LocalDateTime.now());
                role.setUpdateBy(userService.findIdByUsername());
                id = sysRoleService.saveOrUpdateRole(role).getId();
            }
        }
        return id;
    }
}
