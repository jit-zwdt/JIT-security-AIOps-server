package com.jit.server.security;

import com.jit.server.pojo.SysRoleEntity;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<SysUserRoleEntity> roleList = userRoleRepo.findByUserIdAndIsDeleted(user.getId(), 0);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (SysUserRoleEntity role : roleList) {
            authorities.add(new SimpleGrantedAuthority(roleRepo.findRoleSignById(role.getId())));
        }
        User securityUser = new User(user.getUsername(), user.getPassword(), authorities);
        return securityUser;
    }
}
