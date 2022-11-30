package com.maomao.miniprogram.exception;

import com.maomao.miniprogram.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author maomao
 * 2022/10/29 23:27
 */
public class BusinessException extends RuntimeException{

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
