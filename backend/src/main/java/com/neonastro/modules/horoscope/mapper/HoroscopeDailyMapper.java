package com.neonastro.modules.horoscope.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neonastro.modules.horoscope.entity.HoroscopeDaily;
import org.apache.ibatis.annotations.Mapper;

/**
 * 每日运势 Mapper
 *
 * @author neonastro
 */
@Mapper
public interface HoroscopeDailyMapper extends BaseMapper<HoroscopeDaily> {
}
