package com.example.product_module.email_sender;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String sendSubject, String text, String sendTo) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("barik.alex95@yandex.ru");
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setSubject(sendSubject);
        simpleMailMessage.setText(text);

        javaMailSender.send(simpleMailMessage);
    }

}
