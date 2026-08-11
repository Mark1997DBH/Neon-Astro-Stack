package com.neonastro.security;

/**
 * Token 存储接口：抽象刷新令牌缓存与黑名单管理
 * 默认实现：Redis（生产）/ 内存（开发无 Redis 环境）
 *
 * @author neonastro
 */
public interface TokenStorage {

    /**
     * 存储刷新令牌
     *
     * @param userId   用户 ID
     * @param token    刷新令牌
     * @param expire   过期时间（秒）
     */
    void storeRefreshToken(Long userId, String token, long expire);

    /**
     * 获取刷新令牌
     *
     * @param userId 用户 ID
     * @return 刷新令牌，不存在返回 null
     */
    String getRefreshToken(Long userId);

    /**
     * 删除刷新令牌
     *
     * @param userId 用户 ID
     */
    void removeRefreshToken(Long userId);

    /**
     * 将访问令牌加入黑名单
     *
     * @param token  访问令牌
     * @param expire 过期时间（秒）
     */
    void blacklist(String token, long expire);

    /**
     * 判断访问令牌是否已在黑名单
     *
     * @param token 访问令牌
     * @return true 表示已拉黑
     */
    boolean isBlacklisted(String token);
}
