package com.chs.service.impl;

import com.chs.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1324110763@qq.com");
        message.setTo(to);
        message.setSubject("Chs_memes - 验证码");
        message.setText("您的验证码是: " + code + "\n该验证码将在10分钟后过期。");
        mailSender.send(message);
    }
}