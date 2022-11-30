package com.maomao.miniprogram.controller;

import com.maomao.miniprogram.common.BaseResponse;
import com.maomao.miniprogram.common.ErrorCode;
import com.maomao.miniprogram.common.Utils.MyUtils;
import com.maomao.miniprogram.common.Utils.ResultUtils;
import com.maomao.miniprogram.common.Utils.UserHolder;
import com.maomao.miniprogram.entity.User;
import com.maomao.miniprogram.exception.BusinessException;
import com.maomao.miniprogram.model.dto.UserUpdateRequest;
import com.maomao.miniprogram.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author maomao
 * 2022/11/25 19:47
 */

@RestController
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/user/get")
    public BaseResponse<User> getUser(){
        User user = UserHolder.getUser();
        User latestUser = userService.getById(user.getId());
        return ResultUtils.success(latestUser);
    }

    @PostMapping("/user/update")
    public BaseResponse<Boolean> userUpdate(@RequestBody UserUpdateRequest userUpdateRequest){
        if(!MyUtils.isNotNull(userUpdateRequest)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = new User();
        user.setId(UserHolder.getUser().getId());
        user.setNickname(userUpdateRequest.getNickname());
        user.setAvatar(userUpdateRequest.getAvatar());
        user.setIntroduce(userUpdateRequest.getIntroduce());
        boolean b = userService.updateById(user);

        return ResultUtils.success(b);
    }
}
