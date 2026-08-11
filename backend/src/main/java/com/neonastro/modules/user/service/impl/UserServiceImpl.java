package com.neonastro.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neonastro.common.api.ResultCode;
import com.neonastro.common.exception.BusinessException;
import com.neonastro.common.util.ZodiacUtil;
import com.neonastro.modules.auth.entity.User;
import com.neonastro.modules.auth.mapper.UserMapper;
import com.neonastro.modules.user.dto.UpdateProfileReq;
import com.neonastro.modules.user.entity.UserProfile;
import com.neonastro.modules.user.mapper.UserProfileMapper;
import com.neonastro.modules.user.service.UserService;
import com.neonastro.modules.user.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 用户服务实现
 *
 * @author neonastro
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileVO getProfile(Long userId) {
        User user = requireUser(userId);
        UserProfile profile = findProfile(userId);
        return toVO(user, profile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileVO updateProfile(Long userId, UpdateProfileReq req) {
        User user = requireUser(userId);
        UserProfile profile = findProfile(userId);

        // 更新 sys_user：昵称 / 邮箱 / 头像（空值不更新）
        boolean userChanged = false;
        if (StringUtils.hasText(req.getNickname())) {
            user.setNickname(req.getNickname());
            userChanged = true;
        }
        if (StringUtils.hasText(req.getEmail()) && !req.getEmail().equals(user.getEmail())) {
            // 校验邮箱唯一性（排除当前用户）
            Long count = userMapper.selectCount(
                    new LambdaQueryWrapper<User>()
                            .eq(User::getEmail, req.getEmail())
                            .ne(User::getId, userId));
            if (count != null && count > 0) {
                throw new BusinessException(ResultCode.EMAIL_EXISTS);
            }
            user.setEmail(req.getEmail());
            userChanged = true;
        }
        if (StringUtils.hasText(req.getAvatar())) {
            user.setAvatar(req.getAvatar());
            userChanged = true;
        }
        if (userChanged) {
            userMapper.updateById(user);
        }

        // 更新 user_profile：生日 / 简介（首次则新建）
        boolean profileChanged = false;
        if (StringUtils.hasText(req.getBirthday())) {
            profile.setBirthDate(LocalDate.parse(req.getBirthday(), DATE_FORMAT));
            profileChanged = true;
        }
        if (req.getBio() != null) {
            profile.setBio(req.getBio());
            profileChanged = true;
        }
        if (profileChanged) {
            if (profile.getId() == null) {
                profile.setUserId(userId);
                userProfileMapper.insert(profile);
            } else {
                userProfileMapper.updateById(profile);
            }
        }

        log.info("用户资料更新：userId={}", userId);
        return toVO(user, profile);
    }

    /**
     * 查询用户，不存在抛异常
     */
    private User requireUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return user;
    }

    /**
     * 查询用户资料，不存在则返回空对象（首次更新时再创建）
     */
    private UserProfile findProfile(Long userId) {
        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId));
        return profile != null ? profile : new UserProfile();
    }

    /**
     * 实体转视图对象
     */
    private UserProfileVO toVO(User user, UserProfile profile) {
        UserProfileVO vo = new UserProfileVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setBio(profile.getBio());
        vo.setBirthday(profile.getBirthDate() != null ? profile.getBirthDate().format(DATE_FORMAT) : null);
        vo.setZodiacSign(ZodiacUtil.zodiacOf(profile.getBirthDate()));
        vo.setCreatedAt(user.getCreateTime() != null
                ? user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
        return vo;
    }
}
