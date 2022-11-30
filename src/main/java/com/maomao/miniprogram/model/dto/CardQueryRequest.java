package com.maomao.miniprogram.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author maomao
 * 2022/11/15 22:58
 */
@Data
public class CardQueryRequest implements Serializable {

    private static final long serialVersionUID = 733988519809843791L;

    private int pageNum;

    private int pageSize;
}
