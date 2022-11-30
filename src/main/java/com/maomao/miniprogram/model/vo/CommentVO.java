package com.maomao.miniprogram.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author maomao
 * 2022/11/16 16:51
 */

@Data
public class CommentVO implements Serializable {
    private static final long serialVersionUID = 164446684606844237L;

    private Long id;

    /**
     * 父评论id
     */
    private Long parentId;

    /**
     * 评论用户id
     */
    private Long userId;

    /**
     * 评论用户头像
     */
    private String avatar;

    /**
     * 评论用户昵称
     */
    private String nickname;

    /**
     * 评论用户的设备码
     */
    private String clientId;

    /**
     * 评论的所有回复
     */
    private List<CommentVO> replyCommentVOList;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 用户是否点赞
     */
    private Boolean isThumb;

    /**
     * 是否是拥有者
     */
    private Boolean owner;

    /**
     * 评论点赞数量
     */
    private Long thumb;

    /**
     *  创建时间
     */
    private Date createTime;

}
