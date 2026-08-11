package com.neonastro.modules.horoscope.calculator;

import com.neonastro.modules.horoscope.vo.DailyHoroscopeVO;
import com.neonastro.modules.horoscope.vo.WeeklyHoroscopeVO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 运势生成引擎（演示级确定性算法）
 * 同一星座 + 日期必然产生相同结果，生产环境可替换为第三方运势数据源或人工内容库。
 *
 * @author neonastro
 */
public final class HoroscopeCalculator {

    /** 星座列表（用于校验） */
    public static final String[] SIGNS = {
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
            "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    };

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** 幸运颜色池 */
    private static final String[] LUCKY_COLORS = {"紫色", "青色", "粉色", "金色", "蓝色", "绿色"};

    /** 五维运势文案池（控制在 32 字符内以匹配表字段） */
    private static final String[] OVERALL_POOL = {
            "今日能量充沛，宜主动出击", "星象平稳，适合按部就班", "守护星加持，运势稳步上升",
            "运势回暖，把握关键节点", "日月相位和谐，诸事顺遂"
    };
    private static final String[] LOVE_POOL = {
            "桃花旺盛，主动联系更佳", "感情稳定，适合坦诚沟通", "小惊喜不断，把握浪漫时机",
            "多倾听对方，关系更亲密", "单身宜社交，缘分在路上"
    };
    private static final String[] CAREER_POOL = {
            "职场表现亮眼，把握展示机会", "专注手头任务，业绩自然来", "团队协作顺利，方案易获认可",
            "灵感迸发，适合推进新项目", "贵人运旺，多请教多收获"
    };
    private static final String[] WEALTH_POOL = {
            "正财稳健，可考虑小额定投", "偏财小有进账，勿贪多", "理性消费，钱包更稳",
            "投资信号偏暖，谨慎出手", "收支平衡，注意合理分配"
    };
    private static final String[] HEALTH_POOL = {
            "状态在线，适合运动出汗", "注意作息，早睡更佳", "身心放松，散步冥想为宜",
            "精力旺盛，注意劳逸结合", "饮食清淡，状态更轻盈"
    };

    private HoroscopeCalculator() {
    }

    /**
     * 生成每日运势
     *
     * @param sign 星座（英文）
     * @param date 日期
     * @return 每日运势
     */
    public static DailyHoroscopeVO generateDaily(String sign, LocalDate date) {
        Random random = new Random(seed(sign, date.toString()));
        DailyHoroscopeVO vo = new DailyHoroscopeVO();
        vo.setSign(sign);
        vo.setDate(date.format(DATE_FORMAT));
        vo.setOverall(pick(random, OVERALL_POOL));
        vo.setLove(pick(random, LOVE_POOL));
        vo.setCareer(pick(random, CAREER_POOL));
        vo.setWealth(pick(random, WEALTH_POOL));
        vo.setHealth(pick(random, HEALTH_POOL));
        vo.setLuckyNumber(1 + random.nextInt(9));
        vo.setLuckyColor(pick(random, LUCKY_COLORS));
        // 评分由文案派生，保证生成与库内读取结果一致
        vo.setRating(ratingOf(vo.getOverall()));
        return vo;
    }

    /**
     * 生成本周运势
     *
     * @param sign 星座（英文）
     * @return 每周运势（本周一 ~ 本周日）
     */
    public static WeeklyHoroscopeVO generateWeekly(String sign) {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);
        Random random = new Random(seed(sign, monday.toString()));

        WeeklyHoroscopeVO vo = new WeeklyHoroscopeVO();
        vo.setSign(sign);
        vo.setWeekStart(monday.format(DATE_FORMAT));
        vo.setWeekEnd(sunday.format(DATE_FORMAT));
        vo.setOverall(pick(random, OVERALL_POOL) + "，本周整体向好");
        vo.setSummary("本周" + sign + "星象逐渐明朗，上半周适合规划与复盘，下半周把握人际与机会。"
                + "保持节奏，稳步推进，周末适合放松充电。");
        List<String> highlights = new ArrayList<>();
        highlights.add(pick(random, CAREER_POOL));
        highlights.add(pick(random, LOVE_POOL));
        highlights.add(pick(random, WEALTH_POOL));
        vo.setHighlights(highlights);
        vo.setRating(ratingOf(vo.getOverall()));
        return vo;
    }

    /**
     * 校验星座是否合法
     */
    public static boolean isValidSign(String sign) {
        for (String s : SIGNS) {
            if (s.equalsIgnoreCase(sign)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 确定性随机种子
     */
    private static long seed(String sign, String key) {
        return sign.hashCode() * 31L + key.hashCode();
    }

    /**
     * 从文案池中按随机数选取（同一随机序列结果稳定）
     */
    private static String pick(Random random, String[] pool) {
        return pool[random.nextInt(pool.length)];
    }

    /**
     * 根据文案派生评分（1-5 星），保证生成与库内读取一致
     */
    public static int ratingOf(String text) {
        return text == null ? 3 : 3 + Math.floorMod(text.hashCode(), 3);
    }
}
