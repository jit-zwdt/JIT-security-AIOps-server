package com.jit.server.controller;


import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.AssetsEntity;
import com.jit.server.service.AssetsService;
import com.jit.server.util.Params;
import com.jit.server.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public Result findByCondition(@RequestHeader String authorization,@RequestBody Params<AssetsEntity> params, HttpServletResponse resp) throws IOException {
        if(params!=null){
            try{
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
            }catch (Exception e){
                return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
            }
        }
        return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
    }
}
