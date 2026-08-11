package com.neonastro.modules.horoscope.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.neonastro.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 每日运势实体，对应 horoscope_daily 表
 *
 * @author neonastro
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("horoscope_daily")
public class HoroscopeDaily extends BaseEntity {

    /** 关联用户（可空，通用运势） */
    @TableField("user_id")
    private Long userId;

    /** 星座 */
    @TableField("zodiac_sign")
    private String zodiacSign;

    /** 运势日期 */
    @TableField("forecast_date")
    private LocalDate forecastDate;

    /** 综合运势指数 */
    private String overall;

    /** 爱情运势指数 */
    private String love;

    /** 事业运势指数 */
    private String career;

    /** 财运指数 */
    private String wealth;

    /** 健康指数 */
    private String health;

    /** 运势概述 */
    private String summary;

    /** 幸运颜色 */
    @TableField("lucky_color")
    private String luckyColor;

    /** 幸运数字（字符串存储） */
    @TableField("lucky_number")
    private String luckyNumber;
}
