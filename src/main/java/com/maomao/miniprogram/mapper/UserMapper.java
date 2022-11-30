package com.maomao.miniprogram.mapper;

import com.maomao.miniprogram.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maomao.miniprogram.model.vo.UserVO;

import java.util.List;

/**
* @author maomao
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-11-15 15:43:30
* @Entity com.maomao.miniprogram.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取用户关注
     * @param userId
     * @return
     */
    List<UserVO> getUserFollow(Long userId);

    /**
     * 获取用户被哪些用户关注
     * @param userId
     * @return
     */
    List<UserVO> getUserBeFollowed(Long userId);
}




