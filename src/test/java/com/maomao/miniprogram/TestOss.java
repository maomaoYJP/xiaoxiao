package com.maomao.miniprogram;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.maomao.miniprogram.config.OssConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author maomao
 * 2022/11/1 11:17
 */
@SpringBootTest
public class TestOss {

    @Resource
    OssConfig ossConfig;
    @Resource
    OSS ossClient;

    @Test
    void OssTest(){
        // 填写不包含Bucket名称在内的Object完整路径，例如exampleobject.txt。
        String objectName = "test/";
        boolean b = ossClient.doesObjectExist(ossConfig.getBucketName(), objectName);
        System.out.println(b);

    }

    @Test
    void dirTest() throws FileNotFoundException {
        String objectName = "test2/222";
        String content = "";
        boolean b = ossClient.doesObjectExist(ossConfig.getBucketName(), objectName);
        if(b){
            System.out.println("已经存在了");
        }else{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(ossConfig.getBucketName(), objectName, new ByteArrayInputStream(content.getBytes()));
            ossClient.putObject(putObjectRequest);
        }
    }

    @Test
    void utilTest(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String format = df.format(new Date());
        System.out.println(format);

    }

}
