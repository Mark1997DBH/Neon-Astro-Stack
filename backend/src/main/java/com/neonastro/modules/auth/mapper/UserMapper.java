package com.neonastro.modules.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neonastro.modules.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户 Mapper
 *
 * @author neonastro
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户（包含逻辑删除前的所有记录由 SQL 控制）
     *
     * @param username 用户名
     * @return 用户实体
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据用户 ID 查询其角色编码列表（sys_user_role 关联 sys_role）
     *
     * @param userId 用户 ID
     * @return 角色编码列表，如 [ROLE_USER, ROLE_ADMIN]
     */
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 为用户绑定指定角色（按角色编码关联 sys_role 插入）
     *
     * @param userId   用户 ID
     * @param roleCode 角色编码
     */
    void insertUserRole(@Param("userId") Long userId, @Param("roleCode") String roleCode);
}
