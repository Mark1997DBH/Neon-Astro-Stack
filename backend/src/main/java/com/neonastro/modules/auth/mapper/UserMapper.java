package com.neonastro.modules.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neonastro.modules.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
