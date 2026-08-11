package com.neonastro.modules.chart.service;

import com.neonastro.common.api.PageResult;
import com.neonastro.modules.chart.dto.GenerateChartReq;
import com.neonastro.modules.chart.vo.ChartVO;

/**
 * 星盘服务接口
 *
 * @author neonastro
 */
public interface ChartService {

    /**
     * 生成星盘并保存
     *
     * @param req    生成请求
     * @param userId 当前用户 ID
     * @return 星盘视图对象
     */
    ChartVO generate(GenerateChartReq req, Long userId);

    /**
     * 查询星盘详情（校验归属）
     *
     * @param id     星盘 ID
     * @param userId 当前用户 ID
     * @return 星盘视图对象
     */
    ChartVO getChart(Long id, Long userId);

    /**
     * 分页查询当前用户的星盘列表
     *
     * @param userId   当前用户 ID
     * @param page     页码（从 1 开始）
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<ChartVO> listCharts(Long userId, int page, int pageSize);
}
