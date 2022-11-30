package com.maomao.miniprogram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maomao.miniprogram.entity.User;
import com.maomao.miniprogram.model.vo.UserVO;
import com.maomao.miniprogram.service.UserService;
import com.maomao.miniprogram.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author maomao
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-15 15:43:30
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    UserMapper userMapper;

    @Override
    public List<UserVO> getUserFollow(Long userId) {
        return userMapper.getUserFollow(userId);
    }
}




