package com.neonastro.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新用户资料请求 DTO（支持部分更新，空值不更新）
 *
 * @author neonastro
 */
@Data
public class UpdateProfileReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 昵称 */
    private String nickname;

    /** 邮箱 */
    @Email(message = "邮箱格式不正确")
    private String email;

    /** 出生日期（yyyy-MM-dd） */
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "出生日期格式必须为 yyyy-MM-dd")
    private String birthday;

    /** 个人简介 */
    private String bio;

    /** 头像 URL */
    private String avatar;
}
