package com.neonastro.security;

import com.neonastro.common.constant.SecurityConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的 Token 存储实现（开发环境无 Redis 时的降级方案）
 * 由 {@link TokenStorageConfig} 条件装配
 * 注意：应用重启后令牌信息将丢失，仅适用于开发调试
 *
 * @author neonastro
 */
@Slf4j
public class InMemoryTokenStorage implements TokenStorage {

    /** 刷新令牌存储：key -> token */
    private final Map<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    /** 刷新令牌过期时间（秒）：key -> expireAt */
    private final Map<String, Long> refreshTokenExpiry = new ConcurrentHashMap<>();

    /** 黑名单存储：token -> expireAt */
    private final Map<String, Long> blacklistStore = new ConcurrentHashMap<>();

    @Override
    public void storeRefreshToken(Long userId, String token, long expire) {
        String key = SecurityConstant.REFRESH_TOKEN_PREFIX + userId;
        refreshTokenStore.put(key, token);
        refreshTokenExpiry.put(key, System.currentTimeMillis() / 1000 + expire);
        log.debug("[内存] 存储刷新令牌: userId={}, expire={}s", userId, expire);
    }

    @Override
    public String getRefreshToken(Long userId) {
        String key = SecurityConstant.REFRESH_TOKEN_PREFIX + userId;
        Long expireAt = refreshTokenExpiry.get(key);
        if (expireAt != null && System.currentTimeMillis() / 1000 > expireAt) {
            refreshTokenStore.remove(key);
            refreshTokenExpiry.remove(key);
            return null;
        }
        return refreshTokenStore.get(key);
    }

    @Override
    public void removeRefreshToken(Long userId) {
        String key = SecurityConstant.REFRESH_TOKEN_PREFIX + userId;
        refreshTokenStore.remove(key);
        refreshTokenExpiry.remove(key);
        log.debug("[内存] 删除刷新令牌: userId={}", userId);
    }

    @Override
    public void blacklist(String token, long expire) {
        String key = SecurityConstant.TOKEN_BLACKLIST_PREFIX + token;
        blacklistStore.put(key, System.currentTimeMillis() / 1000 + expire);
        log.debug("[内存] 加入令牌黑名单: expire={}s", expire);
    }

    @Override
    public boolean isBlacklisted(String token) {
        String key = SecurityConstant.TOKEN_BLACKLIST_PREFIX + token;
        Long expireAt = blacklistStore.get(key);
        if (expireAt == null) {
            return false;
        }
        if (System.currentTimeMillis() / 1000 > expireAt) {
            blacklistStore.remove(key);
            return false;
        }
        return true;
    }
}
