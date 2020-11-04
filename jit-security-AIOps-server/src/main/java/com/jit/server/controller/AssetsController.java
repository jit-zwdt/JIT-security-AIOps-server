package com.jit.server.controller;


import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorAssetsEntity;
import com.jit.server.request.AssetsParams;
import com.jit.server.service.AssetsService;
import com.jit.server.service.HostService;
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

    @Autowired
    HostService hostService;

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
            MonitorAssetsEntity monitorAssetsEntity = bean.get();
            //判断是否是硬件 如果是硬件进行进一步的判断进行删除 0 是硬件
            if(monitorAssetsEntity.getType().equals("0")){
                // 根据 IP 查询是否有对应的数据 如果有的话进行返回错误的判断
                boolean flag = hostService.findByHostJmxIp(monitorAssetsEntity.getIp());
                //如果存在数据直接返回 Error 错误
                if(flag){
                    return Result.ERROR(ExceptionEnum.ASSET_UNDER_MONITORING_EXISTS);
                }
            }
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

    /**
     * 根据传入 ip 的值判断是否有这条数据 如果有 返回 true 如果没有 返回 false
     * @param ip
     * @return
     */
    @GetMapping("/validateIp")
    public Result validateIp(String ip){
        // 调用方法验证 Ip 是否具有这个 Ip 地址
        boolean flag = assetsService.validateIp(ip);
        // 返回成功对象 flag 是业务层传输的值
        return Result.SUCCESS(flag);
    }
}
