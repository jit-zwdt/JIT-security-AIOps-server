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
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
    public Result findByCondition(@RequestBody PageRequest<AssetsParams> params, HttpServletResponse resp) throws IOException {
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
            /*if (StringUtils.isNotEmpty(id)) {
                assetsService.deleteAssets(id);
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }*/
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
    public Result findByConditionInfo() throws IOException {
        try {
            List<MonitorAssetsEntity> ListResult = assetsService.findByConditionInfo();
            if (null != ListResult) {
                List<Map> ListResult1 = new ArrayList<>();
                List<Map> ListResult2 = new ArrayList<>();
                List<Map> ListResult3 = new ArrayList<>();
                List<Map> ListResult4 = new ArrayList<>();
                for (MonitorAssetsEntity ret : ListResult) {
                    if ("1".equals(ret.getType())) {
                        Map map = new HashMap();
                        map.put("id", ret.getId());
                        map.put("name", ret.getName());
                        ListResult1.add(map);
                    } else if ("2".equals(ret.getType())) {
                        Map map = new HashMap();
                        map.put("id", ret.getId());
                        map.put("name", ret.getName());
                        ListResult2.add(map);
                    } else if ("3".equals(ret.getType())) {
                        Map map = new HashMap();
                        map.put("id", ret.getId());
                        map.put("name", ret.getName());
                        ListResult3.add(map);
                    } else if ("4".equals(ret.getType())) {
                        Map map = new HashMap();
                        map.put("id", ret.getId());
                        map.put("name", ret.getName());
                        ListResult4.add(map);
                    }

                }
                Map<String, Object> resultmap1 = new HashMap<>();
                resultmap1.put("id", "1");
                resultmap1.put("name", "网络设备");
                resultmap1.put("items", ListResult1);
                Map<String, Object> resultmap2 = new HashMap<>();
                resultmap2.put("id", "2");
                resultmap2.put("name", "通讯设备");
                resultmap2.put("items", ListResult2);
                Map<String, Object> resultmap3 = new HashMap<>();
                resultmap3.put("id", "3");
                resultmap3.put("name", "服务器");
                resultmap3.put("items", ListResult3);
                Map<String, Object> resultmap4 = new HashMap<>();
                resultmap4.put("id", "4");
                resultmap4.put("name", "云平台");
                resultmap4.put("items", ListResult4);
                List<Map<String, Object>> result = new ArrayList<>();

                result.add(resultmap1);
                result.add(resultmap2);
                result.add(resultmap3);
                result.add(resultmap4);
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
