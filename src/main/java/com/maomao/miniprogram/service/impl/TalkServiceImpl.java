package com.maomao.miniprogram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maomao.miniprogram.common.ErrorCode;
import com.maomao.miniprogram.common.Utils.UserHolder;
import com.maomao.miniprogram.config.MailSendConfig;
import com.maomao.miniprogram.entity.*;
import com.maomao.miniprogram.exception.BusinessException;
import com.maomao.miniprogram.model.dto.CardQueryRequest;
import com.maomao.miniprogram.model.dto.TalkSaveDTO;
import com.maomao.miniprogram.model.vo.IndexCardVO;
import com.maomao.miniprogram.model.vo.TalkVO;
import com.maomao.miniprogram.model.vo.UserVO;
import com.maomao.miniprogram.service.*;
import com.maomao.miniprogram.mapper.TalkMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author maomao
* @description 针对表【talk】的数据库操作Service实现
* @createDate 2022-11-15 16:04:29
*/
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk>
    implements TalkService{

    @Resource
    UserService userService;
    @Resource
    TalkThumbService talkThumbService;
    @Resource
    TalkPictureService talkPictureService;
    @Resource
    PictureService pictureService;
    @Resource
    TalkCommentService talkCommentService;
    @Resource
    MailSendConfig mailSendConfig;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Page<IndexCardVO> getTalkCard(CardQueryRequest cardQueryRequest, Long userId) {
        int pageNum = cardQueryRequest.getPageNum();
        int pageSize = cardQueryRequest.getPageSize();
        //分页获取talk
        Page<Talk> talkPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Talk> talkQueryWrapper = new QueryWrapper<>();
        talkQueryWrapper.orderByDesc("create_time");
        Page<Talk> page = this.page(talkPage, talkQueryWrapper);

        //一次性获取talk的用户
        List<Long> userIdList
                = page.getRecords().stream().map(Talk::getUserId).collect(Collectors.toList());
        if(userIdList.size() == 0){
            return null;
        }
        List<User> userList = userService.listByIds(userIdList);

        //将user封装成userVO
        Map<Long, UserVO> userVOMap = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toMap(UserVO::getId, userVO -> userVO));

        //判断用户是否已经点赞, 获取文章图片, 封装indexCardVO
        List<IndexCardVO> indexCardVOList = page.getRecords().stream().map(talk -> {
            IndexCardVO indexCardVO = new IndexCardVO();
            UserVO userVO = userVOMap.get(talk.getUserId());
            //判断当前用户是否点赞
            QueryWrapper<TalkThumb> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            wrapper.eq("talk_id", talk.getId());
            TalkThumb one = talkThumbService.getOne(wrapper);
            if (one != null) {
                indexCardVO.setIsThumb(true);
            } else {
                indexCardVO.setIsThumb(false);
            }
            //获取当前文章图片
            QueryWrapper<TalkPicture> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("talk_id", talk.getId()).last("limit 1");
            List<TalkPicture> list = talkPictureService.list(wrapper1);
            if(list.size() != 0){
                TalkPicture talkPicture = list.get(0);
                Long pictureId = talkPicture.getPictureId();
                //根据pictureId获取图片url
                Picture picture = pictureService.getById(pictureId);
                indexCardVO.setFirstPicture(picture.getUrl());
            }

            //封装indexCardVO
            indexCardVO.setId(talk.getId());
            indexCardVO.setTitle(talk.getTitle());
            indexCardVO.setUserVO(userVO);
            indexCardVO.setThumb(talk.getThumb());
            indexCardVO.setContent(talk.getContent());
            return indexCardVO;
        }).collect(Collectors.toList());

        //重新分页
        Page<IndexCardVO> indexCardVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        indexCardVOPage.setRecords(indexCardVOList);

        return indexCardVOPage;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer talkThumb(Long talkId, Long userId) {
        //判断说说是否存在
        Talk talk = this.getById(talkId);
        if(talk == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        TalkThumb talkThumb = new TalkThumb();
        talkThumb.setUserId(userId);
        talkThumb.setTalkId(talkId);
        QueryWrapper<TalkThumb> talkThumbQueryWrapper = new QueryWrapper<>(talkThumb);

        //给单个用户加锁
        synchronized (userId.toString().intern()){
            //判断是否已经点赞
            long count = talkThumbService.count(talkThumbQueryWrapper);
            //已经点赞
            if(count > 0){
                //移除talk_thumb中记录
                boolean remove = talkThumbService.remove(talkThumbQueryWrapper);
                if(remove){
                    //talk中点赞数-1
                    this.update()
                            .eq("id",talkId)
                            .setSql("thumb = thumb - 1")
                            .update();
                    return -1;
                }else{
                    throw new BusinessException(ErrorCode.OPERATION_ERROR);
                }
            }else{
                //没有点赞
                //添加点赞记录
                boolean save = talkThumbService.save(talkThumb);
                if(save){
                    //talk中点赞数+1
                    this.update()
                            .eq("id",talkId)
                            .setSql("thumb = thumb + 1")
                            .update();
                    return 1;
                }else{
                    throw new BusinessException(ErrorCode.OPERATION_ERROR);
                }
            }
        }
    }

    @Override
    public TalkVO getTalk(Long talkId) {
        //获取talk
        Talk talk = this.getById(talkId);

        //封装到talkVO
        TalkVO talkVO = new TalkVO();
        BeanUtils.copyProperties(talk,talkVO);

        //还需封装userVO，pictureList，commentNum, isThumb

        //封装userVO
        Long userId = talk.getUserId();
        User user = userService.getById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        talkVO.setUserVO(userVO);

        //封装isThumb
        QueryWrapper<TalkThumb> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", UserHolder.getUser().getId());
        wrapper.eq("talk_id", talk.getId());
        TalkThumb one = talkThumbService.getOne(wrapper);
        talkVO.setIsThumb(one != null);

        //封装pictureList
        QueryWrapper<TalkPicture> talkPictureWrapper = new QueryWrapper<>();
        talkPictureWrapper.eq("talk_id",talkId);
        List<Long> pictures = talkPictureService.list(talkPictureWrapper)
                .stream().map(TalkPicture::getPictureId).collect(Collectors.toList());
        if(pictures.size() == 0){
            talkVO.setPictureList(null);
        }else{
            List<String> pictureList = pictureService.listByIds(pictures)
                    .stream().map(Picture::getUrl).collect(Collectors.toList());
            talkVO.setPictureList(pictureList);
        }

        return talkVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveTalkAndPicture(TalkSaveDTO talkSaveDTO) {
        String title = talkSaveDTO.getTitle();
        String content = talkSaveDTO.getContent();
        String[] urls = talkSaveDTO.getUrls();
        Long userId = UserHolder.getUser().getId();

        //发送邮件，给关注我的所有人
        List<UserVO> userBeFollowed = userService.getUserBeFollowed(userId);
        userBeFollowed.forEach(userVO -> {
            if(userVO.getEmail() != null){
                mailSendConfig.setTitle("新说说：" + title);
                mailSendConfig.setFrom(UserHolder.getUser().getNickname());
                mailSendConfig.setAddress(userVO.getEmail());
                mailSendConfig.setContent(content);
                mailSendConfig.start();
            }
        });

        //没有图片
        if(urls == null || urls.length == 0){
            Talk talk = new Talk();
            talk.setUserId(UserHolder.getUser().getId());
            talk.setTitle(title);
            talk.setContent(content);
            return this.save(talk);
        }

        //有图片，先保存talk，再保存图片
        Talk talk = new Talk();
        talk.setUserId(UserHolder.getUser().getId());
        talk.setTitle(title);
        talk.setContent(content);
        this.save(talk);

        //保存图片url到picture表，并且设置talk_picture
        try {
            for (String url : urls) {
                Picture picture = new Picture();
                picture.setUrl(url);
                boolean save = pictureService.save(picture);
                if(save){
                    Long talkId = talk.getId();
                    TalkPicture talkPicture = new TalkPicture();
                    talkPicture.setPictureId(picture.getId());
                    talkPicture.setTalkId(talkId);
                    talkPictureService.save(talkPicture);
                }
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        return true;
    }
}




