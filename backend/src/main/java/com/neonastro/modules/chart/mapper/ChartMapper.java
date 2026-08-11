package com.neonastro.modules.chart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neonastro.modules.chart.entity.Chart;
import org.apache.ibatis.annotations.Mapper;

/**
 * 星盘 Mapper
 *
 * @author neonastro
 */
@Mapper
public interface ChartMapper extends BaseMapper<Chart> {
}
