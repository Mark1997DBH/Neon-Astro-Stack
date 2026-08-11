package com.neonastro.modules.ai.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 对话请求 DTO
 *
 * @author neonastro
 */
@Data
public class ChatRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 会话消息列表（按时间顺序，由客户端维护上下文） */
    @NotEmpty(message = "消息列表不能为空")
    @Valid
    private List<ChatMessage> messages;
}
