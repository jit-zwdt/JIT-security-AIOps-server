package com.jit.server.repository;

import com.jit.server.pojo.SysQuartzJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Description: SysQuartzJobRepo
 * @Author: zengxin_miao
 * @Date: 2020/06/09 13:25
 */
@Repository
public interface SysQuartzJobRepo extends JpaRepository<SysQuartzJobEntity, String>, JpaSpecificationExecutor<SysQuartzJobEntity> {


    SysQuartzJobEntity findByIdAndIsDeleted(String id, int isDeleted);
}
