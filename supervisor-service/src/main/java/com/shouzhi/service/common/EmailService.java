package com.shouzhi.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;


/**
 * 邮件发送Service
 * @author WX
 * @date 2020-07-14 10:51:09
 */
@Service("emailService")
public class EmailService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.custom.send-from}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;


    /**
     * 发送简单文本邮件
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleTextMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(from);
        javaMailSender.send(message);
    }

    /**
     * 发送 HTML 邮件
     * @param to
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        // true 为 HTML 邮件
        messageHelper.setText(content, true);
        javaMailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param fileArr
     */
    public void sendAttachmentMail(String to, String subject, String content, String... fileArr)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);

        // 添加附件
        for (String filePath : fileArr) {
            FileSystemResource fileResource = new FileSystemResource(new File(filePath));
            if (fileResource.exists()) {
                String filename = fileResource.getFilename();
                messageHelper.addAttachment(filename, fileResource);
            }
        }
        javaMailSender.send(mimeMessage);
    }


    /**
     *  发送带图片的邮件
     * @param to
     * @param subject
     * @param content
     * @param imgMap
     * @throws MessagingException
     */
    public void sendImgMail(String to, String subject, String content, Map<String, String> imgMap)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        // 注意：在{@link #setText}之后调用{@code addInline}； 否则，邮件阅读器可能无法正确解析内联引用。
        // 添加图片
        for (Map.Entry<String, String> entry : imgMap.entrySet()) {
            FileSystemResource fileResource = new FileSystemResource(new File(entry.getValue()));
            if (fileResource.exists()) {
                String filename = fileResource.getFilename();
                messageHelper.addInline(entry.getKey(), fileResource);
            }
        }
        System.out.println();
        javaMailSender.send(mimeMessage);
    }

    /**
     * 发送模版邮件
     * @param to
     * @param subject
     * @param paramMap
     * @param template
     * @throws MessagingException
     */
    public void sendTemplateMail(String to, String subject, Map<String, Object> paramMap, String template)
            throws MessagingException {
        Context context = new Context();
        // 设置变量的值
        context.setVariables(paramMap);
        String emailContent = templateEngine.process(template, context);
        sendHtmlMail(to, subject, emailContent);
        logger.info("【模版邮件】成功发送！to={}，subject={}，paramsMap={}，template={}", to, subject, paramMap, template);
    }


    /**
     * 发送模版邮件带内联图片
     * @param to
     * @param subject
     * @param paramMap
     * @param template
     * @param imgMap
     * @throws MessagingException
     */
    public void sendTemplateMailInline(String to, String subject, Map<String, Object> paramMap, String template, Map<String, String> imgMap)
            throws MessagingException {
        Context context = new Context();
        // 设置变量的值
        context.setVariables(paramMap);
        String emailContent = templateEngine.process(template, context);
        sendImgMail(to,subject,emailContent,imgMap);
        logger.info("【图片模版邮件】成功发送！to={}，subject={}，paramsMap={}，template={}，imgMap={}", to, subject, paramMap, template, imgMap);
    }
}
