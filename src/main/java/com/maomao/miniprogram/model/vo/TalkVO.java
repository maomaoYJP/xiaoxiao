package com.maomao.miniprogram.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author maomao
 * 2022/11/16 16:46
 */

@Data
public class TalkVO implements Serializable {

    private static final long serialVersionUID = -6751578284960003737L;

    private Long id;

    private List<String> pictureList;

    private String title;

    private String content;

    private UserVO userVO;

    private Long comment;

    private Boolean isThumb;

    private Long thumb;

    private Long view;

    private Long collection;

}
