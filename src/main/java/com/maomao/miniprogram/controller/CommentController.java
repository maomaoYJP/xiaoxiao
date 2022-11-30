package com.maomao.miniprogram.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maomao.miniprogram.common.BaseResponse;
import com.maomao.miniprogram.common.ErrorCode;
import com.maomao.miniprogram.common.Utils.MyUtils;
import com.maomao.miniprogram.common.Utils.ResultUtils;
import com.maomao.miniprogram.common.Utils.UserHolder;
import com.maomao.miniprogram.entity.User;
import com.maomao.miniprogram.exception.BusinessException;
import com.maomao.miniprogram.model.dto.TalkCommentDeleteRequest;
import com.maomao.miniprogram.model.dto.TalkCommentQueryRequest;
import com.maomao.miniprogram.model.dto.TalkCommentSaveRequest;
import com.maomao.miniprogram.model.dto.TalkReplySaveRequest;
import com.maomao.miniprogram.model.vo.CommentVO;
import com.maomao.miniprogram.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author maomao
 * 2022/11/16 21:49
 */
@RestController
public class CommentController {

    @Resource
    CommentService commentService;

    @PostMapping("/talk/comment")
    public BaseResponse<Page<CommentVO>> getTalkComment(@RequestBody TalkCommentQueryRequest talkCommentQueryRequest){
        if(talkCommentQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = UserHolder.getUser();
        //分页获取单个说说评论
        Page<CommentVO> commentVOPage = commentService.getTalkComment(talkCommentQueryRequest,user.getId());

        return ResultUtils.success(commentVOPage);
    }

    @GetMapping("/talk/comment/thumb")
    public BaseResponse<Integer> talkCommentThumb(@RequestParam("commentId")Long commentId){
        if(commentId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Integer isThumb = commentService.doCommentThumb(commentId,UserHolder.getUser().getId());

        return ResultUtils.success(isThumb);
    }

    @PostMapping("/talk/comment/delete")
    public BaseResponse<Boolean> talkCommentDelete(@RequestBody TalkCommentDeleteRequest talkCommentDeleteRequest){
        if(!MyUtils.isNotNull(talkCommentDeleteRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Boolean isDelete = commentService.deleteComment(talkCommentDeleteRequest);

        return ResultUtils.success(isDelete);
    }

    /**
     * talk顶级评论
     * @param talkCommentSaveRequest
     * @return
     */
    @PostMapping("/talk/comment/save")
    public BaseResponse<Boolean> talkComment(@RequestBody TalkCommentSaveRequest talkCommentSaveRequest){
        if(talkCommentSaveRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long userId = UserHolder.getUser().getId();

        Boolean isSave = commentService.saveTopTalkComment(talkCommentSaveRequest,userId);

        return ResultUtils.success(isSave);
    }

    /**
     * talk顶级评论回复
     * @param talkReplySaveRequest
     * @return
     */
    @PostMapping("/talk/reply/save")
    public BaseResponse<Boolean> talkReply(@RequestBody TalkReplySaveRequest talkReplySaveRequest){
        if(talkReplySaveRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long userId = UserHolder.getUser().getId();

        Boolean isSave = commentService.saveTopTalkReply(talkReplySaveRequest,userId);

        return ResultUtils.success(isSave);
    }

}
