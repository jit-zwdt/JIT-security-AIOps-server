package com.jit.server.pojo;

/**
 * @Description table monitor_type: table entity
 * @author zengxin_miao
 * @Date: 2020/06/24 09:56:08
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "monitor_type")
public class MonitorTypeEntity {


    /**
     * 主键
     */
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 类型
     */
    @Column(name = "type")
    private String type;

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
     * 父id
     */
    @Column(name = "pid")
    private String pid;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private int orderNum;

}
