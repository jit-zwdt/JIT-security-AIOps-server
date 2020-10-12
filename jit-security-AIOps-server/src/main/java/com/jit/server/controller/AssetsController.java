package com.jit.server.controller;


import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorAssetsEntity;
import com.jit.server.request.AssetsParams;
import com.jit.server.service.AssetsService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/16 11:09
 */

@RestController
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    AssetsService assetsService;

    @PostMapping("/findByCondition")
    public Result findByCondition(@RequestBody PageRequest<AssetsParams> params, HttpServletResponse resp) {
        try {
            if (params != null) {
                Page<MonitorAssetsEntity> pageResult = assetsService.findByCondition(params.getParam(), params.getPage(), params.getSize());
                if (null != pageResult) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("totalRow", pageResult.getTotalElements());
                    result.put("totalPage", pageResult.getTotalPages());
                    result.put("dataList", pageResult.getContent());
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addAssets")
    public Result addAssets(@RequestBody MonitorAssetsEntity assets) {
        try {
            if (assets != null) {
                assets.setGmtCreate(LocalDateTime.now());
                assets.setGmtModified(LocalDateTime.now());
                assets.setAmount(1);
                assets.setIsDeleted(ConstUtil.IS_NOT_DELETED);
                assetsService.addAssets(assets);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PutMapping("/updateAssets/{id}")
    public Result<MonitorAssetsEntity> updateAssets(@RequestBody AssetsParams params, @PathVariable String id) {
        try {
            if (params != null && StringUtils.isNotEmpty(id)) {
                Optional<MonitorAssetsEntity> bean = assetsService.findByAssetsId(id);
                if (bean.isPresent()) {
                    MonitorAssetsEntity assets = bean.get();
                    BeanUtils.copyProperties(params, assets);
                    assets.setGmtModified(LocalDateTime.now());
                    assetsService.updateAssets(assets);
                    return Result.SUCCESS(assets);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteAssets/{id}")
    public Result deleteAssets(@PathVariable String id) {
        try {
            Optional<MonitorAssetsEntity> bean = assetsService.findByAssetsId(id);
            if (bean.isPresent()) {
                MonitorAssetsEntity assets = bean.get();
                assets.setGmtModified(LocalDateTime.now());
                assets.setIsDeleted(ConstUtil.IS_DELETED);
                assetsService.updateAssets(assets);
                return Result.SUCCESS(assets);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findById/{id}")
    public Result<MonitorAssetsEntity> findById(@PathVariable String id) {
        try {
            Optional<MonitorAssetsEntity> bean = assetsService.findByAssetsId(id);
            if (bean.isPresent()) {
                return Result.SUCCESS(bean);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findByConditionInfo")
    public Result findByConditionInfo() {
        try {
            List<Object> listResult = assetsService.findByConditionInfo();
            if (null != listResult && !listResult.isEmpty()) {
                return Result.SUCCESS(listResult);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @GetMapping("/getHardwareInfo")
    public Result getHardwareInfo() {
        try {
            List<Object> listResult = assetsService.getHardwareInfo();
            if (listResult != null && !listResult.isEmpty()) {
                return Result.SUCCESS(listResult);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
