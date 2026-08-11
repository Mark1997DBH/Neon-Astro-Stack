package com.neonastro.modules.ai.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 对话响应 DTO
 *
 * @author neonastro
 */
@Data
public class ChatResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 模型回复内容 */
    private String reply;
}
