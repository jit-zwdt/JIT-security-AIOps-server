package com.jit.server.service.impl;


import com.jit.server.repository.HostRepo;
import com.jit.server.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    private HostRepo hostRepo;

    @Override
    public List<Object> getHostByType() throws Exception {
        return hostRepo.getHostByType();
    }
}
