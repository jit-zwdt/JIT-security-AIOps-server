package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.config.FtpConfig;
import com.jit.server.config.SFtpConfig;
import com.jit.server.dto.SysUserDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysUserEntity;
import com.jit.server.request.SysUserEntityParams;
import com.jit.server.service.SysUserService;
import com.jit.server.service.UserService;
import com.jit.server.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private FtpConfig ftpConfig;

    @Autowired
    private SFtpConfig sFtpConfig;

    @ResponseBody
    @PostMapping(value = "/getUsers")
    @AutoLog(value = "人员管理-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getUsers(@RequestBody PageRequest<SysUserEntityParams> params) {

        try {
            Page<SysUserDTO> sysUserEntities = sysUserService.getUsers(params);
            Map<String, Object> result = new HashMap<>(5);
            result.put("page", params.getPage());
            result.put("size", params.getSize());
            result.put("totalRow", sysUserEntities.getTotalElements());
            result.put("totalPage", sysUserEntities.getTotalPages());
            result.put("dataList", sysUserEntities.getContent());
            return Result.SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/addUser")
    @AutoLog(value = "人员管理-新增", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addUser(@RequestBody SysUserEntity params) {
        try {
            sysUserService.addUser(params);
            return Result.SUCCESS(null);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateUser")
    @AutoLog(value = "人员管理-修改/冻结", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateUser(@RequestBody SysUserDTO params) {
        try {
            sysUserService.updateUser(params);
            return Result.SUCCESS(null);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updatePassword")
    @AutoLog(value = "人员管理-更改密码", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updatePassword(@RequestBody SysUserEntity params) {
        try {
            sysUserService.updatePassword(params);
            return Result.SUCCESS(null);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    @AutoLog(value = "人员管理-删除", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteUser(@PathVariable String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                sysUserService.deleteUser(id);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getUserById/{id}")
    @AutoLog(value = "人员管理-详情", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getUserById(@PathVariable String id) {
        try {
            SysUserDTO sysUserDTO = sysUserService.findUserById(id);
            if (sysUserDTO != null) {
                return Result.SUCCESS(sysUserDTO);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @GetMapping("/getUserInfo")
    public Result getUserInfo() {
        try {
            String id = userService.findIdByUsername();
            SysUserDTO user = sysUserService.findUserById(id);
            if (user != null) {
                return Result.SUCCESS(user);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/checkUserName/{username}")
    public Result checkUserName(@PathVariable String username) {
        try {
            if (StringUtils.isNotBlank(username)) {
                SysUserEntity sysUserEntity = sysUserService.getByUserName(username);
                if (sysUserEntity == null) {
                    return Result.SUCCESS(false);
                }
                return Result.SUCCESS(true);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/uploadPic")
    public Result uploadPic(MultipartFile file) throws Exception {
        FTPClient ftp = null;
        InputStream input = null;
        try {
            if (!file.isEmpty()) {
                String path = "/";
                String filename = file.getOriginalFilename(); //获得原始的文件名
                input = file.getInputStream();
                FtpClientUtil a = new FtpClientUtil();
                ftp = a.getConnectionFTP(ftpConfig.getHostName(), ftpConfig.getPort(), ftpConfig.getUserName(), ftpConfig.getPassWord());
                String url = a.uploadFile(ftp, path, filename, input);
                url = url.replace("/", "");
                return Result.SUCCESS(url);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            }
        }
    }

    @PostMapping("/getPicBase64/{param}")
    public Result getPicBase64(@PathVariable String param) {
        FTPClient ftpClient = null;
        InputStream inputStream = null;
        String re = null;
        FtpClientUtil a = new FtpClientUtil();
        try {
            ftpClient = a.getConnectionFTP(ftpConfig.getHostName(), ftpConfig.getPort(), ftpConfig.getUserName(), ftpConfig.getPassWord());
            if (ftpClient == null) {
                return null;
            }
            boolean status = param.contains("%2F");
            if (status) {
                param = param.replace("%2F", "/");
                if (param.substring(1, param.length()).contains("/")) {
                    ftpClient.changeWorkingDirectory(param.substring(0, param.lastIndexOf("/") + 1));
                } else {
                    ftpClient.changeWorkingDirectory("/");
                }
            } else {
                ftpClient.changeWorkingDirectory("/");
            }
            inputStream = ftpClient.retrieveFileStream(param);
            byte[] data = a.readInputStream(inputStream);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            re = base64Encoder.encode(data);// 将字节数组转成base64字符串
            ftpClient.logout();
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        } finally {
            if (ftpClient != null && ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            }
        }
        re.replaceAll("[\\s*\t\n\r]", "");
        return Result.SUCCESS(re);
    }

    @PostMapping("/uploadSftpPic")
    public Result uploadSftpPic(MultipartFile file) throws Exception {
        SFTPClientUtil sftp = new SFTPClientUtil(3, 6000);
        InputStream input = null;
        try {
            if (!file.isEmpty()) {
                String filename = file.getOriginalFilename(); //获得原始的文件名
                input = file.getInputStream();
                SftpConfig sftpConfig = new SftpConfig(sFtpConfig.getHostName(), sFtpConfig.getPort(), sFtpConfig.getUserName(), sFtpConfig.getPassWord(), sFtpConfig.getTimeOut(), sFtpConfig.getRemoteRootPath());
                String url = sftp.upload(sftpConfig.getRemoteRootPath(), filename, input, sftpConfig);
                return Result.SUCCESS(url);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            }
        }
    }

    @PostMapping("/getSftpPicBase64/{param}")
    public Result getSftpPicBase64(@PathVariable String param) {
        SFTPClientUtil sftp = new SFTPClientUtil(3, 6000);
        String re = "";
        try {
            SftpConfig sftpConfig = new SftpConfig(sFtpConfig.getHostName(), sFtpConfig.getPort(), sFtpConfig.getUserName(), sFtpConfig.getPassWord(), sFtpConfig.getTimeOut(), sFtpConfig.getRemoteRootPath());
            BASE64Encoder base64Encoder = new BASE64Encoder();
            re = base64Encoder.encode(sftp.download(sftpConfig.getRemoteRootPath(), param, sftpConfig));// 将字节数组转成base64字符串
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        } finally {
        }
        return Result.SUCCESS(re);
    }

}
