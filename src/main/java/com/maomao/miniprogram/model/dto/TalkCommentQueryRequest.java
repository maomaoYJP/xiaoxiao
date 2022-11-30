package com.maomao.miniprogram.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/16 19:51
 */
@Data
public class TalkCommentQueryRequest implements Serializable {
    private static final long serialVersionUID = -2964563010414146249L;

    private Long talkId;

    private Long pageNum = 1L;

    private Long pageSize = 10L;
}
