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
 * @TableName talk_comment
 */
@TableName(value ="talk_comment")
@Data
public class TalkComment implements Serializable {
    /**
     * user_id
     */
    private Long talkId;

    /**
     * comment_id
     */
    private Long commentId;

    /**
     * comment_user_id
     */
    private Long commentUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}