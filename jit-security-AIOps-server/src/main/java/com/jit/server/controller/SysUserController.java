package com.jit.server.controller;

import com.jit.server.config.FtpConfig;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysUserEntity;
import com.jit.server.request.SysUserEntityParams;
import com.jit.server.service.SysUserService;
import com.jit.server.service.UserService;
import com.jit.server.util.FtpClientUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private FtpConfig ftpConfig;

    @ResponseBody
    @PostMapping(value = "/getUsers")
    public Result getUsers(@RequestBody PageRequest<SysUserEntityParams> params) {

        try {
            Page<SysUserEntity> sysUserEntities = sysUserService.getUsers(params);
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
    public Result addUser(@RequestBody SysUserEntity params) {
        try {
            return Result.SUCCESS(sysUserService.addUser(params));
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody SysUserEntity params) {
        try {
            return Result.SUCCESS(sysUserService.updatePassword(params));
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public Result deleteUser(@PathVariable String id) {
        try {
            Optional<SysUserEntity> bean = sysUserService.findById(id);
            if (bean.isPresent()) {
                SysUserEntity sysUserEntity = bean.get();
                sysUserEntity.setGmtModified(new java.sql.Timestamp(new Date().getTime()));
                sysUserEntity.setIsDeleted(1);
                SysUserEntity sysUser = sysUserService.addUser(sysUserEntity);
                return Result.SUCCESS(sysUser);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findUserById/{id}")
    public Result findUserById(@PathVariable String id) {
        try {
            Optional<SysUserEntity> bean = sysUserService.findById(id);
            if (bean.isPresent()) {
                SysUserEntity sysDictionaryEntity = bean.get();
                return Result.SUCCESS(sysDictionaryEntity);
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
            Optional<SysUserEntity> bean = sysUserService.findById(id);
            if (bean.isPresent()) {
                SysUserEntity sysDictionaryEntity = bean.get();
                return Result.SUCCESS(sysDictionaryEntity);
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
        try {
            if (!file.isEmpty()) {
                String path = "/";
                String filename = file.getOriginalFilename(); //获得原始的文件名
                InputStream input = file.getInputStream();
                FtpClientUtil a = new FtpClientUtil();
                FTPClient ftp = a.getConnectionFTP(ftpConfig.getHostName(), ftpConfig.getPort(), ftpConfig.getUserName(), ftpConfig.getPassWord());
                String url = a.uploadFile(ftp, path, filename, input);
                a.closeFTP(ftp);
                return Result.SUCCESS(url);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @PostMapping("/getPicBase64")
    public Result getPicBase64(@RequestBody String param) {
        FTPClient ftpClient = null;
        InputStream inputStream = null;
        String re=null;
        FtpClientUtil a = new FtpClientUtil();
        try {
            System.out.println("进入1");
            ftpClient = a.getConnectionFTP(ftpConfig.getHostName(), ftpConfig.getPort(), ftpConfig.getUserName(), ftpConfig.getPassWord());
            if (ftpClient == null){
                System.out.println("进入2");
                return null;
            }
            System.out.println("进入3");
            ftpClient.changeWorkingDirectory("");
            inputStream = ftpClient.retrieveFileStream("2961f9de-edf7-4627-aefb-d62116ffb1bf.png");
            System.out.println("inputStream.available()："+inputStream.available());
            int firstByte = inputStream.read();
            int length = inputStream.available();
            byte[] bytes = new byte[length+1];
            bytes[0] = (byte)firstByte;
            inputStream.read(bytes,1,length);
            System.out.println("inputStream："+inputStream);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String base64 = base64Encoder.encode(bytes);//将字节数组转成base64字符串
            System.out.println("base64："+base64);
            re = base64;
            ftpClient.logout();
            System.out.println("进入4");
            System.out.println("info："+re);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        } finally {
            if (ftpClient.isConnected()){
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
                }
            }
        }
        return Result.SUCCESS(re);
    }
}
