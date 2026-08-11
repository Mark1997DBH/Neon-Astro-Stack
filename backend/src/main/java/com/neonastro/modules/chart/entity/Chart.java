package com.neonastro.modules.chart.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.neonastro.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 星盘实体，对应 chart 表
 *
 * @author neonastro
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("chart")
public class Chart extends BaseEntity {

    /** 所属用户 ID */
    @TableField("user_id")
    private Long userId;

    /** 星盘名称 */
    private String name;

    /** 星盘类型：natal 本命盘 / transit 推运 / synastry 合盘 */
    @TableField("chart_type")
    private String chartType;

    /** 盘主出生日期 */
    @TableField("birth_date")
    private LocalDate birthDate;

    /** 盘主出生时间 */
    @TableField("birth_time")
    private LocalTime birthTime;

    /** 盘主出生地点 */
    @TableField("birth_place")
    private String birthPlace;

    /** 纬度 */
    private Double latitude;

    /** 经度 */
    private Double longitude;

    /** 星盘计算结果（JSON 字符串） */
    @TableField("chart_data")
    private String chartData;
}
