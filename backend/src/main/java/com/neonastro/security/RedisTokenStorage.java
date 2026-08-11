package com.neonastro.security;

import com.neonastro.common.constant.SecurityConstant;
import com.neonastro.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的 Token 存储实现（生产环境）
 * 由 {@link TokenStorageConfig} 条件装配，依赖 RedisUtil
 *
 * @author neonastro
 */
@Slf4j
public class RedisTokenStorage implements TokenStorage {

    private final RedisUtil redisUtil;

    public RedisTokenStorage(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void storeRefreshToken(Long userId, String token, long expire) {
        String key = SecurityConstant.REFRESH_TOKEN_PREFIX + userId;
        redisUtil.setWithExpire(key, token, expire, TimeUnit.SECONDS);
        log.debug("存储刷新令牌: userId={}, expire={}s", userId, expire);
    }

    @Override
    public String getRefreshToken(Long userId) {
        String key = SecurityConstant.REFRESH_TOKEN_PREFIX + userId;
        return redisUtil.get(key);
    }

    @Override
    public void removeRefreshToken(Long userId) {
        String key = SecurityConstant.REFRESH_TOKEN_PREFIX + userId;
        redisUtil.delete(key);
        log.debug("删除刷新令牌: userId={}", userId);
    }

    @Override
    public void blacklist(String token, long expire) {
        String key = SecurityConstant.TOKEN_BLACKLIST_PREFIX + token;
        redisUtil.setWithExpire(key, "1", expire, TimeUnit.SECONDS);
        log.debug("加入令牌黑名单: expire={}s", expire);
    }

    @Override
    public boolean isBlacklisted(String token) {
        String key = SecurityConstant.TOKEN_BLACKLIST_PREFIX + token;
        return redisUtil.hasKey(key);
    }
}
