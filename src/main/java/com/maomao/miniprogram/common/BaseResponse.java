package com.maomao.miniprogram.common;

import lombok.Data;

/**
 * @author maomao
 * 2022/10/29 22:57
 */
@Data
public class BaseResponse<T> {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(), null ,errorCode.getMessage());
    }
}
