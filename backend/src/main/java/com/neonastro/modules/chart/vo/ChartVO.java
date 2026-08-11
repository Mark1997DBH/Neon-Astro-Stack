package com.neonastro.modules.chart.vo;

import com.neonastro.modules.chart.dto.PlanetPosition;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 星盘视图对象（与前端 Chart 类型对齐）
 *
 * @author neonastro
 */
@Data
public class ChartVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 星盘 ID */
    private Long id;

    /** 所属用户 ID */
    private Long userId;

    /** 星盘名称 */
    private String name;

    /** 出生日期 */
    private String birthDate;

    /** 出生时间 */
    private String birthTime;

    /** 出生地点 */
    private String birthPlace;

    /** 经度 */
    private Double longitude;

    /** 纬度 */
    private Double latitude;

    /** 行星位置列表 */
    private List<PlanetPosition> planets;

    /** 上升星座（Ascendant） */
    private String ascendant;

    /** 天顶星座（MC） */
    private String mc;

    /** 创建时间 */
    private String createdAt;
}
