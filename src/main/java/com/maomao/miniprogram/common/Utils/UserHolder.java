package com.maomao.miniprogram.common.Utils;

import com.maomao.miniprogram.entity.User;

/**
 * @author maomao
 * 2022/11/16 22:59
 */
public class UserHolder {
    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    public static void saveUser(User user){
        tl.set(user);
    }

    public static User getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
