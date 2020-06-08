package com.jit.server.util;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/08 11:09
 */
@Data
@NoArgsConstructor
public class JsonResult {
    private Object result;
    private String status;
}
