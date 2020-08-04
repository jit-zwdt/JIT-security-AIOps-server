package com.jit.server.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemParams {

    private Integer severity;

    private String hostId;

    private String timeFrom;

    private String timeTill;


}

