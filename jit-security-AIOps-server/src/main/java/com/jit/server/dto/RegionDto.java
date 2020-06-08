package com.jit.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RegionDto {

    private long regionId;

    private String regionName;

    private long parentId;

    private Boolean isParent;

    public List<RegionDto> children;


}
