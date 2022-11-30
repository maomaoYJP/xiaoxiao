package com.maomao.miniprogram.model.enums;

/**
 * 性别枚举
 *
 * @author maomao
 * 2022/10/30 16:39
 */
public enum SexEnum {
    MALE("男", 0),
    FEMALE("女", 1);

    private final String text;

    private final int value;

    SexEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
