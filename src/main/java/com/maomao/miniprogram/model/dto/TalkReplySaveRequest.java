package com.maomao.miniprogram.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/17 16:50
 */
@Data
public class TalkReplySaveRequest implements Serializable {
    private static final long serialVersionUID = 4163559786645348174L;

    private String content;

    private Long parentId;

    private Long rootCommentId;

    private Long talkId;

}
