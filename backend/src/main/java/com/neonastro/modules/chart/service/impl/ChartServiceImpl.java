package com.neonastro.modules.chart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neonastro.common.api.PageResult;
import com.neonastro.common.api.ResultCode;
import com.neonastro.common.exception.BusinessException;
import com.neonastro.modules.chart.calculator.ChartCalculator;
import com.neonastro.modules.chart.dto.GenerateChartReq;
import com.neonastro.modules.chart.dto.PlanetPosition;
import com.neonastro.modules.chart.entity.Chart;
import com.neonastro.modules.chart.mapper.ChartMapper;
import com.neonastro.modules.chart.service.ChartService;
import com.neonastro.modules.chart.vo.ChartVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 星盘服务实现
 *
 * @author neonastro
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final ChartMapper chartMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChartVO generate(GenerateChartReq req, Long userId) {
        // 计算星盘（演示级确定性算法）
        LocalDate birthDate = LocalDate.parse(req.getBirthDate(), DATE_FORMAT);
        LocalTime birthTime = LocalTime.parse(req.getBirthTime(), TIME_FORMAT);
        ChartCalculator.CalcResult result = ChartCalculator.calculate(birthDate, birthTime);

        // 构建并保存实体
        Chart chart = new Chart();
        chart.setUserId(userId);
        chart.setName(req.getName());
        chart.setChartType("natal");
        chart.setBirthDate(birthDate);
        chart.setBirthTime(birthTime);
        chart.setBirthPlace(req.getBirthPlace());
        chart.setLatitude(req.getLatitude());
        chart.setLongitude(req.getLongitude());
        chart.setChartData(toJson(result.getPlanets()));
        chartMapper.insert(chart);
        log.info("星盘生成成功：id={}, userId={}, name={}", chart.getId(), userId, chart.getName());

        return toVO(chart, result.getPlanets(), result.getAscendant(), result.getMc());
    }

    @Override
    public ChartVO getChart(Long id, Long userId) {
        Chart chart = chartMapper.selectById(id);
        if (chart == null || !chart.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        List<PlanetPosition> planets = parsePlanets(chart.getChartData());
        return toVO(chart, planets, null, null);
    }

    @Override
    public PageResult<ChartVO> listCharts(Long userId, int page, int pageSize) {
        Page<Chart> result = chartMapper.selectPage(
                new Page<>(page, pageSize),
                new LambdaQueryWrapper<Chart>()
                        .eq(Chart::getUserId, userId)
                        .orderByDesc(Chart::getCreateTime));
        List<ChartVO> records = result.getRecords().stream()
                .map(chart -> toVO(chart, parsePlanets(chart.getChartData()), null, null))
                .toList();
        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 实体转视图对象（可覆盖行星/上升/天顶）
     */
    private ChartVO toVO(Chart chart, List<PlanetPosition> planets, String ascendant, String mc) {
        ChartVO vo = new ChartVO();
        vo.setId(chart.getId());
        vo.setUserId(chart.getUserId());
        vo.setName(chart.getName());
        vo.setBirthDate(chart.getBirthDate() != null ? chart.getBirthDate().format(DATE_FORMAT) : null);
        vo.setBirthTime(chart.getBirthTime() != null ? chart.getBirthTime().format(TIME_FORMAT) : null);
        vo.setBirthPlace(chart.getBirthPlace());
        vo.setLongitude(chart.getLongitude());
        vo.setLatitude(chart.getLatitude());
        vo.setPlanets(planets);
        // 查询场景下从存储数据读取，缺失时前端降级展示
        vo.setAscendant(ascendant);
        vo.setMc(mc);
        vo.setCreatedAt(chart.getCreateTime() != null
                ? chart.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
        return vo;
    }

    /**
     * 行星列表转 JSON 字符串
     */
    private String toJson(List<PlanetPosition> planets) {
        try {
            return objectMapper.writeValueAsString(planets);
        } catch (Exception ex) {
            log.error("行星数据序列化失败", ex);
            throw new BusinessException(ResultCode.SERVER_ERROR);
        }
    }

    /**
     * JSON 字符串解析为行星列表
     */
    private List<PlanetPosition> parsePlanets(String chartData) {
        if (chartData == null || chartData.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(chartData, new TypeReference<List<PlanetPosition>>() {
            });
        } catch (Exception ex) {
            log.error("行星数据反序列化失败", ex);
            return List.of();
        }
    }
}
