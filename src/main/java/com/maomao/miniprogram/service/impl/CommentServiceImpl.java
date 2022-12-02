package com.maomao.miniprogram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maomao.miniprogram.common.ErrorCode;
import com.maomao.miniprogram.common.Utils.MailSendUtil;
import com.maomao.miniprogram.common.Utils.UserHolder;
import com.maomao.miniprogram.config.MailSendConfig;
import com.maomao.miniprogram.entity.*;
import com.maomao.miniprogram.exception.BusinessException;
import com.maomao.miniprogram.mapper.UserMapper;
import com.maomao.miniprogram.model.dto.TalkCommentDeleteRequest;
import com.maomao.miniprogram.model.dto.TalkCommentQueryRequest;
import com.maomao.miniprogram.model.dto.TalkCommentSaveRequest;
import com.maomao.miniprogram.model.dto.TalkReplySaveRequest;
import com.maomao.miniprogram.model.vo.CommentVO;
import com.maomao.miniprogram.model.vo.UserVO;
import com.maomao.miniprogram.service.*;
import com.maomao.miniprogram.mapper.CommentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author maomao
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2022-11-15 16:04:29
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    TalkCommentService talkCommentService;
    @Resource
    UserService userService;
    @Resource
    CommentThumbService commentThumbService;
    @Resource
    TalkReplyService talkReplyService;
    @Resource
    TalkService talkService;
    @Resource
    UserMapper userMapper;
    @Resource
    MailSendConfig mailSendConfig;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Page<CommentVO> getTalkComment(TalkCommentQueryRequest talkCommentQueryRequest,Long userId) {
        Long talkId = talkCommentQueryRequest.getTalkId();
        Long pageNum = talkCommentQueryRequest.getPageNum();
        Long pageSize = talkCommentQueryRequest.getPageSize();

        //分页获取talk的顶级评论
        Page<TalkComment> talkCommentPage = new Page<>(pageNum, pageSize);
        QueryWrapper<TalkComment> talkCommentWrapper = new QueryWrapper<>();
        talkCommentWrapper.eq("talk_id",talkId);
        Page<TalkComment> page = talkCommentService.page(talkCommentPage, talkCommentWrapper);
        List<Long> talkCommentIdList = page.getRecords()
                .stream().map(TalkComment::getCommentId).collect(Collectors.toList());
        if(talkCommentIdList.size() == 0){
            return null;
        }
        List<Comment> commentList = this.listByIds(talkCommentIdList);

        //封装CommentVO,还需 userVO，isThumb，owner，replyCommentVOList
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment,commentVO);

            //封装userVO
            Long commentUserId = comment.getUserId();
            User user = userService.getById(commentUserId);
            commentVO.setUserId(user.getId());
            commentVO.setAvatar(user.getAvatar());
            commentVO.setNickname(user.getNickname());
            commentVO.setClientId(user.getClientId());

            //封装isThumb
            Long commentId = comment.getId();
            QueryWrapper<CommentThumb> commentThumbWrapper = new QueryWrapper<>();
            commentThumbWrapper.eq("user_id",userId);
            commentThumbWrapper.eq("comment_id",commentId);
            long count = commentThumbService.count(commentThumbWrapper);
            commentVO.setIsThumb(count > 0);

            //封装owner
            commentVO.setOwner(comment.getUserId().equals(userId));

            //封装replyCommentVOList
            QueryWrapper<TalkReply> talkReplyWrapper = new QueryWrapper<>();
            talkReplyWrapper.eq("root_comment_id", commentId);
            List<Long> replyIdList = talkReplyService.list(talkReplyWrapper)
                    .stream().map(TalkReply::getCommentId).collect(Collectors.toList());
            if (replyIdList.size() == 0){
                commentVOList.add(commentVO);
                continue;
            }

            //获取子评论
            List<Comment> replyList= this.listByIds(replyIdList);
            List<CommentVO> replyVOList = new ArrayList<>();
            for (Comment reply : replyList) {
                CommentVO replyVO = new CommentVO();
                BeanUtils.copyProperties(reply,replyVO);

                //封装userVO，isThumb，owner
                Long replyUserId = reply.getUserId();
                User replyUser = userService.getById(replyUserId);
                replyVO.setUserId(replyUser.getId());
                replyVO.setAvatar(replyUser.getAvatar());
                replyVO.setNickname(replyUser.getNickname());
                replyVO.setClientId(replyUser.getClientId());

                Long replyId = reply.getId();
                QueryWrapper<CommentThumb> commentThumbWrapper1 = new QueryWrapper<>();
                commentThumbWrapper1.eq("user_id",userId);
                commentThumbWrapper1.eq("comment_id",replyId);
                long replyCount = commentThumbService.count(commentThumbWrapper1);
                replyVO.setIsThumb(replyCount > 0);

                replyVO.setOwner(reply.getUserId().equals(userId));
                replyVOList.add(replyVO);
            }
            commentVO.setReplyCommentVOList(replyVOList);
            commentVOList.add(commentVO);
        }

        Page<CommentVO> commentVOPage = new Page<>(page.getCurrent(),page.getSize(),page.getTotal());
        commentVOPage.setRecords(commentVOList);

        return commentVOPage;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer doCommentThumb(Long commentId, Long userId) {
        //判断评论是否存在
        Comment comment = this.getById(commentId);
        if(comment == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        CommentThumb commentThumb = new CommentThumb();
        commentThumb.setUserId(userId);
        commentThumb.setCommentId(commentId);
        QueryWrapper<CommentThumb> commentThumbWrapper = new QueryWrapper<>(commentThumb);

        //给单个用户加锁
        synchronized (userId.toString().intern()){
            //判断是否点赞
            long count = commentThumbService.count(commentThumbWrapper);
            //已经点赞
            if(count > 0 ){
                //移除点赞记录
                boolean remove = commentThumbService.remove(commentThumbWrapper);
                if(remove){
                    //点赞数-1
                    this.update()
                            .eq("id",commentId)
                            .setSql("thumb = thumb - 1")
                            .update();
                    return -1;
                }else{
                    throw new BusinessException(ErrorCode.OPERATION_ERROR);
                }
            }else{
                //添加点赞记录
                boolean save = commentThumbService.save(commentThumb);
                if (save){
                    //点赞数+1
                    this.update()
                            .eq("id",commentId)
                            .setSql("thumb = thumb + 1")
                            .update();
                    return -1;
                }else{
                    throw new BusinessException(ErrorCode.OPERATION_ERROR);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean saveTopTalkComment(TalkCommentSaveRequest talkCommentSaveRequest, Long userId) {
        Long talkId = talkCommentSaveRequest.getTalkId();
        String content = talkCommentSaveRequest.getContent();
        Long parentId = talkCommentSaveRequest.getParentId();

        //构建comment，保存
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);
        boolean save = this.save(comment);

        if(save){
            //添加对应关系talkComment
            TalkComment talkComment = new TalkComment();
            talkComment.setCommentId(comment.getId());
            talkComment.setTalkId(talkId);
            talkComment.setCommentUserId(userId);
            talkCommentService.save(talkComment);

            //加锁，避免多加评论数
            synchronized (userId.toString().intern()){
                //更新评论数
                boolean id = talkService.update()
                        .eq("id", talkId)
                        .setSql("comment = comment + 1")
                        .update();
                //给当前评论的说说所有者发送邮件,除去自己
                UserVO talkUser = userMapper.getUserByTalkId(talkId);
                if(talkUser.getEmail() != null){
                    if(!talkUser.getId().equals(userId)){
                        MailSendUtil mailSendUtil = new MailSendUtil();
                        mailSendUtil.setRecipient(mailSendConfig.getRecipient());
                        mailSendUtil.setPassword(mailSendConfig.getPassword());
                        mailSendUtil.setTitle("新评论");
                        mailSendUtil.setFrom(UserHolder.getUser().getNickname());
                        mailSendUtil.setAddress(talkUser.getEmail());
                        mailSendUtil.setContent(content);
                        mailSendUtil.start();
                    }
                }
                return id;
            }
        }else{
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean saveTopTalkReply(TalkReplySaveRequest talkReplySaveRequest, Long userId) {
        String content = talkReplySaveRequest.getContent();
        Long rootCommentId = talkReplySaveRequest.getRootCommentId();
        Long parentId = talkReplySaveRequest.getParentId();
        Long talkId = talkReplySaveRequest.getTalkId();
        Long replyUserId = talkReplySaveRequest.getReplyUserId();

        //构建comment，保存
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setParentId(parentId);
        comment.setContent(content);
        boolean save = this.save(comment);

        if(save){
            //添加对应关系在talkReply
            TalkReply talkReply = new TalkReply();
            talkReply.setCommentId(comment.getId());
            talkReply.setRootCommentId(rootCommentId);
            talkReplyService.save(talkReply);
            //加锁，避免多加评论数
            synchronized (userId.toString().intern()){
                //更新评论数
                boolean id = talkService.update()
                        .eq("id", talkId)
                        .setSql("comment = comment + 1")
                        .update();
                //给回复的评论用户发送邮件,除去自己
                User user = userService.getById(replyUserId);
                if(user.getEmail() != null){
                    if(!user.getId().equals(userId)){
                        MailSendUtil mailSendUtil = new MailSendUtil();
                        mailSendUtil.setRecipient(mailSendConfig.getRecipient());
                        mailSendUtil.setPassword(mailSendConfig.getPassword());
                        mailSendUtil.setTitle("新评论");
                        mailSendUtil.setFrom(UserHolder.getUser().getNickname());
                        mailSendUtil.setAddress(user.getEmail());
                        mailSendUtil.setContent(content);
                        mailSendUtil.start();
                    }
                }
                return id;
            }

        }

        return false;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean deleteComment(TalkCommentDeleteRequest talkCommentDeleteRequest) {
        Long commentId = talkCommentDeleteRequest.getCommentId();
        Long talkId = talkCommentDeleteRequest.getTalkId();

        Comment comment = this.getById(commentId);

        Long parentId = comment.getParentId();

        boolean remove;
        if (parentId == null){
            //顶级评论，删除对应关系(本身对应关系，回复的对应关系)
            QueryWrapper<TalkComment> talkCommentQueryWrapper = new QueryWrapper<>();
            talkCommentQueryWrapper.eq("comment_id", commentId);
            QueryWrapper<TalkReply> talkReplyQueryWrapper = new QueryWrapper<>();
            talkReplyQueryWrapper.eq("root_comment_id", commentId);
            //获取一共删除的评论数
            long count = talkCommentService.count(talkCommentQueryWrapper) +
                    talkReplyService.count(talkReplyQueryWrapper);
            //移除对应关系
            talkReplyService.remove(talkReplyQueryWrapper);
            talkCommentService.remove(talkCommentQueryWrapper);
            //更新评论数
            remove = talkService.update()
                    .eq("id", talkId)
                    .setSql("comment = comment - " + count)
                    .update();

        }else{
            //回复，删除对应关系
            QueryWrapper<TalkReply> talkReplyQueryWrapper = new QueryWrapper<>();
            talkReplyQueryWrapper.eq("comment_id", commentId);
            //获取需要删除的评论数
            long count = talkReplyService.count(talkReplyQueryWrapper);
            talkReplyService.remove(talkReplyQueryWrapper);

            remove = talkService.update()
                    .eq("id", talkId)
                    .setSql("comment = comment - " + count)
                    .update();
        }

        //逻辑删除comment
        if(remove){
            return this.removeById(commentId);
        }

        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    }
}




