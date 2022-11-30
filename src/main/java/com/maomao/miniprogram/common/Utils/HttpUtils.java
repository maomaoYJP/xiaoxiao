package com.maomao.miniprogram.common.Utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


/**
 * @author maomao
 * 2022/11/10 18:02
 */
@Slf4j
public class HttpUtils {

    public static String sentGet(String url) {
        String res = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(5000)
                // 设置请求超时时间(单位毫秒)
                .setSocketTimeout(5000)
                // socket读写超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000)
                // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(false).build();
        // 将上面的配置信息 运用到这个Get请求里
        httpget.setConfig(requestConfig);
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpget);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            log.info("响应状态为：{}",response.getStatusLine());
            if (responseEntity != null) {
                res = EntityUtils.toString((org.apache.http.HttpEntity) responseEntity);
                log.info("响应内容为：{}",res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return res;
    }
}
