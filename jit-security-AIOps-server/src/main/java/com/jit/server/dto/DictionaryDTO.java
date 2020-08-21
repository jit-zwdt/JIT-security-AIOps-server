package com.jit.server.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.server.pojo.SysDictionaryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictionaryDTO {
    private Integer num;
    private SysDictionaryEntity dictionaryEntity;
}
