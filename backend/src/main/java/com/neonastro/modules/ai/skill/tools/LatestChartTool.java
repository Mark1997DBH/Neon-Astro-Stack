package com.neonastro.modules.ai.skill.tools;

import com.neonastro.common.api.PageResult;
import com.neonastro.modules.ai.skill.AiTool;
import com.neonastro.modules.chart.service.ChartService;
import com.neonastro.modules.chart.vo.ChartVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 技能：查询当前用户最近一次生成的星盘
 *
 * @author neonastro
 */
@Component
@RequiredArgsConstructor
public class LatestChartTool implements AiTool {

    private final ChartService chartService;

    @Override
    public String name() {
        return "get_latest_chart";
    }

    @Override
    public String description() {
        return "查询当前登录用户最近一次生成的本命星盘，返回行星落座、星座内度数、宫位、逆行情况以及上升星座与天顶。"
                + "解读用户星盘前应先调用本技能确认是否有数据";
    }

    @Override
    public Map<String, Object> parameters() {
        return Map.of("type", "object", "properties", Map.of(), "required", List.of());
    }

    @Override
    public Object execute(Map<String, Object> args, Long userId) {
        if (userId == null) {
            throw new IllegalStateException("当前请求未登录，无法获取星盘");
        }
        PageResult<ChartVO> page = chartService.listCharts(userId, 1, 1);
        if (page == null || page.getRecords() == null || page.getRecords().isEmpty()) {
            return Map.of(
                    "found", false,
                    "message", "该用户还没有生成过星盘，请引导其前往「星盘生成」页填写出生信息");
        }
        ChartVO chart = page.getRecords().get(0);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("found", true);
        data.put("name", chart.getName());
        data.put("birthDate", chart.getBirthDate());
        data.put("birthTime", chart.getBirthTime());
        data.put("birthPlace", chart.getBirthPlace());
        data.put("ascendant", chart.getAscendant());
        data.put("mc", chart.getMc());
        data.put("planets", chart.getPlanets());
        return data;
    }
}
