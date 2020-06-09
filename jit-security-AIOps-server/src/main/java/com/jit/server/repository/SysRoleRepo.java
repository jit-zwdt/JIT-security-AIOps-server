package com.jit.server.repository;

import com.jit.server.pojo.SysRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepo extends JpaRepository<SysRoleEntity,String> {
    @Query(value = "select roleSign from SysRoleEntity where id = ?1")
    public String findRoleSignById(String id);

}
