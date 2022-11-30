package com.maomao.miniprogram.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/15 23:29
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 2687907529512951784L;

    private Long id;

    private String avatar;

    private String nickname;

    private String clientId;
}
