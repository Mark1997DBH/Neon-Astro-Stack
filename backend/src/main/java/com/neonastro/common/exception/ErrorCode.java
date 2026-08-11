package com.neonastro.common.exception;

/**
 * 错误码接口，统一规范错误码与消息的获取方式
 *
 * @author neonastro
 */
public interface ErrorCode {

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    int getCode();

    /**
     * 获取错误消息
     *
     * @return 错误消息
     */
    String getMessage();
}
