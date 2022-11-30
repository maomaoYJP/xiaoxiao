package com.maomao.miniprogram.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/17 16:30
 */
@Data
public class TalkCommentSaveRequest implements Serializable {
    private static final long serialVersionUID = 8694578341304285573L;

    private Long talkId;

    private Long parentId;

    private String content;
}
