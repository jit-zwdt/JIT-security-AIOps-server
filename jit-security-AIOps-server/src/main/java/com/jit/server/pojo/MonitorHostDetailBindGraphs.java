package com.jit.server.pojo;

/**
 * @Description table monitor_host_detail_bind_items: table entity
 * @author zengxin_miao
 * @Date: 2020/07/21 20:16:03
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "monitor_host_detail_bind_graphs")
public class MonitorHostDetailBindGraphs {

    /**
     * 主键
     */
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 主机id
     */
    @Column(name = "host_id")
    private String hostId;

    /**
     * 类型id
     */
    @Column(name = "type_id")
    private String typeId;

    /**
     * 图形id
     */
    @Column(name = "graph_id")
    private String graphId;

    /**
     * 监控项名称
     */
    @Column(name = "graph_name")
    private String graphName;

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

}
