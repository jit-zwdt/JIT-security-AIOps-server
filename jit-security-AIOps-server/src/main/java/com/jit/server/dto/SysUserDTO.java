package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Description: SysUserDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.19
 */
@Data
@AllArgsConstructor
public class SysUserDTO {

    private String id;

    private String username;

    private String name;

    private String workNo;

    private String mobile;

    private Integer sex;

    private LocalDate birth;

    private String picId;

    private String picUrl;

    private String departmentId;

    private String province;

    private String city;

    private String liveAddress;

    private String hobby;

    private String email;

    private Integer status;

}
