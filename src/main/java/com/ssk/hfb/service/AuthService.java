package com.ssk.hfb.service;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.common.pojo.UserInfo;
import com.ssk.hfb.common.utils.JwtUtils;
import com.ssk.hfb.common.utils.RsaUtils;
import com.ssk.hfb.common.utils.SystemUtlis;
import com.ssk.hfb.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.tools.java.Constants;

import java.security.PrivateKey;
import java.security.PublicKey;

@Service
@Slf4j
public class AuthService {
    private static final String pubKeyPathLinux= "/public.pub";
    private static final String pubKeyPath = "/public.pub";
    public static final String PATH_CLASS_ROOT = Constants.class.getClassLoader().getResource("").getPath();
    public static final String ROOT_Path = PATH_CLASS_ROOT.substring(0,PATH_CLASS_ROOT.length() - "WEB-INF\\classes\\".length());
    private static final String priKeyPath = "/private.pri";
    private static final String priKeyPathLinux = "/private.pri";
    private PublicKey   publicKey ;
    private PrivateKey privateKey;
    {
        try {
            boolean systemLinux = SystemUtlis.isSystemLinux();
            System.out.println(ROOT_Path);
            if (systemLinux){
                publicKey = RsaUtils.getPublicKey(ROOT_Path+pubKeyPathLinux);
                privateKey = RsaUtils.getPrivateKey(ROOT_Path+priKeyPathLinux);
            }else {
                publicKey = RsaUtils.getPublicKey(ROOT_Path+pubKeyPath);
                privateKey = RsaUtils.getPrivateKey(ROOT_Path+priKeyPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String verifyToken(String token){
        boolean expiration = false;
        try{
             expiration = JwtUtils.isExpiration(token, publicKey);

        }catch (Exception e){
            log.error(e.getMessage());
            System.out.println("jin");
            throw new CommonAdviceException(ExceptioneEnum.TOKEN_IS_EXPIRED);
        }
        UserInfo info = JwtUtils.getInfoFromToken(token, publicKey);
        String newToken = JwtUtils.generateTokenExpireInMinutes(new UserInfo(info.getUserId(),info.getUserName()), privateKey);
        return newToken;
    }
    public String generateToken(User user){
        String newToken = JwtUtils.generateTokenExpireInMinutes(new UserInfo(user.getId(),user.getUserName()), privateKey);
        return newToken;
    }

    public int getUId(String token) {
        if ("".equals(token)|| token == null){
            return -1;
        }
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, publicKey);
            return info.getUserId();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return -1;
    }
}
