package com.maomao.miniprogram.config;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @author maomao
 * 2022/10/14 17:09
 */
@Component
@Configuration
public class MailSendConfig extends Thread {

    /**
     * 发件人邮箱
     */
    @Value("${qq.recipient}")
    private String recipient;

    /**
     * 邮箱授权码
     */
    @Value("${qq.password}")
    private String password;

    public String getRecipient() {
        return recipient;
    }

    public String getPassword() {
        return password;
    }
}
