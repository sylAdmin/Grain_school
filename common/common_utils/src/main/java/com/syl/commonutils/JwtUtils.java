package com.syl.commonutils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Jwt工具类
 */
public class JwtUtils {

    public static final long EXPIRE = 1000 * 60 * 60 * 24;  //token过期时间
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO"; //秘钥

    /**
     * 生成JWT token字符串
     * @return
     */
    public static String getJwtToken(String id, String nickname) {
        String JwtToken = Jwts.builder()
                //设置jwt的头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                //设置token的过期时间
                .setSubject("guli_token") //分类
                .setIssuedAt(new Date()) //获取当前时间
                //将当前时间加上过期使时间，如果超过就过期了
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

                .claim("id", id)  //设置token主体部分，存储用户信息
                .claim("nickname", nickname)

                .signWith(SignatureAlgorithm.HS256, APP_SECRET)  //签名哈希
                .compact();

        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     *
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     *
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if (StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     *
     * @param request
     * @return
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if (StringUtils.isEmpty(jwtToken)) return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("id");
    }
}