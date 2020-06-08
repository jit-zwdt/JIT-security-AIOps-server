package com.jit.server.repository;

import com.jit.server.pojo.ServerResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerResourceRepo extends JpaRepository<ServerResource, Long> {

    ServerResource findByServerId(Long id);
}
