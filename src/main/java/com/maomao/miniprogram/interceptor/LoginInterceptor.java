package com.maomao.miniprogram.interceptor;

import com.maomao.miniprogram.common.ErrorCode;
import com.maomao.miniprogram.common.Utils.TokenUtils;
import com.maomao.miniprogram.common.Utils.UserHolder;
import com.maomao.miniprogram.entity.User;
import com.maomao.miniprogram.exception.BusinessException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.maomao.miniprogram.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author maomao
 * 2022/11/16 21:59
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求头里面是否携带了token
        String token = request.getHeader("token");

        if(token != null){
            User user = TokenUtils.gentUser(token);
            //将user放到ThreadLocal中
            UserHolder.saveUser(user);
            return true;
        }

        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"未登录" + request.getRequestURL().toString());
    }
}
