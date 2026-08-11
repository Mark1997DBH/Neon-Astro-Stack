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
     * 与 AI 占星师对话（调用 OpenAI 兼容接口）
     *
     * @param req 对话请求（含会话消息列表）
     * @return 模型回复
     */
    ChatResponse chat(ChatRequest req);
}
