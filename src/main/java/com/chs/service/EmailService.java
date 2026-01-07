package com.chs.service;

public interface EmailService {
    void sendVerificationCode(String to, String code);
}
