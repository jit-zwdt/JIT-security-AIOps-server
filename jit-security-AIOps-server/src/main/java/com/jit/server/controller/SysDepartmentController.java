package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysDepartmentEntity;
import com.jit.server.service.SysDepartmentService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.08.18
 */

@Slf4j
@RestController
@RequestMapping("/sys/department")
public class SysDepartmentController {

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping(value = "/getDepartmentInfos")
    @AutoLog(value = "部门管理-查询所有部门树", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getDepartmentInfos() {
        try {
            return Result.SUCCESS(sysDepartmentService.getDepartmentInfos());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/addDepartment")
    @AutoLog(value = "部门管理-添加部门", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addDepartment(@RequestBody SysDepartmentEntity department) {
        try {
            if (department != null) {
                //调用更新添加的方法
                String id = saveDepartment(department);
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

    @PostMapping("/updateDepartment")
    @AutoLog(value = "部门管理-修改部门", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateDepartment(@RequestBody SysDepartmentEntity department) {
        try {
            if (department != null) {
                //调用更新添加的方法
                String id = saveDepartment(department);
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
    @GetMapping(value = "/getDepartment/{id}")
    @AutoLog(value = "部门管理-详细信息", logType = ConstLogUtil.LOG_TYPE_OPERATION)
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
    @DeleteMapping(value = "/deleteDepartment/{ids}")
    @AutoLog(value = "部门管理-批量删除", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteDepartment(@PathVariable String ids) {
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
                sysDepartmentService.delDepartmentsByIds(list, LocalDateTime.now(), userService.findIdByUsername());
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

    @ResponseBody
    @GetMapping(value = "/getAllDepartment")
    public Result getAllDepartment() {
        try {
            return Result.SUCCESS(sysDepartmentService.getAllDepartment());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    /**
     * 修改或者添加部门
     * @param department 需要进行添加或者修改的部门数据 添加没有实体类的 id
     * @return 部门 ID
     */
    private String saveDepartment(SysDepartmentEntity department) throws Exception {
        String id = "";
        if (StringUtils.isBlank(department.getId())) {
            if ("0".equals(department.getParentId())) {
                department.setDepartType("1");
            } else {
                department.setDepartType("2");
            }
            department.setGmtCreate(LocalDateTime.now());
            department.setCreateBy(userService.findIdByUsername());
            department.setIsDeleted(ConstUtil.IS_NOT_DELETED);
            id = sysDepartmentService.saveOrUpdateDepartment(department);
        } else {
            SysDepartmentEntity sysDepartmentEntity = sysDepartmentService.getDepartment(department.getId());
            if (sysDepartmentEntity != null) {
                sysDepartmentEntity.setDepartName(department.getDepartName());
                sysDepartmentEntity.setDepartCode(department.getDepartCode());
                sysDepartmentEntity.setMobile(department.getMobile());
                sysDepartmentEntity.setFax(department.getFax());
                sysDepartmentEntity.setAddress(department.getAddress());
                sysDepartmentEntity.setDepartCategory(department.getDepartCategory());
                sysDepartmentEntity.setStatus(department.getStatus());
                sysDepartmentEntity.setDepartNameEn(department.getDepartNameEn());
                sysDepartmentEntity.setDepartNameAbbr(department.getDepartNameAbbr());
                sysDepartmentEntity.setRemark(department.getRemark());
                sysDepartmentEntity.setDepartOrder(department.getDepartOrder());
                sysDepartmentEntity.setDescription(department.getDescription());
                sysDepartmentEntity.setGmtModified(LocalDateTime.now());
                sysDepartmentEntity.setUpdateBy(userService.findIdByUsername());
                id = sysDepartmentService.saveOrUpdateDepartment(sysDepartmentEntity);
            }
        }
        return id;
    }
}
