package com.jit.server.controller;

import com.jit.server.dto.TransferDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysRoleEntity;
import com.jit.server.request.RoleParams;
import com.jit.server.service.SysRoleService;
import com.jit.server.service.UserService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping(value = "/getRoles")
    public Result getRoles(@RequestBody PageRequest<Map<String, Object>> params) {
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
    public Result addRole(@RequestBody RoleParams roleParams) {
        try {
            if (roleParams != null) {
                String id = roleParams.getId();
                if (StringUtils.isBlank(id)) {
                    SysRoleEntity role = new SysRoleEntity();
                    role.setRoleName(roleParams.getRoleName());
                    role.setRoleSign(roleParams.getRoleSign());
                    role.setRemark(roleParams.getRemark());
                    role.setIsDeleted(0);
                    role.setGmtCreate(new Timestamp(System.currentTimeMillis()));
                    role.setCreateBy(userService.findIdByUsername());
                    id = sysRoleService.saveOrUpdateRole(role).getId();
                } else {
                    SysRoleEntity role = sysRoleService.findByIdAndIsDeleted(id);
                    if (role != null) {
                        role.setRoleName(roleParams.getRoleName());
                        role.setRoleSign(roleParams.getRoleSign());
                        role.setRemark(roleParams.getRemark());
                        role.setGmtModified(new Timestamp(System.currentTimeMillis()));
                        role.setUpdateBy(userService.findIdByUsername());
                        id = sysRoleService.saveOrUpdateRole(role).getId();
                    } else {
                        return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                    }
                }
                if (id != null && !"".equals(id)) {
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
    @DeleteMapping(value = "/delRole/{id}")
    public Result delRole(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                SysRoleEntity sysRoleEntity = sysRoleService.findByIdAndIsDeleted(id);
                if (sysRoleEntity == null) {
                    return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
                } else {
                    sysRoleEntity.setIsDeleted(1);
                    sysRoleEntity.setGmtModified(new Timestamp(System.currentTimeMillis()));
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
                if (object == null) {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
                return Result.SUCCESS(object);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }
}
