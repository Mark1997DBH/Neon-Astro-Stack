package com.neonastro.modules.ai.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neonastro.common.api.ResultCode;
import com.neonastro.common.exception.BusinessException;
import com.neonastro.modules.ai.dto.ChatMessage;
import com.neonastro.modules.ai.dto.ChatRequest;
import com.neonastro.modules.ai.dto.ChatResponse;
import com.neonastro.modules.ai.service.AIService;
import com.neonastro.modules.ai.skill.AiToolRegistry;
import com.neonastro.modules.ai.skill.SkillPromptLibrary;
import com.neonastro.modules.user.service.UserService;
import com.neonastro.modules.user.vo.UserProfileVO;
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
 * <p>
 * 模型地址 / API Key / 模型名 / 技能开关均在 application.yml 的 neon.ai.* 中配置。
 * 上下文由三部分构成：基础人设 + 命中技能提示词 + 当前用户画像；历史消息按配置条数裁剪。
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

    /** 技能开关：同时控制提示词技能库与工具调用技能 */
    @Value("${neon.ai.skills.enabled:true}")
    private boolean skillsEnabled;

    /** 单次对话最多执行技能（工具调用）的轮数 */
    @Value("${neon.ai.tool-max-rounds:3}")
    private int toolMaxRounds;

    /** 单次对话最多携带的历史消息条数（上下文裁剪） */
    @Value("${neon.ai.history-max-messages:20}")
    private int historyMaxMessages;

    private final ObjectMapper objectMapper;

    /** 工具调用技能注册表 */
    private final AiToolRegistry toolRegistry;

    /** 提示词技能库 */
    private final SkillPromptLibrary skillPromptLibrary;

    /** 用户资料（用于注入上下文画像） */
    private final UserService userService;

    @Override
    public ChatResponse chat(ChatRequest req, Long userId) {
        if (!StringUtils.hasText(apiKey)) {
            log.warn("AI 模型未配置 api-key，拒绝对话请求");
            throw new BusinessException(ResultCode.AI_NOT_CONFIGURED);
        }

        // 组装消息：system（人设 + 技能 + 用户画像） + 裁剪后的历史消息
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message("system", buildSystemPrompt(userId, latestUserText(req))));
        messages.addAll(trimHistory(req.getMessages()));

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", temperature);

        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        try {
            // 技能调用循环：模型每次请求技能 -> 本地执行 -> 结果回填 -> 再次推理，直到产出最终文本
            for (int round = 0; ; round++) {
                boolean toolsEnabled = skillsEnabled && round < toolMaxRounds;
                if (toolsEnabled) {
                    requestBody.put("tools", toolRegistry.toolDefinitions());
                    requestBody.put("tool_choice", "auto");
                } else {
                    // 达到轮数上限后不再下发技能，强制模型给出最终回复
                    requestBody.remove("tools");
                    requestBody.remove("tool_choice");
                }
                requestBody.put("messages", messages);

                JsonNode response = restClient.post()
                        .uri("/chat/completions")
                        .body(requestBody)
                        .retrieve()
                        .body(JsonNode.class);

                JsonNode message = response == null ? null : response.path("choices").path(0).path("message");
                JsonNode toolCalls = message == null ? null : message.get("tool_calls");

                if (!toolsEnabled || toolCalls == null || toolCalls.isEmpty()) {
                    String reply = extractReply(response);
                    log.info("AI 对话成功：model={}, 技能轮数={}, 回复长度={}", model, round, reply.length());
                    ChatResponse chatResponse = new ChatResponse();
                    chatResponse.setReply(reply);
                    return chatResponse;
                }

                // 记录模型发起的技能调用，再逐个执行并把结果以 tool 角色回填
                messages.add(objectMapper.convertValue(message, new TypeReference<Map<String, Object>>() {
                }));
                for (JsonNode call : toolCalls) {
                    String toolName = call.path("function").path("name").asText();
                    String argsJson = call.path("function").path("arguments").asText();
                    Map<String, Object> toolMessage = new LinkedHashMap<>();
                    toolMessage.put("role", "tool");
                    toolMessage.put("tool_call_id", call.path("id").asText());
                    toolMessage.put("content", toolRegistry.execute(toolName, argsJson, userId));
                    messages.add(toolMessage);
                }
            }
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("AI 模型调用失败：baseUrl={}, model={}", baseUrl, model, ex);
            throw new BusinessException(ResultCode.AI_CALL_FAILED, "AI 模型调用失败：" + ex.getMessage());
        }
    }

    /**
     * 构建 system 提示词：基础人设 + 当前日期 + 命中的技能提示词 + 当前用户画像
     */
    private String buildSystemPrompt(Long userId, String userText) {
        StringBuilder prompt = new StringBuilder(systemPrompt);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        prompt.append("\n今天是：")
                .append(today.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 EEEE", Locale.CHINA)))
                .append("。");

        // 上下文一：按用户提问匹配到的技能规则
        if (skillsEnabled) {
            prompt.append(skillPromptLibrary.match(userText));
        }

        // 上下文二：当前登录用户画像，让模型知道"对面是谁"
        String profile = describeUser(userId);
        if (StringUtils.hasText(profile)) {
            prompt.append("\n【当前用户资料】").append(profile)
                    .append("（可结合该资料个性化回答，这些已知信息无需再向用户确认）");
        }
        return prompt.toString();
    }

    /**
     * 描述当前用户画像（昵称 / 星座 / 生日 / 简介），查询失败时降级为空串
     */
    private String describeUser(Long userId) {
        if (userId == null) {
            return "";
        }
        try {
            UserProfileVO profile = userService.getProfile(userId);
            if (profile == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            if (StringUtils.hasText(profile.getNickname())) {
                sb.append("昵称：").append(profile.getNickname()).append("；");
            }
            if (StringUtils.hasText(profile.getZodiacSign())) {
                sb.append("星座：").append(profile.getZodiacSign()).append("；");
            }
            if (StringUtils.hasText(profile.getBirthday())) {
                sb.append("生日：").append(profile.getBirthday()).append("；");
            }
            if (StringUtils.hasText(profile.getBio())) {
                sb.append("个人简介：").append(profile.getBio()).append("；");
            }
            return sb.toString();
        } catch (Exception ex) {
            log.warn("注入用户画像失败：userId={}, 原因：{}", userId, ex.getMessage());
            return "";
        }
    }

    /**
     * 取最后一条用户消息，用于技能提示词匹配
     */
    private String latestUserText(ChatRequest req) {
        List<ChatMessage> list = req.getMessages();
        for (int i = list.size() - 1; i >= 0; i--) {
            if ("user".equalsIgnoreCase(list.get(i).getRole())) {
                return list.get(i).getContent();
            }
        }
        return "";
    }

    /**
     * 上下文裁剪：只保留最近 N 条历史消息，控制 token 与响应延迟
     */
    private List<Map<String, Object>> trimHistory(List<ChatMessage> history) {
        int from = Math.max(0, history.size() - historyMaxMessages);
        List<Map<String, Object>> result = new ArrayList<>();
        for (ChatMessage msg : history.subList(from, history.size())) {
            result.add(message(msg.getRole(), msg.getContent()));
        }
        return result;
    }

    private Map<String, Object> message(String role, String content) {
        Map<String, Object> message = new LinkedHashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
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
