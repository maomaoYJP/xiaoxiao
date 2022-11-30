package com.maomao.miniprogram.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/23 17:04
 */

@Data
public class TalkSaveDTO implements Serializable {
    private static final long serialVersionUID = 5239876521113036787L;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片url
     */
    private String[] urls;

}
