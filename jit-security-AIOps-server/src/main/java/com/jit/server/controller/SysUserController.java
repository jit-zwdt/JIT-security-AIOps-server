package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysUserEntity;
import com.jit.server.request.SysUserEntityParams;
import com.jit.server.service.SysUserService;
import com.jit.server.service.UserService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping(value = "/getUsers")
    public Result getUsers(@RequestBody PageRequest<SysUserEntityParams> params) {

        try {
            Page<SysUserEntity> sysUserEntities = sysUserService.getUsers(params);
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

    @PostMapping("/addUser")
    public Result addUser(@RequestBody SysUserEntity params) {
        try{
            return Result.SUCCESS(sysUserService.addUser(params));
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public Result deleteUser(@PathVariable String id) {
        try{
            Optional<SysUserEntity> bean = sysUserService.findById(id);
            if (bean.isPresent()) {
                SysUserEntity sysUserEntity = bean.get();
                sysUserEntity.setGmtModified(new java.sql.Timestamp(new Date().getTime()));
                sysUserEntity.setIsDeleted(1);
                SysUserEntity sysUser = sysUserService.addUser(sysUserEntity);
                return Result.SUCCESS(sysUser);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findUserById/{id}")
    public Result findUserById(@PathVariable String id) {
        try{
            Optional<SysUserEntity> bean = sysUserService.findById(id);
            if (bean.isPresent()) {
                SysUserEntity sysDictionaryEntity = bean.get();
                return Result.SUCCESS(sysDictionaryEntity);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
    @GetMapping("/getUserInfo")
    public Result getUserInfo(){
        try {
            String id = userService.findIdByUsername();
            Optional<SysUserEntity> bean = sysUserService.findById(id);
            if (bean.isPresent()) {
                SysUserEntity sysDictionaryEntity = bean.get();
                return Result.SUCCESS(sysDictionaryEntity);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/checkUserName/{username}")
    public Result checkUserName(@PathVariable String username) {
        try {
            if (StringUtils.isNotBlank(username)) {
                SysUserEntity sysUserEntity = sysUserService.getByUserName(username);
                if (sysUserEntity == null) {
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
}
