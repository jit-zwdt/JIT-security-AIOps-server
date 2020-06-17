package com.jit.server.controller;


import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.AssetsEntity;
import com.jit.server.pojo.Region;
import com.jit.server.request.AssetsParams;
import com.jit.server.service.AssetsService;
import com.jit.server.util.Params;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/06/16 11:09
 */

@Api(tags = "AssetsController")
@RestController
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    AssetsService assetsService;

    @ApiOperation(value = "获得资产信息列表（带分页）",notes = "用于资产信息列表的展示")
    @PostMapping("/findByCondition")
    public Result findByCondition(@RequestHeader String authorization, @RequestBody Params<AssetsParams> params, HttpServletResponse resp) throws IOException {
        try{
            if(params!=null){
                Page<AssetsEntity> pageResult= assetsService.findByCondition(params.getParam(),params.getPage(),params.getSize());
                if (null != pageResult) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("totalRow", pageResult.getTotalElements());
                    result.put("totalPage", pageResult.getTotalPages());
                    result.put("dataList", pageResult.getContent());
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addAssets")
    public Result addAssets(@RequestHeader String authorization, @RequestBody AssetsEntity assets) {
        try{
            if(assets!=null){
                assets.setGmtCreate(LocalDateTime.now());
                assets.setGmtModified(LocalDateTime.now());
                assets.setIsDeleted("0");
                assetsService.addAssets(assets);
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PutMapping("/updateAssets")
    public Result<AssetsEntity> updateAssets(@RequestHeader String authorization, @RequestBody AssetsEntity assets) {
        try{
            if(assets!=null){
                Optional<AssetsEntity> bean = assetsService.findByAssetsId(assets.getId());
                if (bean.isPresent()) {
                    if(assets.getGmtModified()==null){
                        assets.setGmtModified(LocalDateTime.now());
                    }
                    assetsService.updateAssets(assets);
                    return Result.SUCCESS(assets);
                }else{
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteAssets/{id}")
    public Result deleteAssets(@RequestHeader String authorization, @PathVariable String id) {
        try{
            /*if (StringUtils.isNotEmpty(id)) {
                assetsService.deleteAssets(id);
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }*/
            Optional<AssetsEntity> bean = assetsService.findByAssetsId(id);
            if (bean.isPresent()) {
                AssetsEntity assets = bean.get();
                assets.setGmtModified(LocalDateTime.now());
                assets.setIsDeleted("1");
                assetsService.updateAssets(assets);
                return Result.SUCCESS(assets);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findById/{id}")
    public Result<AssetsEntity> findById(@RequestHeader String authorization, @PathVariable String id) {
        try{
            Optional<AssetsEntity> bean = assetsService.findByAssetsId(id);
            if (bean.isPresent()) {
                return Result.SUCCESS(bean);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
