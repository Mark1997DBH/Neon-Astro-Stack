package com.neonastro.modules.ai.skill;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 技能注册表：收集所有 {@link AiTool} 实现，负责输出 tools 定义与分发执行。
 *
 * @author neonastro
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiToolRegistry {

    /** Spring 注入的全部技能实现 */
    private final List<AiTool> tools;

    private final ObjectMapper objectMapper;

    /** 技能名 -> 技能实现 */
    private Map<String, AiTool> toolIndex = Map.of();

    @PostConstruct
    public void init() {
        Map<String, AiTool> index = new LinkedHashMap<>();
        for (AiTool tool : tools) {
            index.put(tool.name(), tool);
        }
        this.toolIndex = Map.copyOf(index);
        log.info("AI 技能注册完成，共 {} 个：{}", toolIndex.size(), toolIndex.keySet());
    }

    /**
     * 输出 OpenAI 兼容的 tools 定义
     */
    public List<Map<String, Object>> toolDefinitions() {
        List<Map<String, Object>> definitions = new ArrayList<>();
        for (AiTool tool : tools) {
            Map<String, Object> function = new LinkedHashMap<>();
            function.put("name", tool.name());
            function.put("description", tool.description());
            function.put("parameters", tool.parameters());
            definitions.add(Map.of("type", "function", "function", function));
        }
        return definitions;
    }

    /**
     * 执行指定技能；失败或技能不存在时返回错误 JSON，交由模型自行兜底回复
     *
     * @param name     技能名
     * @param argsJson 模型给出的参数 JSON 字符串
     * @param userId   当前登录用户 ID
     * @return 观测结果文本
     */
    public String execute(String name, String argsJson, Long userId) {
        AiTool tool = toolIndex.get(name);
        if (tool == null) {
            log.warn("模型请求了未注册的技能：{}", name);
            return "{\"error\":\"未知技能：" + name + "\"}";
        }
        try {
            Map<String, Object> args = StringUtils.hasText(argsJson)
                    ? objectMapper.readValue(argsJson, new TypeReference<>() {
            })
                    : Map.of();
            Object result = tool.execute(args, userId);
            String text = result instanceof String str ? str : objectMapper.writeValueAsString(result);
            log.info("技能 {} 执行成功：{}", name, text);
            return text;
        } catch (Exception ex) {
            log.error("技能 {} 执行失败", name, ex);
            return "{\"error\":\"技能执行失败：" + ex.getMessage() + "\"}";
        }
    }
}
