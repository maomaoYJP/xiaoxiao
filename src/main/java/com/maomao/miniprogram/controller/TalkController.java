package com.maomao.miniprogram.controller;

import com.maomao.miniprogram.common.BaseResponse;
import com.maomao.miniprogram.common.ErrorCode;
import com.maomao.miniprogram.common.Utils.MyUtils;
import com.maomao.miniprogram.common.Utils.ResultUtils;
import com.maomao.miniprogram.entity.Talk;
import com.maomao.miniprogram.exception.BusinessException;
import com.maomao.miniprogram.model.dto.TalkSaveDTO;
import com.maomao.miniprogram.service.TalkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author maomao
 * 2022/11/23 17:01
 */

@RestController
public class TalkController {

    @Resource
    TalkService talkService;

    /**
     * 创建talk
     * @param talkSaveDTO
     * @return
     */
    @PostMapping("/talk/save")
    public BaseResponse<Boolean> talkSave(@RequestBody TalkSaveDTO talkSaveDTO){
        if(!MyUtils.isNotNull(talkSaveDTO)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Boolean isSave = talkService.saveTalkAndPicture(talkSaveDTO);

        return ResultUtils.success(isSave);
    }

    /**
     * 删除talk，但是不删除其附带的评论和说说
     * @param talkId
     * @return
     */
    @GetMapping("/talk/delete")
    public BaseResponse<Boolean> talkDelete(@RequestParam("talkId")Long talkId){
        if(talkId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Boolean isDelete = talkService.removeById(talkId);

        return ResultUtils.success(isDelete);
    }


}
