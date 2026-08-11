package com.neonastro.modules.user.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户资料视图对象（与前端 UserVO 类型对齐）
 *
 * @author neonastro
 */
@Data
public class UserProfileVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户 ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 邮箱 */
    private String email;

    /** 头像 URL */
    private String avatar;

    /** 个人简介 */
    private String bio;

    /** 星座（英文，如 Scorpio） */
    private String zodiacSign;

    /** 出生日期（yyyy-MM-dd） */
    private String birthday;

    /** 创建时间 */
    private String createdAt;
}
