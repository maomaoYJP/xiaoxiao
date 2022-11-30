package com.maomao.miniprogram.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maomao.miniprogram.entity.Talk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maomao.miniprogram.model.dto.CardQueryRequest;
import com.maomao.miniprogram.model.dto.TalkSaveDTO;
import com.maomao.miniprogram.model.vo.IndexCardVO;
import com.maomao.miniprogram.model.vo.TalkVO;

/**
* @author maomao
* @description 针对表【talk】的数据库操作Service
* @createDate 2022-11-15 16:04:29
*/
public interface TalkService extends IService<Talk> {

    /**
     * 获取说说卡片
     * @param cardQueryRequest 包含pageNum，pageSize
     * @param userId 当前用户id
     * @return
     */
    Page<IndexCardVO> getTalkCard(CardQueryRequest cardQueryRequest, Long userId);

    /**
     * talk点赞或取消点赞
     * @param talkId
     * @param userId
     * @return -1 取消点赞  1点赞
     */
    Integer talkThumb(Long talkId,Long userId);

    /**
     * 获取单个talk
     * @param talkId
     * @return talkVO
     */
    TalkVO getTalk(Long talkId);

    /**
     * 创建talk 并附带picture
     * @param talkSaveDTO
     * @return
     */
    Boolean saveTalkAndPicture(TalkSaveDTO talkSaveDTO);
}
