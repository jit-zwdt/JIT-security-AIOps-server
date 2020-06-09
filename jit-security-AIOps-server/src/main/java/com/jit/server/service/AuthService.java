package com.jit.server.service;


import com.jit.server.util.JwtTokenDto;

public interface AuthService {

    public JwtTokenDto login(String username, String password);

    public JwtTokenDto refreshToken(String refreshToken);
}
