package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: object user param
 * @Author: jian_liu
 * @Date: 2020/08/05 13:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserParams {

    private String mediaid;
    private String userid;
    private String mediatypeid;
    private Object sendto;
    private String active;
    private String severity;
    private String period;
    private String name;
    private String type;
}
