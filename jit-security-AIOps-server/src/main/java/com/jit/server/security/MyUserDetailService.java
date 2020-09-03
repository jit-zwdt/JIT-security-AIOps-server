package com.jit.server.security;

import com.jit.server.exception.AccountDisabledException;
import com.jit.server.pojo.SysUserEntity;
import com.jit.server.pojo.SysUserRoleEntity;
import com.jit.server.repository.SysRoleRepo;
import com.jit.server.repository.SysUserRepo;
import com.jit.server.repository.SysUserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private SysUserRepo userRepo;
    @Autowired
    private SysUserRoleRepo userRoleRepo;
    @Autowired
    private SysRoleRepo roleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user is not found");
        }
        if (0 == user.getStatus()) {
            throw new AccountDisabledException("account is disabled");
        }
        List<SysUserRoleEntity> roleList = userRoleRepo.findByUserIdAndIsDeleted(user.getId(), 0);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (SysUserRoleEntity role : roleList) {
            authorities.add(new SimpleGrantedAuthority(roleRepo.findRoleSignById(role.getRoleId())));
        }
        User securityUser = new User(user.getUsername(), user.getPassword(), authorities);
        return securityUser;
    }
}
