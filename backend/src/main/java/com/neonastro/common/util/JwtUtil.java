package com.neonastro.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类（基于 jjwt 0.12+ API）
 *
 * @author neonastro
 */
@Slf4j
@Component
public class JwtUtil {

    /** JWT 签名密钥 */
    @Value("${jwt.secret}")
    private String secret;

    /** 访问令牌有效期（秒） */
    @Value("${jwt.access-expire}")
    private long accessExpire;

    /** 刷新令牌有效期（秒） */
    @Value("${jwt.refresh-expire}")
    private long refreshExpire;

    /** 签名密钥对象 */
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成访问令牌
     *
     * @param userId   用户 ID
     * @param username 用户名
     * @return 访问令牌
     */
    public String generateAccessToken(Long userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessExpire * 1000);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("type", "access")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * 生成刷新令牌
     *
     * @param userId 用户 ID
     * @return 刷新令牌
     */
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshExpire * 1000);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * 解析令牌中的用户 ID
     *
     * @param token 令牌
     * @return 用户 ID
     */
    public Long parseUserId(String token) {
        String subject = parseClaims(token).getSubject();
        return Long.valueOf(subject);
    }

    /**
     * 解析令牌中的用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String parseUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    /**
     * 校验令牌是否有效
     *
     * @param token 令牌
     * @return 有效返回 true
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.debug("令牌已过期");
        } catch (Exception ex) {
            log.debug("令牌无效：{}", ex.getMessage());
        }
        return false;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 过期返回 true
     */
    public boolean isExpired(String token) {
        try {
            return parseClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    /**
     * 获取访问令牌有效期（秒）
     */
    public long getAccessExpire() {
        return accessExpire;
    }

    /**
     * 获取刷新令牌有效期（秒）
     */
    public long getRefreshExpire() {
        return refreshExpire;
    }

    /**
     * 解析令牌 Claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
