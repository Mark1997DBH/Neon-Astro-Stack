package com.neonastro.modules.chart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 行星位置信息
 *
 * @author neonastro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanetPosition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 行星名称 */
    private String planet;

    /** 所在星座（英文，如 Aries） */
    private String sign;

    /** 星座内度数（0-30） */
    private double degree;

    /** 所在宫位（1-12） */
    private int house;

    /** 是否逆行 */
    private boolean retrograde;
}
