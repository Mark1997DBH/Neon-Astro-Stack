package com.neonastro.modules.horoscope.service;

import com.neonastro.modules.horoscope.vo.DailyHoroscopeVO;
import com.neonastro.modules.horoscope.vo.WeeklyHoroscopeVO;

/**
 * 运势服务接口
 *
 * @author neonastro
 */
public interface HoroscopeService {

    /**
     * 获取指定星座今日运势（当日数据缺失时自动生成并落库）
     *
     * @param sign 星座（英文，如 Scorpio）
     * @return 每日运势
     */
    DailyHoroscopeVO getDaily(String sign);

    /**
     * 获取指定星座本周运势
     *
     * @param sign 星座（英文，如 Scorpio）
     * @return 每周运势
     */
    WeeklyHoroscopeVO getWeekly(String sign);
}
