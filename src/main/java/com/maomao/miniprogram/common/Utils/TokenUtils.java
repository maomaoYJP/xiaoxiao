package com.maomao.miniprogram.common.Utils;

import com.maomao.miniprogram.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author maomao
 * 2022/11/29 18:28
 */
public class TokenUtils {
    private static Map<String, User> tokenMap = new HashMap<>();


    public static String generateToken(User user){
        //生成唯一不重复的字符串
        String token = UUID.randomUUID().toString();
        tokenMap.put(token,user);
        return token;
    }

    /**
     * 验证token是否合法
     * @param token
     * @return
     */
    public static  boolean verify(String token){
        return tokenMap.containsKey(token);
    }

    public static User gentUser(String token){
        return tokenMap.get(token);
    }

    public static void removeToken(String token){
        tokenMap.remove(token);
    }

}
