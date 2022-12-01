package com.maomao.miniprogram.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName update_log
 */
@TableName(value ="update_log")
@Data
public class UpdateLog implements Serializable {
    /**
     * 
     */
    private String content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}