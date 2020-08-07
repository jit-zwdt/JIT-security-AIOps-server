package com.jit.server.service.impl;

import com.jit.server.request.UserParams;
import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixUserDTO;
import com.jit.zabbix.client.model.user.ZabbixMedias;
import com.jit.zabbix.client.model.user.ZabbixMediatypes;
import com.jit.zabbix.client.request.ZabbixGetUserParams;
import com.jit.zabbix.client.service.ZabbixUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    public static final String EXTEND = "extend";
    public static final String ALIAS = "alias";
    public static final String USERID = "userid";

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ZabbixUserService zabbixUserService;

    @Override
    public List<ZabbixUserDTO> getUserInfo(String alias) throws Exception {
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        ZabbixGetUserParams params_user = new ZabbixGetUserParams();
        params_user.setOutput(EXTEND);
        if (!StringUtils.isEmpty(alias)) {
            Map<String, Object> search = new HashMap<>();
            search.put("alias", alias);
            params_user.setSearch(search);
        }

        return zabbixUserService.get(params_user, authToken);
    }

    @Override
    public List<UserParams> getUserAndMediaInfo(String alias, String userid) throws Exception {
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }
        ZabbixGetUserParams params_user = new ZabbixGetUserParams();
        params_user.setOutput(EXTEND);
        if (!StringUtils.isEmpty(alias)) {
            Map<String, Object> search = new HashMap<>();
            search.put(ALIAS, alias);
            params_user.setSearch(search);
        }
        if (!StringUtils.isEmpty(userid)) {
            Map<String, Object> search = new HashMap<>();
            search.put(USERID, userid);
            params_user.setFilter(search);
            params_user.setSelectMedias(EXTEND);
            params_user.setSelectMediatypes(EXTEND);
            params_user.setSelectUsrgrps(EXTEND);
        }

        List<ZabbixUserDTO> dtoList = zabbixUserService.get(params_user, authToken);
        List<UserParams> userParamList = new ArrayList<>();
        UserParams userparam;
        if (!CollectionUtils.isEmpty(dtoList)) {
            for (ZabbixUserDTO dto : dtoList) {
                List<ZabbixMedias> medias = dto.getZabbixMedias();
                List<ZabbixMediatypes> mediatypes = dto.getZabbixMediatypes();
                for (ZabbixMedias media : medias) {
                    userparam = new UserParams();
                    userparam.setMediaid(media.getMediaid());
                    userparam.setActive(media.getActive());
                    userparam.setUserid(media.getUserid());
                    userparam.setMediatypeid(media.getMediatypeid());
                    userparam.setSendto(media.getSendto());
                    userparam.setSeverity(media.getSeverity());
                    userparam.setPeriod(media.getPeriod());
                    String name = "";
                    String type = "";
                    for (ZabbixMediatypes mediatype : mediatypes) {
                        if (mediatype.getMediatypeid().equals(media.getMediatypeid())) {
                            name = mediatype.getName();
                            type = mediatype.getType();
                            break;
                        }
                    }
                    userparam.setName(name);
                    userparam.setType(type);
                    userParamList.add(userparam);
                }
            }
        }
        return userParamList;
    }

    @Override
    public Object updateUserInfo(String userId, List<Map<String, Object>> params) throws Exception {
        String authToken = zabbixAuthService.getAuth();
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        // to do

        return null;
    }
}
