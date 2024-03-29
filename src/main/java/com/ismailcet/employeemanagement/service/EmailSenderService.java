package com.ismailcet.employeemanagement.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String toEmail,
                          String subject,
                          String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ismailcet3@gmail.com");
        message.setTo(toEmail);
        message.setText(subject);
        message.setSubject(body);

        javaMailSender.send(message);

        System.out.println("Mail send Successfully  ! ");
    }

}
