package com.jit.server.controller;


import com.jit.server.dto.TreeNode;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.Region;
import com.jit.server.service.RegionService;
import com.jit.server.util.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Api(tags = "RegionController")
@RestController
@RequestMapping(value = "region")
public class RegionController {
    @Autowired
    RegionService regionService;

    @GetMapping
    public Result<List<TreeNode>> getRegionList() {
        List<TreeNode> list = regionService.findRegion();
        return Result.SUCCESS(list);
    }

    @PostMapping()
    public Result addRegion(@RequestBody Region region) {
        regionService.addRegion(region);
        return Result.SUCCESS(null);
    }

    @PostMapping("/regionParent")
    public Result addRegionParent(@RequestBody Region region) {
        if (regionService.regionExist(region)) {
            return Result.ERROR(ExceptionEnum.Duplicate_EXCEPTION);
        }
        regionService.addParentRegion(region);
        return Result.SUCCESS(null);
    }

    @PutMapping("/{id}")
    public Result<Region> updateRegion(@RequestBody Region region, @PathVariable Long id) {
        Optional<Region> bean = regionService.findByRegionId(id);
        if (bean.isPresent()) {
            regionService.updateRedion(region);
            return Result.SUCCESS(region);
        }
        return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
    }

    @DeleteMapping("/{id}")
    public Result deleteRegion(@PathVariable Long id) {
        Optional<Region> bean = regionService.findByRegionId(id);
        if (bean.isPresent()) {
            regionService.deleteRegion(id);
            return Result.SUCCESS(null);
        }
        return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
    }

}
