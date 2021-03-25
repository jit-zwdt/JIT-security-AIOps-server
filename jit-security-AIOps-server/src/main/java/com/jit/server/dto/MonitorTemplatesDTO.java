package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: MonitorTemplatesDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorTemplatesDTO {

    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型表主键
     */
    private String typeId;

    /**
     * 子类型 关联类型表主键（多个逗号隔开）
     */
    private String subtypeIds;

    /**
     * 使用的模版ids
     */
    private String templateIds;

    /**
     * 使用的模版
     */
    private String templates;


    /**
     * 帮助描述文档
     */
    private String helpDoc;

    /**
     * 标识
     */
    private String tempKey;

    /**
     * 标识
     */
    private int orderNum;

    /**
     * 图标 base64
     */
    private String ico;

    private LocalDateTime gmtCreate;

}
