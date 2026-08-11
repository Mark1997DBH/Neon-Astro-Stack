package com.neonastro.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author neonastro
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    /** 启用 */
    ACTIVE(1),
    /** 禁用 */
    DISABLED(0);

    /** 状态码 */
    private final int code;

    /**
     * 根据 code 获取枚举
     */
    public static UserStatusEnum of(int code) {
        for (UserStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return DISABLED;
    }
}
