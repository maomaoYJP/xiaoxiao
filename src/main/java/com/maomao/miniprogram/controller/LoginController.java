package com.maomao.miniprogram.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maomao.miniprogram.common.BaseResponse;
import com.maomao.miniprogram.common.ErrorCode;
import com.maomao.miniprogram.common.Utils.HttpUtils;
import com.maomao.miniprogram.common.Utils.MyUtils;
import com.maomao.miniprogram.common.Utils.ResultUtils;
import com.maomao.miniprogram.common.Utils.TokenUtils;
import com.maomao.miniprogram.entity.User;
import com.maomao.miniprogram.exception.BusinessException;
import com.maomao.miniprogram.model.dto.LoginAppRequestDTO;
import com.maomao.miniprogram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.maomao.miniprogram.constant.UserConstant.USER_LOGIN_STATE;
import static com.maomao.miniprogram.constant.WXConstant.WX_APP_ID;
import static com.maomao.miniprogram.constant.WXConstant.WX_APP_SECRET;

/**
 * @author maomao
 * 2022/11/10 16:49
 */

@RestController
@Slf4j
public class LoginController {

    @Resource
    UserService userService;

    @GetMapping(value = "wxapi/decryptCode")
    public BaseResponse<String> decodeOpenid(@RequestParam("code") String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=" + WX_APP_ID;
        url += "&secret=" + WX_APP_SECRET;
        url += "&js_code=" + code;
        url += "&grant_type=authorization_code";
        url += "&connect_redirect=1";

        String res = HttpUtils.sentGet(url);
        return ResultUtils.success(res);
    }

    @GetMapping("/login")
    public BaseResponse<String> login(String openid, HttpSession session){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        User user = userService.getOne(wrapper);
        if(user != null){
            session.setAttribute(USER_LOGIN_STATE,user);
            return ResultUtils.success(session.getId());
        }
        throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"没有对应用户");
    }

    @PostMapping("login/app")
    public BaseResponse<String> loginApp(@RequestBody LoginAppRequestDTO loginAppRequestDTO){
        if(!MyUtils.isNotNull(loginAppRequestDTO)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname",loginAppRequestDTO.getNickname());
        wrapper.eq("password",loginAppRequestDTO.getPassword());
        User user = userService.getOne(wrapper);
        if(user != null){
            //生成token
            String token = TokenUtils.generateToken(user);
            return ResultUtils.success(token);
        }
        throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"没有对应用户");

    }

    @GetMapping("/loginOut")
    public BaseResponse<String> loginOut(HttpServletRequest request){
        String token = request.getHeader("token");
        if(token != null){
            TokenUtils.removeToken(token);
            return ResultUtils.success("注销成功");
        }

        throw new BusinessException(ErrorCode.OPERATION_ERROR);
    }

    @GetMapping("/register")
    public BaseResponse<User> register(@RequestParam("openid") String openid){
        User user = new User();
        user.setOpenid(openid);
        if(userService.save(user)){
            return ResultUtils.success(user);
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR);
    }
}
