package com.neonastro.modules.horoscope.controller;

import com.neonastro.common.api.Result;
import com.neonastro.modules.horoscope.service.HoroscopeService;
import com.neonastro.modules.horoscope.vo.DailyHoroscopeVO;
import com.neonastro.modules.horoscope.vo.WeeklyHoroscopeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 运势接口
 *
 * @author neonastro
 */
@Tag(name = "运势管理")
@RestController
@RequestMapping("/horoscope")
@RequiredArgsConstructor
public class HoroscopeController {

    private final HoroscopeService horoscopeService;

    /**
     * 获取指定星座今日运势
     */
    @Operation(summary = "获取每日运势")
    @GetMapping("/daily/{sign}")
    public Result<DailyHoroscopeVO> getDaily(@PathVariable String sign) {
        return Result.success(horoscopeService.getDaily(sign));
    }

    /**
     * 获取指定星座本周运势
     */
    @Operation(summary = "获取每周运势")
    @GetMapping("/weekly/{sign}")
    public Result<WeeklyHoroscopeVO> getWeekly(@PathVariable String sign) {
        return Result.success(horoscopeService.getWeekly(sign));
    }
}
