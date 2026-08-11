package com.neonastro.modules.chart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 生成星盘请求 DTO
 *
 * @author neonastro
 */
@Data
public class GenerateChartReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 星盘名称 / 备注 */
    @NotBlank(message = "星盘名称不能为空")
    private String name;

    /** 出生日期（yyyy-MM-dd） */
    @NotBlank(message = "出生日期不能为空")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "出生日期格式必须为 yyyy-MM-dd")
    private String birthDate;

    /** 出生时间（HH:mm） */
    @NotBlank(message = "出生时间不能为空")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "出生时间格式必须为 HH:mm")
    private String birthTime;

    /** 出生地点 */
    @NotBlank(message = "出生地点不能为空")
    private String birthPlace;

    /** 经度（可选） */
    private Double longitude;

    /** 纬度（可选） */
    private Double latitude;
}
