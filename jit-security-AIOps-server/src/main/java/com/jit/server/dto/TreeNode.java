package com.jit.server.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TreeNode {

    private long id;

    private String label;

    private List<TreeNode> children;

    private long parentId;

    private Boolean parent;

}
