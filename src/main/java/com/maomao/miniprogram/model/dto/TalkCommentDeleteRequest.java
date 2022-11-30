package com.maomao.miniprogram.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/26 19:19
 */
@Data
public class TalkCommentDeleteRequest implements Serializable {
    private static final long serialVersionUID = -7727034039582631913L;

    private Long commentId;

    private Long talkId;
}
