package com.neonastro.security;

import com.neonastro.modules.auth.entity.User;
import com.neonastro.modules.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户详情服务实现，供 Spring Security 认证使用
 *
 * @author neonastro
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /** 兜底角色：历史数据无角色绑定记录时授予 */
    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }
        // 从 sys_user_role 关联表动态加载角色，避免硬编码权限
        List<String> roles = userMapper.selectRolesByUserId(user.getId());
        Set<SimpleGrantedAuthority> authorities;
        if (roles == null || roles.isEmpty()) {
            log.warn("用户 {} 无角色绑定记录，授予兜底角色 {}", user.getUsername(), DEFAULT_ROLE);
            authorities = Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE));
        } else {
            authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        return new LoginUser(user.getId(), user.getUsername(), user.getPassword(),
                user.getStatus(), authorities);
    }
}
