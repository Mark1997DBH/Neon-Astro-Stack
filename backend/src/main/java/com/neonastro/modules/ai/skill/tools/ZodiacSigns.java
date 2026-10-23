package com.neonastro.modules.ai.skill.tools;

import com.neonastro.modules.user.service.UserService;
import com.neonastro.modules.user.vo.UserProfileVO;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 星座类技能共用的参数定义与解析工具
 *
 * @author neonastro
 */
final class ZodiacSigns {

    /** 十二星座英文名（后端统一使用英文枚举，与 HoroscopeService 保持一致） */
    private static final List<String> SIGNS = List.of(
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
            "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces");

    private ZodiacSigns() {
    }

    /**
     * 构造仅含 sign 参数的 JSON Schema
     *
     * @param description 参数说明
     */
    static Map<String, Object> signSchema(String description) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("sign", Map.of(
                "type", "string",
                "enum", SIGNS,
                "description", description));
        return Map.of("type", "object", "properties", properties, "required", List.of());
    }

    /**
     * 解析目标星座：优先使用模型传入的 sign，其次回退到当前用户资料中的星座
     *
     * @param args      模型参数
     * @param userId    当前登录用户 ID
     * @param userService 用户服务
     * @return 星座英文名
     */
    static String resolve(Map<String, Object> args, Long userId, UserService userService) {
        Object sign = args.get("sign");
        if (sign != null && StringUtils.hasText(sign.toString())) {
            return sign.toString().strip();
        }
        if (userId != null) {
            UserProfileVO profile = userService.getProfile(userId);
            if (profile != null && StringUtils.hasText(profile.getZodiacSign())) {
                return profile.getZodiacSign();
            }
        }
        throw new IllegalArgumentException("未指定星座，且当前用户资料中也没有星座，请先向用户确认其星座");
    }
}
