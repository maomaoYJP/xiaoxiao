package com.maomao.miniprogram.common.Utils;

/**
 * @author maomao
 * 2022/11/23 17:22
 */
public class MyUtils {

    public static boolean isNotNull(Object o){
        return o != null && !"".equals(o) && !"null".equals(o);
    }
}
