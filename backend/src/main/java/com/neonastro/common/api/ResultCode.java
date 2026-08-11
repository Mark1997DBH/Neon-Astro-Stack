package com.neonastro.common.api;

import com.neonastro.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务错误码枚举
 *
 * @author neonastro
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements ErrorCode {

    /* ====== 通用错误码 ====== */
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权或认证失效"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    SERVER_ERROR(500, "服务器内部错误"),

    /* ====== 用户业务错误码 1xxxx ====== */
    USER_NOT_FOUND(10001, "用户不存在"),
    USERNAME_EXISTS(10002, "用户名已存在"),
    PASSWORD_ERROR(10003, "密码错误"),
    TOKEN_EXPIRED(10004, "令牌已过期"),
    TOKEN_INVALID(10005, "令牌无效"),
    USER_DISABLED(10006, "用户已被禁用"),
    EMAIL_EXISTS(10007, "邮箱已被注册"),
    REFRESH_TOKEN_INVALID(10008, "刷新令牌无效"),

    /* ====== 星盘/运势业务错误码 2xxxx ====== */
    CHART_NOT_FOUND(20001, "星盘不存在"),
    HOROSCOPE_NOT_FOUND(20002, "运势数据不存在"),

    /* ====== AI 对话业务错误码 3xxxx ====== */
    AI_NOT_CONFIGURED(30001, "AI 模型未配置，请在 application.yml 中填写 neon.ai.api-key"),
    AI_CALL_FAILED(30002, "AI 模型调用失败，请稍后重试");

    /** 错误码 */
    private final int code;

    /** 错误消息 */
    private final String message;
}
