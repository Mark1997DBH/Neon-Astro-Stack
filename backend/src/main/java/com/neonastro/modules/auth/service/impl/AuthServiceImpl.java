package com.neonastro.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neonastro.common.enums.UserStatusEnum;
import com.neonastro.common.api.ResultCode;
import com.neonastro.common.exception.BusinessException;
import com.neonastro.modules.auth.dto.LoginReq;
import com.neonastro.modules.auth.dto.LoginResp;
import com.neonastro.modules.auth.dto.RefreshTokenReq;
import com.neonastro.modules.auth.dto.RegisterReq;
import com.neonastro.modules.auth.entity.User;
import com.neonastro.modules.auth.mapper.UserMapper;
import com.neonastro.modules.auth.service.AuthService;
import com.neonastro.modules.auth.vo.UserVO;
import com.neonastro.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务实现
 *
 * @author neonastro
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(RegisterReq req) {
        // 校验用户名是否重复
        User exist = userMapper.selectByUsername(req.getUsername());
        if (exist != null) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }
        // 校验邮箱是否重复
        Long emailCount = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getEmail, req.getEmail()));
        if (emailCount != null && emailCount > 0) {
            throw new BusinessException(ResultCode.EMAIL_EXISTS);
        }
        // 构建用户实体
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setNickname(req.getNickname() != null ? req.getNickname() : req.getUsername());
        user.setStatus(UserStatusEnum.ACTIVE.getCode());
        userMapper.insert(user);
        // 绑定默认角色（sys_user_role 关联表）
        userMapper.insertUserRole(user.getId(), "ROLE_USER");
        log.info("用户注册成功：{}", user.getUsername());
        return toUserVO(user);
    }

    @Override
    public LoginResp login(LoginReq req) {
        User user = userMapper.selectByUsername(req.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        if (UserStatusEnum.DISABLED.getCode() == user.getStatus()) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        return jwtTokenProvider.createAccessTokenPair(user.getId(), user.getUsername());
    }

    @Override
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        jwtTokenProvider.blacklistAccessToken(token);
        Long userId = jwtTokenProvider.parseUserId(token);
        if (userId != null) {
            jwtTokenProvider.removeRefreshToken(userId);
        }
    }

    @Override
    public LoginResp refresh(RefreshTokenReq req) {
        Long userId = jwtTokenProvider.validateRefreshToken(req.getRefreshToken());
        if (userId == null) {
            throw new BusinessException(ResultCode.REFRESH_TOKEN_INVALID);
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return jwtTokenProvider.createAccessTokenPair(user.getId(), user.getUsername());
    }

    /**
     * 实体转视图对象
     */
    private UserVO toUserVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setEmail(user.getEmail());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }
}
