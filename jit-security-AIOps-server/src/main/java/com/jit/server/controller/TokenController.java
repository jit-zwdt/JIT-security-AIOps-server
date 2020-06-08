package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.util.JWTBuilder;
import com.jit.server.util.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/08 11:07
 */

@RestController
@RequestMapping("/token")
@Api(value = "TokenController to creat and check token")
public class TokenController {

    @ResponseBody
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @ApiOperation(value = "get token by username and passwd", notes = "username and passwd is necessary")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "username", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "passwd", value = "passwd", required = true, dataType = "String")
    })
    public ResponseEntity<JsonResult> getToken(@RequestParam String username, @RequestParam String passwd) {
        String token = JWTBuilder.createToken(username, passwd);
        JSONObject jsonObj = new JSONObject();
        JsonResult jsonResult = new JsonResult();
        jsonObj.put("token", token);
        jsonResult.setStatus("ok");
        jsonResult.setResult(jsonObj);
        return ResponseEntity.ok(jsonResult);
    }
}
