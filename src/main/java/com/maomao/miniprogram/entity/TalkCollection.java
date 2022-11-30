package com.maomao.miniprogram.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @author maomao
 * @TableName talk_collection
 */
@TableName(value ="talk_collection")
@Data
public class TalkCollection implements Serializable {
    /**
     * user_id
     */
    private Long userId;

    /**
     * talk_id
     */
    private Long talkId;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}