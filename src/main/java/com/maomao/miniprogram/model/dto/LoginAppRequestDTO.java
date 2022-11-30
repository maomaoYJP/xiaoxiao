package com.maomao.miniprogram.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/28 16:01
 */

@Data
public class LoginAppRequestDTO implements Serializable {
    private static final long serialVersionUID = 655189614078239053L;

    private String nickname;

    private String password;
}
