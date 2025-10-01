package org.instalkbackend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
@Component
public class JwtUtil {

    private static final String KEY = "this is a key";

    public static final long ACCESS_EXPIRE_TIME = 1000 * 60 * 60;//1h
    //public static final long ACCESS_EXPIRE_TIME = 1000 * 6;//6s
    public static final long REFRESH_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;//14d
    //public static final long REFRESH_EXPIRE_TIME = 1000 * 60;//1m
    //接收业务数据,生成token并返回
    public static String genAccessToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_TIME))
                .sign(Algorithm.HMAC256(KEY));
    }

    public static String genRefreshToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_TIME))
                .sign(Algorithm.HMAC256(KEY));
    }

    //接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        Map<String, Object> claims = JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();

        // 手动处理类型转换
        return convertNumberTypes(claims);
    }

    private static Map<String, Object> convertNumberTypes(Map<String, Object> claims) {
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Integer) {
                if ("id".equals(entry.getKey())) {
                    entry.setValue(((Integer) value).longValue());
                }
            }
        }
        return claims;
    }

}
