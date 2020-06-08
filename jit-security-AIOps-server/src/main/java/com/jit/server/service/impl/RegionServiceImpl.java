package com.jit.server.service.impl;


import com.jit.server.dto.TreeNode;
import com.jit.server.pojo.Region;
import com.jit.server.repository.RegionRepo;
import com.jit.server.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    RegionRepo regionRepo;

    @Override
    public List<TreeNode> findRegion() {
        List<Region> list = regionRepo.findAll();
        return buildTree(list,0L);
    }

    @Override
    public boolean regionExist(Region region) {
        return regionRepo.countByRegionName(region.getRegionName()) > 0 ?  true :  false;
    }

    @Override
    public void addRegion(Region region) {
        regionRepo.save(region);
    }

    @Override
    public void addParentRegion(Region region) {
        region.setParentId(0);
        region.setParent(true);
        regionRepo.save(region);
    }

    @Override
    public void deleteRegion(long id) {
        regionRepo.deleteById(id);
    }

    @Override
    public Optional<Region> findByRegionId(long id) {
        return regionRepo.findByRegionId(id);
    }

    @Override
    public void updateRedion(Region region) {
        regionRepo.save(region);
    }

    private List<TreeNode> buildTree(List<Region> regionList,Long parentId){
        List<TreeNode> resultList = new ArrayList<TreeNode>();
        for(Region region : regionList){
            if(region.getParentId() == parentId){
                TreeNode dto = new TreeNode();
                dto.setLabel(region.getRegionName());
                dto.setId(region.getRegionId());
                dto.setParentId(region.getParentId());
                dto.setParent(region.getParent());
                dto.setChildren(buildTree(regionList,dto.getId()));
                resultList.add(dto);
            }
        }
        return resultList;
    }
}
