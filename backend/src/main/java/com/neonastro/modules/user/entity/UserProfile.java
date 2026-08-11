package com.neonastro.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.neonastro.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 用户资料实体，对应 user_profile 表
 *
 * @author neonastro
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_profile")
public class UserProfile extends BaseEntity {

    /** 关联用户 ID */
    @TableField("user_id")
    private Long userId;

    /** 出生日期 */
    @TableField("birth_date")
    private LocalDate birthDate;

    /** 出生时间 */
    @TableField("birth_time")
    private LocalTime birthTime;

    /** 出生地点 */
    @TableField("birth_place")
    private String birthPlace;

    /** 性别：0 未知，1 男，2 女 */
    private Integer gender;

    /** 个人简介 */
    private String bio;
}
