package com.jit.server.repository;

import com.jit.server.pojo.MonitorTemplates;
import com.jit.server.pojo.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020-06-17 09:46:28
 */
@Repository
public interface MonitorTemplatesRepo extends JpaRepository<MonitorTemplates, String> {
}
