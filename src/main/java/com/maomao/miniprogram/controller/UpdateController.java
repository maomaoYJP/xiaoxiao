package com.maomao.miniprogram.controller;

import com.maomao.miniprogram.common.BaseResponse;
import com.maomao.miniprogram.common.Utils.ResultUtils;
import com.maomao.miniprogram.entity.UpdateLog;
import com.maomao.miniprogram.service.UpdateLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author maomao
 * 2022/12/1 22:45
 */
@RestController
public class UpdateController {

    @Resource
    UpdateLogService updateLogService;

    @GetMapping("update/log")
    public BaseResponse<String> getUpdateLog(){
        UpdateLog one = updateLogService.getOne(null);
        return ResultUtils.success(one.getContent());
    }
}
