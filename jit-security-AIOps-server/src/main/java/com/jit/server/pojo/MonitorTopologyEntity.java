package com.jit.server.pojo;

/**
 * @Description table monitor_topology: table entity
 * @author jian_liu
 * @Date: 2020/12/23 14:48:43
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "monitor_topology")
public class MonitorTopologyEntity {

    /**
     * 主键
     */
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 参数
     */
    @Column(name = "json_param")
    private String jsonParam;

    /**
     * 名称
     */
    @Column(name = "info_name")
    private String infoName;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private java.time.LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private java.time.LocalDateTime gmtModified;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 是否删除 1:表示删除;0:表示未删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

    /**
     * 是否首页展示 1：展示 0：不展示
     */
    @Column(name = "home_page_display")
    private int homePageDisplay;

}
