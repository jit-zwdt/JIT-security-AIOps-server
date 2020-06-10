package com.jit.server.repository;

import com.jit.server.pojo.SysUserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepo extends JpaRepository<SysUserRoleEntity,String> {

    public List<SysUserRoleEntity> findByUserId(String userId);

}
