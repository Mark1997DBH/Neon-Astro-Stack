package com.neonastro.modules.ai.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neonastro.common.api.ResultCode;
import com.neonastro.common.exception.BusinessException;
import com.neonastro.modules.ai.dto.ChatMessage;
import com.neonastro.modules.ai.dto.ChatRequest;
import com.neonastro.modules.ai.dto.ChatResponse;
import com.neonastro.modules.ai.service.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * AI 对话服务实现（基于 OpenAI 兼容接口，通过 RestClient 调用）
 * 模型地址 / API Key / 模型名均在 application.yml 的 neon.ai.* 中配置
 *
 * @author neonastro
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    /** OpenAI 兼容接口地址（如 https://api.openai.com/v1 或自建服务） */
    @Value("${neon.ai.base-url}")
    private String baseUrl;

    /** 模型 API Key（环境变量注入，勿提交到仓库） */
    @Value("${neon.ai.api-key:}")
    private String apiKey;

    /** 模型名称 */
    @Value("${neon.ai.model:gpt-4o-mini}")
    private String model;

    /** 系统提示词：AI 占星师人设 */
    @Value("${neon.ai.system-prompt}")
    private String systemPrompt;

    /** 采样温度 */
    @Value("${neon.ai.temperature:0.8}")
    private double temperature;

    private final ObjectMapper objectMapper;

    @Override
    public ChatResponse chat(ChatRequest req) {
        if (!StringUtils.hasText(apiKey)) {
            log.warn("AI 模型未配置 api-key，拒绝对话请求");
            throw new BusinessException(ResultCode.AI_NOT_CONFIGURED);
        }

        // 组装请求体：系统提示词 + 当前日期（让模型知道"今天"）+ 会话消息
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", temperature);
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", buildSystemPrompt()));
        for (ChatMessage msg : req.getMessages()) {
            messages.add(Map.of("role", msg.getRole(), "content", msg.getContent()));
        }
        requestBody.put("messages", messages);

        try {
            RestClient restClient = RestClient.builder()
                    .baseUrl(baseUrl)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            JsonNode response = restClient.post()
                    .uri("/chat/completions")
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            String reply = extractReply(response);
            log.info("AI 对话成功：model={}, 回复长度={}", model, reply.length());

            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setReply(reply);
            return chatResponse;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("AI 模型调用失败：baseUrl={}, model={}", baseUrl, model, ex);
            throw new BusinessException(ResultCode.AI_CALL_FAILED, "AI 模型调用失败：" + ex.getMessage());
        }
    }

    /**
     * 构建系统提示词：基础人设 + 当前日期时间（Asia/Shanghai）
     * 让模型明确知道"今天"是哪一天，运势解读才准确
     */
    private String buildSystemPrompt() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        String todayText = today.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 EEEE", Locale.CHINA));
        return systemPrompt + "\n今天是：" + todayText + "。";
    }

    /**
     * 从 OpenAI 兼容响应中提取回复文本
     * 响应结构：{ choices: [ { message: { role, content } } ] }
     */
    private String extractReply(JsonNode response) {
        try {
            if (response == null || !response.hasNonNull("choices")
                    || response.get("choices").isEmpty()) {
                log.warn("AI 响应结构异常：{}", response);
                throw new BusinessException(ResultCode.AI_CALL_FAILED);
            }
            JsonNode message = response.get("choices").get(0).get("message");
            String content = message.get("content").asText();
            if (!StringUtils.hasText(content)) {
                throw new BusinessException(ResultCode.AI_CALL_FAILED);
            }
            return content.trim();
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("AI 响应解析失败：{}", response, ex);
            throw new BusinessException(ResultCode.AI_CALL_FAILED);
        }
    }
}
