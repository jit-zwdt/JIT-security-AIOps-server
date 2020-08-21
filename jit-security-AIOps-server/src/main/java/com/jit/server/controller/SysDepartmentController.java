package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysDepartmentEntity;
import com.jit.server.service.SysDepartmentService;
import com.jit.server.service.UserService;
import com.jit.server.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.08.18
 */

@RestController
@RequestMapping("/sys/department")
public class SysDepartmentController {

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping(value = "/getDepartmentInfos")
    public Result getDepartmentInfos() {
        try {
            return Result.SUCCESS(sysDepartmentService.getDepartmentInfos());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/addDepartment")
    public Result addDepartment(@RequestBody SysDepartmentEntity department) {
        try {
            if (department != null) {
                if (StringUtils.isBlank(department.getId())) {
                    if ("0".equals(department.getParentId())) {
                        department.setDepartType("1");
                    } else {
                        department.setDepartType("2");
                    }
                    department.setGmtCreate(new Timestamp(System.currentTimeMillis()));
                    department.setCreateBy(userService.findIdByUsername());
                } else {
                    department.setGmtModified(new Timestamp(System.currentTimeMillis()));
                    department.setUpdateBy(userService.findIdByUsername());
                }
                department.setIsDeleted(0);
                String id = sysDepartmentService.saveOrUpdateDepartment(department);
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

    @ResponseBody
    @GetMapping(value = "/getDepartment/{id}")
    public Result getDepartment(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                return Result.SUCCESS(sysDepartmentService.getDepartment(id));
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/delDepartment/{ids}")
    public Result delDepartment(@PathVariable String ids) {
        try {
            if (StringUtils.isNotBlank(ids)) {
                List<String> list = new ArrayList<>();
                for (String id : ids.split(",")) {
                    if (StringUtils.isNotBlank(id)) {
                        list.add(id);
                        list.addAll(getDelIds(id, new ArrayList<>()));
                    }
                }
                HashSet set = new HashSet(list);
                list.clear();
                list.addAll(set);
                sysDepartmentService.delDepartmentsByIds(list, new Timestamp(System.currentTimeMillis()), userService.findIdByUsername());
                return Result.SUCCESS("success");
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    private List<String> getDelIds(String id, List<String> list) throws Exception {
        List<String> ids = sysDepartmentService.getSubDepIds(id);
        if (ids != null && !ids.isEmpty()) {
            list.addAll(ids);
            for (String i : ids) {
                getDelIds(i, list);
            }
        }
        return list;
    }

    @ResponseBody
    @GetMapping(value = "/checkDepartCode/{code}")
    public Result checkDepartCode(@PathVariable String code) {
        try {
            if (StringUtils.isNotBlank(code)) {
                SysDepartmentEntity sysDepartmentEntity = sysDepartmentService.getDepartmentByDepartCode(code);
                if (sysDepartmentEntity == null) {
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