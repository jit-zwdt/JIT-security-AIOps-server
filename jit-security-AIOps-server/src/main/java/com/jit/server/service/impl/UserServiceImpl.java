package com.jit.server.service.impl;

import com.jit.server.repository.SysUserRepo;
import com.jit.server.request.UserParams;
import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixUpdateMediaDTO;
import com.jit.zabbix.client.dto.ZabbixUserDTO;
import com.jit.zabbix.client.model.user.ZabbixMedias;
import com.jit.zabbix.client.model.user.ZabbixMediasUpdate;
import com.jit.zabbix.client.model.user.ZabbixMediatypes;
import com.jit.zabbix.client.request.ZabbixGetUserParams;
import com.jit.zabbix.client.service.ZabbixUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    public static final String EXTEND = "extend";
    public static final String ALIAS = "alias";
    public static final String USERID = "userid";

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ZabbixUserService zabbixUserService;

    @Autowired
    private SysUserRepo sysUserRepo;

    @Override
    public List<ZabbixUserDTO> getUserInfo(String alias, HttpServletRequest req) throws Exception {
        String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
        if (StringUtils.isEmpty(auth)) {
            return null;
        }

        ZabbixGetUserParams params_user = new ZabbixGetUserParams();
        params_user.setOutput(EXTEND);
        if (!StringUtils.isEmpty(alias)) {
            Map<String, Object> search = new HashMap<>();
            search.put("alias", alias);
            params_user.setSearch(search);
        }

        return zabbixUserService.get(params_user, auth);
    }

    @Override
    public List<UserParams> getUserAndMediaInfo(String alias, String userid,HttpServletRequest req) throws Exception {
        String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
        if (StringUtils.isEmpty(auth)) {
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

        List<ZabbixUserDTO> dtoList = zabbixUserService.get(params_user, auth);
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
    public String updateUserInfo(String userId, List<UserParams> params, HttpServletRequest req) throws Exception {
        String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
        if (StringUtils.isEmpty(auth)) {
            return null;
        }
        ZabbixUpdateMediaDTO zabbixUpdateMediaDTO = new ZabbixUpdateMediaDTO();
        if (!StringUtils.isEmpty(userId)) {
            zabbixUpdateMediaDTO.setUserid(userId);
        }
        if (params.size() > 0) {
            List<ZabbixMediasUpdate> zabbixMediasList = new ArrayList<>();
            for (UserParams userParams : params) {
                ZabbixMediasUpdate zabbixMediasUpdate = new ZabbixMediasUpdate();
                zabbixMediasUpdate.setActive(userParams.getActive());
                zabbixMediasUpdate.setMediatypeid(userParams.getMediatypeid());
                zabbixMediasUpdate.setPeriod(userParams.getPeriod());
                zabbixMediasUpdate.setSeverity(userParams.getSeverity());
                zabbixMediasUpdate.setSendto(userParams.getSendto());
                zabbixMediasList.add(zabbixMediasUpdate);
            }
            zabbixUpdateMediaDTO.setList(zabbixMediasList);
        }
        return zabbixUserService.update(zabbixUpdateMediaDTO, auth);
    }

    @Override
    public String findIdByUsername() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (StringUtils.isNotEmpty(username)) {
            return sysUserRepo.findIdByUsername(username);
        } else {
            return null;
        }
    }
}
