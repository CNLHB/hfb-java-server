package com.ssk.hfb.service;

import com.ssk.hfb.common.enums.TemplateEnum;
import com.ssk.hfb.common.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "user:code:email:";
    private static final String SENDER = "1348844909@qq.com";
    private static final String SUBJECT = "[韩府帮] 邮箱绑定：";
    private static final String CONTENT = "你正在绑定邮箱！验证码为：";
    /**
     * 发送普通邮件
     *
     * @param to      收件人subject 主题
     * @param content 内容
     */
    public void sendSimpleMailMessge(String to, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER);
        message.setTo(to);
        message.setSubject(SUBJECT);
        String code = NumberUtils.generateCode(4);
        message.setText(CONTENT + code + " 。两分钟内有效！");
        try {
            mailSender.send(message);
            this.redisTemplate.opsForValue().set(KEY_PREFIX + to, code, 2, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("发送简单邮件时发生异常!", e);
        }
    }

    /**
     * 发送 HTML 邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendMimeMessge(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SENDER);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送MimeMessge时发生异常！", e);
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件路径
     */
    public void sendMimeMessge(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SENDER);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送带附件的MimeMessge时发生异常！", e);
        }
    }

    /**
     * 发送带静态文件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param rscIdMap 需要替换的静态文件
     */
    public void sendMimeMessge(String to, String subject, String content, Map<String, String> rscIdMap) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SENDER);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            for (Map.Entry<String, String> entry : rscIdMap.entrySet()) {
                FileSystemResource file = new FileSystemResource(new File(entry.getValue()));
                helper.addInline(entry.getKey(), file);
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送带静态文件的MimeMessge时发生异常！", e);
        }
    }
}