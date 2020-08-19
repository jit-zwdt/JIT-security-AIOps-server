package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysDepartmentEntity;
import com.jit.server.service.DepartmentService;
import com.jit.server.service.UserService;
import com.jit.server.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.08.18
 */

@RestController
@RequestMapping("/sys/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping(value = "/getDepartmentInfos")
    public Result getDepartmentInfos() {
        try {
            return Result.SUCCESS(departmentService.getDepartmentInfos());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/addDepartment")
    public Result addDepartment(@RequestBody SysDepartmentEntity department) {
        try {
            if (department != null) {
                if(StringUtils.isBlank(department.getId())){
                    department.setGmtCreate(new Timestamp(System.currentTimeMillis()));
                    department.setCreateBy(userService.findIdByUsername());
                }else{
                    department.setGmtModified(new Timestamp(System.currentTimeMillis()));
                    department.setUpdateBy(userService.findIdByUsername());
                }
                department.setIsDeleted(0);
                String id = departmentService.saveOrUpdateDepartment(department);
                if (id != null) {
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


}