package com.jit.server.pojo;

/**
 * @Description table monitor_templates: table entity
 * @author zengxin_miao
 * @Date: 2020/06/17 09:40:43
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "monitor_templates")
public class MonitorTemplatesEntity {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 类型表主键
     */
    @Column(name = "type_id")
    private String typeId;

    /**
     * 子类型 关联类型表主键（多个逗号隔开）
     */
    @Column(name = "subtype_ids")
    private String subtypeIds;

    /**
     * 使用的模版
     */
    @Column(name = "templates")
    private String templates;

    /**
     * 删除标识 0：表示未删除， 1：表示删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private java.sql.Timestamp gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private java.sql.Timestamp gmtModified;

    /**
     * 帮助描述文档
     */
    @Column(name = "help_doc")
    private String helpDoc;

    /**
     * 标识
     */
    @Column(name = "temp_key", nullable = false, unique = true)
    private String tempKey;

    /**
     * 标识
     */
    @Column(name = "order_num")
    private int orderNum;

    /**
     * 图标 base64
     */
    @Column(name = "ico")
    private String ico;

}
