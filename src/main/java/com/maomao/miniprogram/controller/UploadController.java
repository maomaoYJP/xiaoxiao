package com.maomao.miniprogram.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.maomao.miniprogram.common.BaseResponse;
import com.maomao.miniprogram.common.Utils.ResultUtils;
import com.maomao.miniprogram.config.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author maomao
 * 2022/11/24 14:28
 */

@RestController
@Slf4j
public class UploadController {

    @Resource
    OssConfig ossConfig;
    @Resource
    OSS ossClient;

    @PostMapping("upload/image")
    public BaseResponse<String> uploadImage(@RequestBody MultipartFile file){
        String fileName = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //目录名：当前日期 2022-11-24
        String dirName = df.format(new Date());
        try {
            //文件名，时间戳 + UUID
            fileName = System.currentTimeMillis() + UUID.randomUUID().toString();
            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType("image/jpg");
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            fileName = dirName + "/" + fileName;
            // 上传文件
            ossClient.putObject(ossConfig.getBucketName(), fileName, inputStream, objectMetadata);
        } catch (IOException e) {
            log.error("Error occurred: {}", e.getMessage(), e);
        }
        //拼接url地址
        String url = "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + fileName;
        return ResultUtils.success(url);
    }
}
