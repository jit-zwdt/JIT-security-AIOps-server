package com.jit.server.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TreeNode {

    private String id;

    private String label;

    private List<TreeNode> children;

    private String parentId;

    private Boolean parent;

}
