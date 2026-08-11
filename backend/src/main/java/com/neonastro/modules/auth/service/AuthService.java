package com.neonastro.modules.auth.service;

import com.neonastro.modules.auth.dto.LoginReq;
import com.neonastro.modules.auth.dto.LoginResp;
import com.neonastro.modules.auth.dto.RefreshTokenReq;
import com.neonastro.modules.auth.dto.RegisterReq;
import com.neonastro.modules.auth.vo.UserVO;

/**
 * 认证服务接口
 *
 * @author neonastro
 */
public interface AuthService {

    /**
     * 用户注册
     *
     * @param req 注册请求
     * @return 用户信息
     */
    UserVO register(RegisterReq req);

    /**
     * 用户登录
     *
     * @param req 登录请求
     * @return 登录响应（含双 token）
     */
    LoginResp login(LoginReq req);

    /**
     * 退出登录
     *
     * @param token 访问令牌
     */
    void logout(String token);

    /**
     * 刷新令牌
     *
     * @param req 刷新令牌请求
     * @return 新的登录响应
     */
    LoginResp refresh(RefreshTokenReq req);
}
