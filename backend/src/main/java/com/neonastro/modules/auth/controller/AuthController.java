package com.neonastro.modules.auth.controller;

import com.neonastro.common.api.Result;
import com.neonastro.common.constant.SecurityConstant;
import com.neonastro.modules.auth.dto.LoginReq;
import com.neonastro.modules.auth.dto.LoginResp;
import com.neonastro.modules.auth.dto.RefreshTokenReq;
import com.neonastro.modules.auth.dto.RegisterReq;
import com.neonastro.modules.auth.service.AuthService;
import com.neonastro.modules.auth.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 *
 * @author neonastro
 */
@Tag(name = "认证接口", description = "用户注册、登录、登出、令牌刷新")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterReq req) {
        return Result.success(authService.register(req));
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginResp> login(@Valid @RequestBody LoginReq req) {
        return Result.success(authService.login(req));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String header = request.getHeader(SecurityConstant.HEADER_AUTHORIZATION);
        String token = null;
        if (header != null && header.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            token = header.substring(SecurityConstant.TOKEN_PREFIX.length());
        }
        authService.logout(token);
        return Result.success();
    }

    @Operation(summary = "刷新令牌")
    @PostMapping("/refresh")
    public Result<LoginResp> refresh(@Valid @RequestBody RefreshTokenReq req) {
        return Result.success(authService.refresh(req));
    }
}
