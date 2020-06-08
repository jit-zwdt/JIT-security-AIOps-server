package com.jit.server.repository;


import com.jit.server.pojo.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepo extends JpaRepository<Region, Long> {
    Optional<Region> findByRegionId(long id);

    int countByRegionName(String regionName);
}
