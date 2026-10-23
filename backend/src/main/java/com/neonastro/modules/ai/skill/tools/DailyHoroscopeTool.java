package com.neonastro.modules.ai.skill.tools;

import com.neonastro.modules.ai.skill.AiTool;
import com.neonastro.modules.horoscope.service.HoroscopeService;
import com.neonastro.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 技能：查询指定星座的今日运势
 *
 * @author neonastro
 */
@Component
@RequiredArgsConstructor
public class DailyHoroscopeTool implements AiTool {

    private final HoroscopeService horoscopeService;
    private final UserService userService;

    @Override
    public String name() {
        return "get_daily_horoscope";
    }

    @Override
    public String description() {
        return "查询指定星座的今日运势，返回综合、爱情、事业、财运、健康解读以及幸运数字、幸运颜色和星级评分。"
                + "用户未说明星座时可不传 sign，将自动使用其个人资料中的星座。";
    }

    @Override
    public Map<String, Object> parameters() {
        return ZodiacSigns.signSchema("星座英文名，如 Scorpio；不传则使用当前用户资料中的星座");
    }

    @Override
    public Object execute(Map<String, Object> args, Long userId) {
        String sign = ZodiacSigns.resolve(args, userId, userService);
        return horoscopeService.getDaily(sign);
    }
}
