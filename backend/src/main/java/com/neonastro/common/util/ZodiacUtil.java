package com.neonastro.common.util;

import java.time.LocalDate;

/**
 * 星座计算工具（与前端 getZodiacSign 逻辑保持一致）
 *
 * @author neonastro
 */
public final class ZodiacUtil {

    private ZodiacUtil() {
    }

    /**
     * 根据出生日期返回星座（英文，如 Aries）
     *
     * @param birthDate 出生日期
     * @return 星座英文名，无法识别返回 Unknown
     */
    public static String zodiacOf(LocalDate birthDate) {
        if (birthDate == null) {
            return "Unknown";
        }
        int month = birthDate.getMonthValue();
        int day = birthDate.getDayOfMonth();

        // [星座, 起始月, 起始日, 结束月, 结束日]
        int[][] ranges = {
                {12, 22, 1, 19, 0},  // Capricorn
                {1, 20, 2, 18, 1},   // Aquarius
                {2, 19, 3, 20, 2},   // Pisces
                {3, 21, 4, 19, 3},   // Aries
                {4, 20, 5, 20, 4},   // Taurus
                {5, 21, 6, 21, 5},   // Gemini
                {6, 22, 7, 22, 6},   // Cancer
                {7, 23, 8, 22, 7},   // Leo
                {8, 23, 9, 22, 8},   // Virgo
                {9, 23, 10, 23, 9},  // Libra
                {10, 24, 11, 22, 10}, // Scorpio
                {11, 23, 12, 21, 11}  // Sagittarius
        };
        String[] signs = {
                "Capricorn", "Aquarius", "Pisces", "Aries", "Taurus", "Gemini",
                "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius"
        };

        for (int i = 0; i < ranges.length; i++) {
            int fromMonth = ranges[i][0];
            int fromDay = ranges[i][1];
            int toMonth = ranges[i][2];
            int toDay = ranges[i][3];
            if (fromMonth == toMonth) {
                if (month == fromMonth && day >= fromDay && day <= toDay) {
                    return signs[ranges[i][4]];
                }
            } else if (month == fromMonth && day >= fromDay) {
                return signs[ranges[i][4]];
            } else if (month == toMonth && day <= toDay) {
                return signs[ranges[i][4]];
            }
        }
        return "Unknown";
    }
}
