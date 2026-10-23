package com.neonastro.modules.ai.skill;

import java.util.Map;

/**
 * AI 技能（工具调用）抽象：每个实现即一个可被模型调用的技能。
 * <p>
 * 实现类标注 {@code @Component} 后由 {@link AiToolRegistry} 自动收集，
 * 并转换为 OpenAI 兼容的 {@code tools} 定义下发给模型。
 *
 * @author neonastro
 */
public interface AiTool {

    /**
     * 技能名（模型调用时使用的函数名，建议英文下划线命名）
     */
    String name();

    /**
     * 技能描述（模型据此判断何时调用，描述越具体越准确）
     */
    String description();

    /**
     * 参数 JSON Schema（type / properties / required）
     */
    Map<String, Object> parameters();

    /**
     * 执行技能
     *
     * @param args   模型给出的参数（已从 JSON 解析）
     * @param userId 当前登录用户 ID（可能为 null）
     * @return 返回给模型的观测结果（对象会被序列化为 JSON，字符串直接透传）
     * @throws Exception 执行失败时抛出，由注册表统一兜底为错误信息返回给模型
     */
    Object execute(Map<String, Object> args, Long userId) throws Exception;
}
