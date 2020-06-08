package com.jit.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.util.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyAccessDeniedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(403);
        PrintWriter out = httpServletResponse.getWriter();
        Result result = Result.ERROR(ExceptionEnum.NO_AUTH);
        out.write(new ObjectMapper().writeValueAsString(result));
    }
}
