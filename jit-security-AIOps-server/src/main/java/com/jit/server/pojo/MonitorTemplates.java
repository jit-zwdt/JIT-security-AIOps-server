package com.jit.server.pojo;

/**
 * @Description table monitor_templates: table entity
 * @author zengxin_miao
 * @Date: 2020/06/17 09:40:43
 */

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "monitor_templates")
public class MonitorTemplates {

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
     * 类型 1：操作系统；2：数据库；3：中间件；4：网络设备；5：硬件；6：虚拟化；7：云平台
     */
    @Column(name = "type")
    private String type;

    /**
     * 帮助描述
     */
    @Column(name = "help")
    private String help;

    /**
     * 使用的模版
     */
    @Column(name = "user_template")
    private String userTemplate;

    /**
     * 删除标识 0：表示未删除， 1：表示删除
     */
    @Column(name = "is_deleted")
    private long isDeleted;

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
