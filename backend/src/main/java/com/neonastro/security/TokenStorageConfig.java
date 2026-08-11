package com.neonastro.security;

import com.neonastro.common.util.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Token 存储配置
 * 根据 neon.redis.enabled 属性决定使用 Redis 存储（生产）还是内存存储（开发降级）
 * 注意：@ConditionalOnProperty 必须放在 @Bean 方法 / @Configuration 类上才能可靠生效
 *
 * @author neonastro
 */
@Configuration
public class TokenStorageConfig {

    /**
     * Redis 存储实现（neon.redis.enabled=true，默认）
     */
    @Bean
    @ConditionalOnProperty(prefix = "neon.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
    public TokenStorage redisTokenStorage(RedisUtil redisUtil) {
        return new RedisTokenStorage(redisUtil);
    }

    /**
     * 内存存储实现（neon.redis.enabled=false，无 Redis 环境降级）
     */
    @Bean
    @ConditionalOnProperty(prefix = "neon.redis", name = "enabled", havingValue = "false")
    public TokenStorage inMemoryTokenStorage() {
        return new InMemoryTokenStorage();
    }
}
