package com.jit.server.pojo;

/**
 * @Description table monitor_scheme_timer_task: table entity
 * @author jian_liu
 * @Date: 2020/10/19 10:08:05
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table ( name ="monitor_scheme_timer_task" )
public class MonitorSchemeTimerTaskEntity {

	@Id
	@Column(length = 32, nullable = false)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	/**
	 * 定时任务表ID
	 */
	@Column(name = "schedule_id")
	private String scheduleId;

	/**
	 * ftp路径
	 */
	@Column(name = "ftp_url")
	private String ftpUrl;

	/**
	 * 巡检计划名称
	 */
	@Column(name = "scheme_name")
	private String schemeName;

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
	 * 父级 ID 如果没有父级 ID 默认为 1
	 */
	@Column(name = "parent_id")
	private String parentId;

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
	private long isDeleted;

}
