package com.maomao.miniprogram;

import com.maomao.miniprogram.common.Utils.HttpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.maomao.miniprogram.constant.WXConstant.WX_APP_ID;
import static com.maomao.miniprogram.constant.WXConstant.WX_APP_SECRET;


/**
 * @author maomao
 * 2022/11/1 11:17
 */
@SpringBootTest
public class TestUtils {

    @Test
    void getComment(){
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=" + WX_APP_ID;
        url += "&secret=" + WX_APP_SECRET;
        url += "&js_code=" + "053owg1w3UP7xZ2k8u3w3bfUjQ0owg1W";
        url += "&grant_type=authorization_code";
        url += "&connect_redirect=1";
        String s = HttpUtils.sentGet(url);
    }

    @Test
    void test(){

    }
}
