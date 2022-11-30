package com.maomao.miniprogram.service;

import com.maomao.miniprogram.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maomao.miniprogram.model.vo.UserVO;

import java.util.List;

/**
* @author maomao
* @description 针对表【user】的数据库操作Service
* @createDate 2022-11-15 15:43:30
*/
public interface UserService extends IService<User> {


    /**
     * 获取用户的关注
     * @param userId
     * @return
     */
    List<UserVO> getUserFollow(Long userId);

    /**
     * 获取用户被谁关注
     * @param userId
     * @return
     */
    List<UserVO> getUserBeFollowed(Long userId);
}
