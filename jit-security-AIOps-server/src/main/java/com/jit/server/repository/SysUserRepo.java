package com.jit.server.repository;

import com.jit.server.pojo.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/09 13:25
 */
@Repository
public interface SysUserRepo extends JpaRepository<SysUserEntity, String> {

}
