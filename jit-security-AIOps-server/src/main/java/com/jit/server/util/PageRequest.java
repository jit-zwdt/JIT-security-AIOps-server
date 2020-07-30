package com.jit.server.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest<T> {

    private Integer page;

    private Integer size;

    private List<Map<String, String>> orders;

    private T param;

    public void setPage(Integer page) {
        this.page = page != null ? page : 1;
    }

    public void setSize(Integer size) {
        this.size = size != null ? size : 10;
    }
}
