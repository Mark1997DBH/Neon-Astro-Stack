package com.neonastro.common.constant;

/**
 * 安全相关常量
 *
 * @author neonastro
 */
public final class SecurityConstant {

    private SecurityConstant() {
    }

    /** 认证头名称 */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /** 令牌前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** 访问令牌黑名单前缀（Redis） */
    public static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    /** 刷新令牌前缀（Redis） */
    public static final String REFRESH_TOKEN_PREFIX = "token:refresh:";

    /** 默认时区 */
    public static final String DEFAULT_TIMEZONE = "Asia/Shanghai";
}
