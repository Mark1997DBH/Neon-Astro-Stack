package com.neonastro.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neonastro.common.enums.UserStatusEnum;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * 登录用户信息，实现 Spring Security UserDetails
 *
 * @author neonastro
 */
@Data
public class LoginUser implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户 ID */
    private Long userId;

    /** 用户名 */
    private String username;

    /** 密码（密文） */
    @JsonIgnore
    private String password;

    /** 状态：1 启用，0 禁用 */
    private Integer status;

    /** 权限集合 */
    @JsonIgnore
    private Set<SimpleGrantedAuthority> authorities;

    public LoginUser() {
    }

    public LoginUser(Long userId, String username, String password, Integer status,
                     Set<SimpleGrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.authorities = authorities;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 基于用户状态判断是否启用
     */
    @Override
    public boolean isEnabled() {
        return status != null && UserStatusEnum.ACTIVE.getCode() == status;
    }
}
