package com.neonastro.modules.horoscope.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neonastro.common.api.ResultCode;
import com.neonastro.common.exception.BusinessException;
import com.neonastro.modules.horoscope.calculator.HoroscopeCalculator;
import com.neonastro.modules.horoscope.entity.HoroscopeDaily;
import com.neonastro.modules.horoscope.mapper.HoroscopeDailyMapper;
import com.neonastro.modules.horoscope.service.HoroscopeService;
import com.neonastro.modules.horoscope.vo.DailyHoroscopeVO;
import com.neonastro.modules.horoscope.vo.WeeklyHoroscopeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * 运势服务实现
 *
 * @author neonastro
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HoroscopeServiceImpl implements HoroscopeService {

    private final HoroscopeDailyMapper horoscopeDailyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DailyHoroscopeVO getDaily(String sign) {
        validateSign(sign);
        LocalDate today = LocalDate.now();

        // 优先返回当日已生成的数据
        HoroscopeDaily existing = horoscopeDailyMapper.selectOne(
                new LambdaQueryWrapper<HoroscopeDaily>()
                        .eq(HoroscopeDaily::getZodiacSign, sign)
                        .eq(HoroscopeDaily::getForecastDate, today));
        if (existing != null) {
            return toVO(existing);
        }

        // 无则生成并落库
        DailyHoroscopeVO vo = HoroscopeCalculator.generateDaily(sign, today);
        HoroscopeDaily entity = new HoroscopeDaily();
        entity.setZodiacSign(sign);
        entity.setForecastDate(today);
        entity.setOverall(vo.getOverall());
        entity.setLove(vo.getLove());
        entity.setCareer(vo.getCareer());
        entity.setWealth(vo.getWealth());
        entity.setHealth(vo.getHealth());
        entity.setSummary(vo.getOverall());
        entity.setLuckyColor(vo.getLuckyColor());
        entity.setLuckyNumber(String.valueOf(vo.getLuckyNumber()));
        horoscopeDailyMapper.insert(entity);
        log.info("每日运势已生成：sign={}, date={}", sign, today);
        return vo;
    }

    @Override
    public WeeklyHoroscopeVO getWeekly(String sign) {
        validateSign(sign);
        return HoroscopeCalculator.generateWeekly(sign);
    }

    /**
     * 校验星座参数
     */
    private void validateSign(String sign) {
        if (sign == null || sign.isBlank() || !HoroscopeCalculator.isValidSign(sign)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "无效的星座参数：" + sign);
        }
    }

    /**
     * 实体转视图对象
     */
    private DailyHoroscopeVO toVO(HoroscopeDaily entity) {
        DailyHoroscopeVO vo = new DailyHoroscopeVO();
        vo.setSign(entity.getZodiacSign());
        vo.setDate(entity.getForecastDate() != null ? entity.getForecastDate().toString() : null);
        vo.setOverall(entity.getOverall());
        vo.setLove(entity.getLove());
        vo.setCareer(entity.getCareer());
        vo.setWealth(entity.getWealth());
        vo.setHealth(entity.getHealth());
        vo.setLuckyColor(entity.getLuckyColor());
        try {
            vo.setLuckyNumber(entity.getLuckyNumber() != null ? Integer.parseInt(entity.getLuckyNumber()) : null);
        } catch (NumberFormatException ex) {
            vo.setLuckyNumber(null);
        }
        vo.setRating(HoroscopeCalculator.ratingOf(entity.getOverall()));
        return vo;
    }
}
