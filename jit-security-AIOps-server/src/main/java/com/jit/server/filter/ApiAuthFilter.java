package com.jit.server.filter;

import com.auth0.jwt.interfaces.Claim;
import com.jit.server.util.JWTBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Description: ApiAuthFilter use to check token
 * @Author: zengxin_miao
 * @Date: 2020/06/08 10:16
 */
public class ApiAuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //get request token
        String token = request.getHeader("authorization");
        if (StringUtils.isNotBlank(token)) {
            //verifyToken
            Map<String, Claim> claims = JWTBuilder.verifyToken(token);
            if (claims == null) {
                response.getWriter().write("token is invalid");
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            response.getWriter().write("token is empty");
        }
    }

    @Override
    public void destroy() {

    }
}
