package com.neonastro.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * 由 {@code RedisConfig} 通过 @Bean 方式装配（不依赖组件扫描的条件注解）
 *
 * @author neonastro
 */
@Slf4j
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 写入缓存
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 读取缓存
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 写入缓存并设置过期时间
     */
    public void setWithExpire(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 删除缓存
     */
    public boolean delete(String key) {
        Boolean result = stringRedisTemplate.delete(key);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 是否存在指定 key
     */
    public boolean hasKey(String key) {
        Boolean result = stringRedisTemplate.hasKey(key);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 设置过期时间
     */
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        Boolean result = stringRedisTemplate.expire(key, timeout, timeUnit);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 自增
     */
    public long increment(String key) {
        return stringRedisTemplate.opsForValue().increment(key, 1);
    }
}
