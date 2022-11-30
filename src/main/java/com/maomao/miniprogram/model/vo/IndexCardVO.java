package com.maomao.miniprogram.model.vo;

import com.maomao.miniprogram.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/15 23:18
 */

@Data
public class IndexCardVO implements Serializable {
    private static final long serialVersionUID = -96815407170364667L;

    private Long id;

    private String firstPicture;

    private String title;

    private String content;

    private UserVO userVO;

    private Boolean isThumb;

    private Long thumb;


}
