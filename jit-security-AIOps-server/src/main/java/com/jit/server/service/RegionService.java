package com.jit.server.service;


import com.jit.server.dto.TreeNode;
import com.jit.server.pojo.Region;

import java.util.List;
import java.util.Optional;

public interface RegionService {

    List<TreeNode> findRegion();

    boolean regionExist(Region region);

    void addRegion(Region region);

    void addParentRegion(Region region);

    void deleteRegion(long id);

    Optional<Region> findByRegionId(long id);

    void updateRedion(Region region);
}
