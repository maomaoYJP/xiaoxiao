package com.maomao.miniprogram.common;

import com.maomao.miniprogram.constant.CommonConstant;
import lombok.Data;

/**
 * @author maomao
 * 2022/11/2 10:29
 */

@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;

}
