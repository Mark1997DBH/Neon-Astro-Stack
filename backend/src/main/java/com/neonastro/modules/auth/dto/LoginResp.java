package com.neonastro.modules.auth.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录响应 DTO
 *
 * @author neonastro
 */
@Data
public class LoginResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 访问令牌 */
    private String accessToken;

    /** 刷新令牌 */
    private String refreshToken;

    /** 令牌类型 */
    private String tokenType = "Bearer";

    /** 访问令牌有效期（秒） */
    private Long expiresIn;

    /** 用户 ID */
    private Long userId;

    /** 用户名 */
    private String username;
}
