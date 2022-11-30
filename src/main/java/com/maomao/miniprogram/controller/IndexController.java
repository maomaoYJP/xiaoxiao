package com.maomao.miniprogram.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maomao.miniprogram.common.BaseResponse;
import com.maomao.miniprogram.common.ErrorCode;
import com.maomao.miniprogram.common.Utils.ResultUtils;
import com.maomao.miniprogram.common.Utils.UserHolder;
import com.maomao.miniprogram.entity.User;
import com.maomao.miniprogram.exception.BusinessException;
import com.maomao.miniprogram.model.dto.CardQueryRequest;
import com.maomao.miniprogram.model.vo.IndexCardVO;
import com.maomao.miniprogram.model.vo.TalkVO;
import com.maomao.miniprogram.service.TalkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author maomao
 * 2022/11/15 16:09
 */

@RestController
public class IndexController {

    @Resource
    TalkService talkService;

    @PostMapping("/index/page")
    public BaseResponse<Page<IndexCardVO>> indexTalkPage(@RequestBody CardQueryRequest cardQueryRequest){
        if(cardQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = UserHolder.getUser();
        //分页查询talk
        Page<IndexCardVO> indexCardVO = talkService.getTalkCard(cardQueryRequest, user.getId());

        return ResultUtils.success(indexCardVO);
    }

    @GetMapping("/index/thumb")
    public BaseResponse<Integer> indexTalkPageThumb(@RequestParam("talkId") Long talkId){
        if(talkId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //点赞或取消点赞
        Integer isThumb = talkService.talkThumb(talkId,UserHolder.getUser().getId());

        return ResultUtils.success(isThumb);
    }

    @GetMapping("/index/talk")
    public BaseResponse<TalkVO> getTalk(@RequestParam("talkId")Long talkId){
        if(talkId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //获取单个说说
        TalkVO talkVO = talkService.getTalk(talkId);

        return ResultUtils.success(talkVO);
    }

}
