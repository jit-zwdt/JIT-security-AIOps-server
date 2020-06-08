package com.jit.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class MyUserDetailService extends InMemoryUserDetailsManager {

    @PostConstruct
    public void initUsers(){
        Set<GrantedAuthority> authorities = new HashSet<>();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        createUser(new User("frank", passwordEncoder.encode("frank"), authorities));
    }
}
