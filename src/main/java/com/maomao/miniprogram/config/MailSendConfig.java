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
     * 标题
     */
    private String title;

    /**
     * 发送人信息
     */
    private String from;

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

    /**
     * 邮件发送的服务器
     */
    private String host = "smtp.qq.com";

    /**
     * 发送内容
     */
    private String content;

    /**
     * 收件人信息
     */
    private String address;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void run() {
        try {
            Properties properties = new Properties();

            properties.setProperty("mail.host", "smtp.qq.com");

            properties.setProperty("mail.transport.protocol", "smtp");

            properties.setProperty("mail.smtp.auth", "true");

            //QQ存在一个特性设置SSL加密
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            //创建一个session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(recipient, password);
                }
            });

            //开启debug模式
            session.setDebug(true);

            //获取连接对象
            Transport transport = null;
            try {
                transport = session.getTransport();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }

            //连接服务器
            transport.connect(host, from, password);

            //创建一个邮件发送对象
            MimeMessage mimeMessage = new MimeMessage(session);
            //邮件发送人
            mimeMessage.setFrom(new InternetAddress(recipient, from));

            //邮件接收人
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(address));

            //邮件标题
            mimeMessage.setSubject(title);

            //邮件内容
            mimeMessage.setContent(content, "text/html;charset=UTF-8");

            //发送邮件
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

            transport.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
