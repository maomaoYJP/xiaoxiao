package com.maomao.miniprogram.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maomao.miniprogram.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maomao.miniprogram.model.dto.TalkCommentDeleteRequest;
import com.maomao.miniprogram.model.dto.TalkCommentQueryRequest;
import com.maomao.miniprogram.model.dto.TalkCommentSaveRequest;
import com.maomao.miniprogram.model.dto.TalkReplySaveRequest;
import com.maomao.miniprogram.model.vo.CommentVO;

import java.util.List;

/**
* @author maomao
* @description 针对表【comment】的数据库操作Service
* @createDate 2022-11-15 16:04:29
*/
public interface CommentService extends IService<Comment> {

    /**
     * 分页获取talk的评论
     * @param talkCommentQueryRequest
     * @param userId
     * @return
     */
    Page<CommentVO> getTalkComment(TalkCommentQueryRequest talkCommentQueryRequest, Long userId);

    /**
     * 评论点赞 取消点赞
     * @param commentId
     * @param userId
     * @return
     */
    Integer doCommentThumb(Long commentId, Long userId);

    /**
     * 创建talk顶级评论
     * @param talkCommentSaveRequest
     * @param userId
     * @return
     */
    Boolean saveTopTalkComment(TalkCommentSaveRequest talkCommentSaveRequest, Long userId);

    /**
     * 创建talk顶级评论回复
     * @param talkReplySaveRequest
     * @param userId
     * @return
     */
    Boolean saveTopTalkReply(TalkReplySaveRequest talkReplySaveRequest, Long userId);

    /**
     * 删除评论
     * @param talkCommentDeleteRequest
     * @return
     */
    Boolean deleteComment(TalkCommentDeleteRequest talkCommentDeleteRequest);
}
