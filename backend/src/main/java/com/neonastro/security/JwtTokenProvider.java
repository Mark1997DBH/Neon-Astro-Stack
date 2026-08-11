package com.neonastro.security;

import com.neonastro.common.util.JwtUtil;
import com.neonastro.modules.auth.dto.LoginResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * JWT 令牌提供者，封装访问令牌/刷新令牌的生成与缓存管理
 * 通过 TokenStorage 抽象层实现 Redis / 内存存储的切换
 *
 * @author neonastro
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;
    private final TokenStorage tokenStorage;

    /**
     * 创建访问令牌对（access + refresh）并缓存刷新令牌
     *
     * @param userId   用户 ID
     * @param username 用户名
     * @return 登录响应
     */
    public LoginResp createAccessTokenPair(Long userId, String username) {
        String accessToken = jwtUtil.generateAccessToken(userId, username);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        // 缓存刷新令牌
        tokenStorage.storeRefreshToken(userId, refreshToken, jwtUtil.getRefreshExpire());

        LoginResp resp = new LoginResp();
        resp.setAccessToken(accessToken);
        resp.setRefreshToken(refreshToken);
        resp.setTokenType("Bearer");
        resp.setExpiresIn(jwtUtil.getAccessExpire());
        resp.setUserId(userId);
        resp.setUsername(username);
        return resp;
    }

    /**
     * 校验刷新令牌是否有效
     *
     * @param refreshToken 刷新令牌
     * @return 有效返回用户 ID，否则返回 null
     */
    public Long validateRefreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            return null;
        }
        Long userId = jwtUtil.parseUserId(refreshToken);
        String cached = tokenStorage.getRefreshToken(userId);
        if (cached == null || !cached.equals(refreshToken)) {
            return null;
        }
        return userId;
    }

    /**
     * 从访问令牌中解析用户 ID（用于登出场景，令牌可能已失效）
     *
     * @param accessToken 访问令牌
     * @return 用户 ID，解析失败返回 null
     */
    public Long parseUserId(String accessToken) {
        try {
            return jwtUtil.parseUserId(accessToken);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 将访问令牌加入黑名单
     *
     * @param accessToken 访问令牌
     */
    public void blacklistAccessToken(String accessToken) {
        if (!jwtUtil.validateToken(accessToken)) {
            return;
        }
        tokenStorage.blacklist(accessToken, jwtUtil.getAccessExpire());
    }

    /**
     * 判断访问令牌是否已加入黑名单
     *
     * @param accessToken 访问令牌
     * @return 已加入黑名单返回 true
     */
    public boolean isBlacklisted(String accessToken) {
        return tokenStorage.isBlacklisted(accessToken);
    }

    /**
     * 移除用户的刷新令牌（登出时调用）
     *
     * @param userId 用户 ID
     */
    public void removeRefreshToken(Long userId) {
        tokenStorage.removeRefreshToken(userId);
    }
}
