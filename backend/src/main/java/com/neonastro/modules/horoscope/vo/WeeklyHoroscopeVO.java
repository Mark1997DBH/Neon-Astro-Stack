package com.neonastro.modules.horoscope.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 每周运势视图对象（与前端 WeeklyHoroscope 类型对齐）
 *
 * @author neonastro
 */
@Data
public class WeeklyHoroscopeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 星座 */
    private String sign;

    /** 周起始日期（yyyy-MM-dd） */
    private String weekStart;

    /** 周结束日期（yyyy-MM-dd） */
    private String weekEnd;

    /** 综合运势描述 */
    private String overall;

    /** 本周概述 */
    private String summary;

    /** 本周亮点 */
    private List<String> highlights;

    /** 综合评分（1-5 星） */
    private Integer rating;
}
