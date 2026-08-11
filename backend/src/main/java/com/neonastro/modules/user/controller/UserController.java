package com.neonastro.modules.user.controller;

import com.neonastro.common.api.Result;
import com.neonastro.modules.user.dto.UpdateProfileReq;
import com.neonastro.modules.user.service.UserService;
import com.neonastro.modules.user.vo.UserProfileVO;
import com.neonastro.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户中心接口
 *
 * @author neonastro
 */
@Tag(name = "用户中心")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户资料
     */
    @Operation(summary = "获取当前用户资料")
    @GetMapping("/profile")
    public Result<UserProfileVO> getProfile() {
        return Result.success(userService.getProfile(getCurrentUserId()));
    }

    /**
     * 更新当前用户资料
     */
    @Operation(summary = "更新当前用户资料")
    @PutMapping("/profile")
    public Result<UserProfileVO> updateProfile(@Valid @RequestBody UpdateProfileReq req) {
        return Result.success(userService.updateProfile(getCurrentUserId(), req));
    }

    /**
     * 从安全上下文获取当前登录用户 ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            return loginUser.getUserId();
        }
        return null;
    }
}
