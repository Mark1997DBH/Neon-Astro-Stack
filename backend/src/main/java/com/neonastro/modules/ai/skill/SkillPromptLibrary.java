package com.neonastro.modules.ai.skill;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 提示词技能库：从 classpath:skills/*.md 加载技能文档，按用户提问匹配后拼接进 system prompt。
 * <p>
 * 技能文件格式（前置元信息 + 正文）：
 * <pre>
 * ---
 * name: 星盘解读
 * keywords: 星盘,本命盘,上升星座
 * ---
 * 技能正文...
 * </pre>
 *
 * @author neonastro
 */
@Slf4j
@Component
public class SkillPromptLibrary {

    /** 技能文件位置（打包后位于 classpath:skills/ 下） */
    private static final String SKILL_LOCATION = "classpath*:skills/*.md";

    /** 已加载的技能文档 */
    private final List<SkillDoc> skills = new ArrayList<>();

    /**
     * 启动时加载全部技能文档
     */
    @PostConstruct
    public void load() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(SKILL_LOCATION);
            for (Resource resource : resources) {
                try {
                    SkillDoc doc = parse(resource.getContentAsString(StandardCharsets.UTF_8));
                    if (doc != null) {
                        skills.add(doc);
                    } else {
                        log.warn("技能文件格式不合规，已跳过：{}", resource.getFilename());
                    }
                } catch (Exception ex) {
                    log.warn("技能文件加载失败：{}，原因：{}", resource.getFilename(), ex.getMessage());
                }
            }
            log.info("提示词技能库加载完成，共 {} 个：{}", skills.size(), skills.stream().map(SkillDoc::name).toList());
        } catch (Exception ex) {
            log.error("提示词技能库加载失败", ex);
        }
    }

    /**
     * 按用户最新提问匹配技能，返回可拼接进 system prompt 的文本；未命中返回空串
     *
     * @param userText 用户最新一条提问
     * @return 命中的技能提示词（可能为空串）
     */
    public String match(String userText) {
        if (!StringUtils.hasText(userText)) {
            return "";
        }
        StringBuilder matched = new StringBuilder();
        for (SkillDoc doc : skills) {
            boolean hit = doc.keywords().stream().anyMatch(userText::contains);
            if (hit) {
                matched.append("\n【技能：").append(doc.name()).append("】\n")
                        .append(doc.prompt()).append('\n');
            }
        }
        return matched.toString();
    }

    /**
     * 解析技能文件：前置元信息（name / keywords）+ 正文
     */
    private SkillDoc parse(String text) {
        String content = text.strip();
        if (!content.startsWith("---")) {
            return null;
        }
        int headerEnd = content.indexOf("\n---", 3);
        if (headerEnd < 0) {
            return null;
        }
        String header = content.substring(3, headerEnd).strip();
        String body = content.substring(headerEnd + 4).strip();

        String name = null;
        List<String> keywords = new ArrayList<>();
        for (String line : header.split("\n")) {
            String[] kv = line.split(":", 2);
            if (kv.length < 2) {
                continue;
            }
            String key = kv[0].strip();
            String value = kv[1].strip();
            if ("name".equals(key)) {
                name = value;
            } else if ("keywords".equals(key)) {
                for (String keyword : value.split(",")) {
                    if (StringUtils.hasText(keyword)) {
                        keywords.add(keyword.strip());
                    }
                }
            }
        }
        if (!StringUtils.hasText(name) || keywords.isEmpty() || !StringUtils.hasText(body)) {
            return null;
        }
        return new SkillDoc(name, body, keywords);
    }

    /**
     * 技能文档
     *
     * @param name     技能名称
     * @param prompt   技能提示词正文
     * @param keywords 触发关键词
     */
    public record SkillDoc(String name, String prompt, List<String> keywords) {
    }
}
