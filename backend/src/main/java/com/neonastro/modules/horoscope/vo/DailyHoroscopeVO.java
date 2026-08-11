package com.neonastro.modules.horoscope.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 每日运势视图对象（与前端 DailyHoroscope 类型对齐）
 *
 * @author neonastro
 */
@Data
public class DailyHoroscopeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 星座 */
    private String sign;

    /** 运势日期（yyyy-MM-dd） */
    private String date;

    /** 综合运势描述 */
    private String overall;

    /** 爱情运势描述 */
    private String love;

    /** 事业运势描述 */
    private String career;

    /** 财运描述 */
    private String wealth;

    /** 健康描述 */
    private String health;

    /** 幸运数字 */
    private Integer luckyNumber;

    /** 幸运颜色 */
    private String luckyColor;

    /** 综合评分（1-5 星） */
    private Integer rating;
}
