package com.neonastro.modules.user.service;

import com.neonastro.modules.user.dto.UpdateProfileReq;
import com.neonastro.modules.user.vo.UserProfileVO;

/**
 * 用户服务接口
 *
 * @author neonastro
 */
public interface UserService {

    /**
     * 获取当前用户资料
     *
     * @param userId 用户 ID
     * @return 用户资料
     */
    UserProfileVO getProfile(Long userId);

    /**
     * 更新当前用户资料（支持部分更新）
     *
     * @param userId 用户 ID
     * @param req    更新请求
     * @return 更新后的用户资料
     */
    UserProfileVO updateProfile(Long userId, UpdateProfileReq req);
}
