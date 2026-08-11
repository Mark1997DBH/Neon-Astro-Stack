package com.neonastro.modules.chart.calculator;

import com.neonastro.modules.chart.dto.PlanetPosition;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 星盘计算引擎（纯 Java，无框架依赖，便于单元测试）
 * 当前为演示级确定性算法：同一出生信息必然产生相同结果。
 * 生产环境可替换为真实天文历法计算（Swiss Ephemeris / 第三方 API）。
 *
 * @author neonastro
 */
public final class ChartCalculator {

    /** 黄道十二星座（英文，与前端 ECharts 雷达图 indicator 对齐） */
    public static final String[] SIGNS = {
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
            "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    };

    /** 常用行星 */
    public static final String[] PLANETS = {
            "Sun", "Moon", "Mercury", "Venus", "Mars",
            "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"
    };

    private ChartCalculator() {
    }

    /**
     * 计算结果
     */
    @Data
    @AllArgsConstructor
    public static class CalcResult {
        /** 行星位置列表 */
        private List<PlanetPosition> planets;
        /** 上升星座 */
        private String ascendant;
        /** 天顶星座 */
        private String mc;
    }

    /**
     * 计算星盘（演示级：基于出生信息的确定性伪随机）
     *
     * @param birthDate 出生日期
     * @param birthTime 出生时间
     * @return 星盘计算结果
     */
    public static CalcResult calculate(LocalDate birthDate, LocalTime birthTime) {
        // 以出生信息为种子，保证同一输入同一结果
        long seed = birthDate.toString().hashCode() * 31L
                + (birthTime == null ? 0L : birthTime.toString().hashCode());
        Random random = new Random(seed);

        // 上升点（ASC）度数 0-360，等宫制划分 12 宫
        double ascDegree = random.nextDouble() * 360.0;

        List<PlanetPosition> planets = new ArrayList<>(PLANETS.length);
        for (String planet : PLANETS) {
            double absoluteDegree = random.nextDouble() * 360.0;
            int signIndex = ((int) (absoluteDegree / 30)) % 12;
            double inSignDegree = Math.round((absoluteDegree % 30) * 10.0) / 10.0;
            // 宫位：以 ASC 为第 1 宫起点，等宫制
            int house = ((int) (((absoluteDegree - ascDegree + 360.0) % 360.0) / 30.0)) + 1;
            boolean retrograde = random.nextBoolean();
            planets.add(new PlanetPosition(planet, SIGNS[signIndex], inSignDegree, house, retrograde));
        }

        // 天顶（MC）取第 10 宫起始点
        double mcDegree = (ascDegree + 270.0) % 360.0;
        String ascendant = SIGNS[((int) (ascDegree / 30)) % 12];
        String mc = SIGNS[((int) (mcDegree / 30)) % 12];

        return new CalcResult(planets, ascendant, mc);
    }
}
