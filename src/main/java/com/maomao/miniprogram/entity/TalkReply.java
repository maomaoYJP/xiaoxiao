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
 * @TableName talk_reply
 */
@TableName(value ="talk_reply")
@Data
public class TalkReply implements Serializable {
    /**
     * root_comment_id
     */
    private Long rootCommentId;

    /**
     * comment_id
     */
    private Long commentId;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}