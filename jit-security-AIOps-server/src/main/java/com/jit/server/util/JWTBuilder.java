package com.jit.server.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: JWTBuilder use to creat token and check token
 * @Author: zengxin_miao
 * @Date: 2020/06/08 10:18
 */
public class JWTBuilder {

    /**
     * token expiredTime, unit:(ms).
     */
    private static final int TOKEN_EXPIRED_TIME = 60 * 1000;

    /**
     * jwt Encryption and decryption key
     */
    private static final String JWT_SECRET = "3HK9afzQPzZBfoMLDCF99";

    /**
     * jwtId
     */
    public static final String jwtId = "tokenId";

    /**
     * createToken
     *
     * @param name
     * @param passwd
     * @return token
     */
    public static String createToken(String name, String passwd) {
        //签发时间
        Date istDate = new Date();

        //设置过期时间
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MILLISECOND, TOKEN_EXPIRED_TIME);
        Date expiresDate = nowTime.getTime();

        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        String token = JWT.create()
                .withHeader(map)
                .withClaim("name", name)
                .withClaim("passwd", passwd)
                .withExpiresAt(expiresDate)
                .withIssuedAt(istDate)
                .withJWTId(jwtId)
                .sign(Algorithm.HMAC256(JWT_SECRET));
        return token;
    }


    /**
     * verifyToken
     *
     * @param token
     * @return
     */
    public static Map<String, Claim> verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jwt.getClaims();
    }
}
