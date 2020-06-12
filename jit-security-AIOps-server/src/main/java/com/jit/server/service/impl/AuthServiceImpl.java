package com.jit.server.service.impl;

import com.jit.server.security.MyUserDetailService;
import com.jit.server.service.AuthService;
import com.jit.server.util.JwtTokenDto;
import com.jit.vue.utils.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public JwtTokenDto login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = this.myUserDetailService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
        JwtTokenDto tokenDto = new JwtTokenDto();
        tokenDto.setAccess_token(token);
        tokenDto.setRefresh_token(refreshToken);
        return tokenDto;
    }

    @Override
    public JwtTokenDto refreshToken(String refreshToken) throws JwtException {
        JwtTokenDto dto = new JwtTokenDto();
       if(!StringUtils.isEmpty(refreshToken)){
           String userName = jwtTokenUtil.getUsernameFromToken(refreshToken);
           UserDetails userDetails = myUserDetailService.loadUserByUsername(userName);
           boolean flag = jwtTokenUtil.validateToken(refreshToken,userDetails);
           if(flag){
               //校验成功
               String token = jwtTokenUtil.generateToken(userDetails);
               dto.setAccess_token(token);
               dto.setRefresh_token(refreshToken);
               return dto;
           }
       }
       return null;
    }
}
