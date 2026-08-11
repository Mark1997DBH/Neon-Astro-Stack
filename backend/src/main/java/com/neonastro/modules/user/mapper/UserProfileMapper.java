package com.neonastro.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neonastro.modules.user.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户资料 Mapper
 *
 * @author neonastro
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
}
