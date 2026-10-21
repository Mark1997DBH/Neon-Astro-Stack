package com.neonastro.modules.ai.controller;

import com.neonastro.common.api.Result;
import com.neonastro.modules.ai.dto.ChatRequest;
import com.neonastro.modules.ai.dto.ChatResponse;
import com.neonastro.modules.ai.service.AIService;
import com.neonastro.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 占星师对话接口
 *
 * @author neonastro
 */
@Tag(name = "AI 占星师")
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    /**
     * 与 AI 占星师对话（需登录，便于注入用户画像与执行用户相关技能）
     */
    @Operation(summary = "AI 占星师对话")
    @PostMapping("/chat")
    public Result<ChatResponse> chat(@Valid @RequestBody ChatRequest req,
                                     @AuthenticationPrincipal LoginUser loginUser) {
        Long userId = loginUser == null ? null : loginUser.getUserId();
        return Result.success(aiService.chat(req, userId));
    }
}
