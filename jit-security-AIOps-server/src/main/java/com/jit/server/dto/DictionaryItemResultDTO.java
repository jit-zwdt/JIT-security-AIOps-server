package com.jit.server.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.server.pojo.SysDictionaryItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictionaryItemResultDTO {
    private List<SysDictionaryItemEntity> list;
    private int count;
}
