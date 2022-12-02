package com.maomao.miniprogram.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/25 20:19
 */
@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = -1723527117538873260L;

    private String avatar;

    private String nickname;

    private String introduce;

    private String clientId;

    private String email;
}
