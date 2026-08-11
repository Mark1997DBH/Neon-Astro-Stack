package com.neonastro.modules.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 对话消息
 *
 * @author neonastro
 */
@Data
public class ChatMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 角色：user / assistant（system 由后端注入） */
    @NotBlank(message = "消息角色不能为空")
    private String role;

    /** 消息内容 */
    @NotBlank(message = "消息内容不能为空")
    private String content;
}
