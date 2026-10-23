package com.neonastro.modules.ai.skill.tools;

import com.neonastro.modules.ai.skill.AiTool;
import com.neonastro.modules.user.service.UserService;
import com.neonastro.modules.user.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 技能：查询当前登录用户的个人资料（昵称 / 星座 / 生日 / 简介）
 *
 * @author neonastro
 */
@Component
@RequiredArgsConstructor
public class UserProfileTool implements AiTool {

    private final UserService userService;

    @Override
    public String name() {
        return "get_user_profile";
    }

    @Override
    public String description() {
        return "查询当前登录用户的占星相关资料：昵称、星座、出生日期、个人简介。"
                + "当需要确认用户本人星座或出生信息时调用，避免让用户重复提供";
    }

    @Override
    public Map<String, Object> parameters() {
        return Map.of("type", "object", "properties", Map.of(), "required", List.of());
    }

    @Override
    public Object execute(Map<String, Object> args, Long userId) {
        if (userId == null) {
            throw new IllegalStateException("当前请求未登录，无法获取用户资料");
        }
        UserProfileVO profile = userService.getProfile(userId);
        if (profile == null) {
            throw new IllegalStateException("用户不存在");
        }
        // 仅回传与占星相关的字段，避免泄露邮箱等隐私信息
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("nickname", profile.getNickname());
        data.put("zodiacSign", profile.getZodiacSign());
        data.put("birthday", profile.getBirthday());
        data.put("bio", profile.getBio());
        return data;
    }
}
