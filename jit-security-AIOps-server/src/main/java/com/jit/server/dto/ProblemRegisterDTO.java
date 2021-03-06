package com.jit.server.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemRegisterDTO {
    private String id;

    private String problemType;

    private String problemReason;

    private String problemSolution;

    private String problemProcess;

    private int isResolve;

    private LocalDateTime gmtCreate;

    private String problemHandleTime;

}
