package com.neonastro.modules.chart.controller;

import com.neonastro.common.api.PageResult;
import com.neonastro.common.api.Result;
import com.neonastro.modules.chart.dto.GenerateChartReq;
import com.neonastro.modules.chart.service.ChartService;
import com.neonastro.modules.chart.vo.ChartVO;
import com.neonastro.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 星盘接口
 *
 * @author neonastro
 */
@Tag(name = "星盘管理")
@RestController
@RequestMapping("/chart")
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    /**
     * 生成星盘
     */
    @Operation(summary = "生成星盘")
    @PostMapping
    public Result<ChartVO> generate(@Valid @RequestBody GenerateChartReq req) {
        return Result.success(chartService.generate(req, getCurrentUserId()));
    }

    /**
     * 查询星盘详情
     */
    @Operation(summary = "查询星盘详情")
    @GetMapping("/{id}")
    public Result<ChartVO> getChart(@PathVariable Long id) {
        return Result.success(chartService.getChart(id, getCurrentUserId()));
    }

    /**
     * 分页查询当前用户星盘列表
     */
    @Operation(summary = "分页查询星盘列表")
    @GetMapping
    public Result<PageResult<ChartVO>> listCharts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(chartService.listCharts(getCurrentUserId(), page, pageSize));
    }

    /**
     * 从安全上下文获取当前登录用户 ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            return loginUser.getUserId();
        }
        return null;
    }
}
