package com.neonastro.modules.ai.service;

import com.neonastro.modules.ai.dto.ChatRequest;
import com.neonastro.modules.ai.dto.ChatResponse;

/**
 * AI 对话服务接口
 *
 * @author neonastro
 */
public interface AIService {

    /**
     * 与 AI 占星师对话（调用 OpenAI 兼容接口，支持技能调用与上下文注入）
     *
     * @param req    对话请求（含会话消息列表）
     * @param userId 当前登录用户 ID，用于注入用户画像与执行用户相关技能
     * @return 模型回复
     */
    ChatResponse chat(ChatRequest req, Long userId);
}
