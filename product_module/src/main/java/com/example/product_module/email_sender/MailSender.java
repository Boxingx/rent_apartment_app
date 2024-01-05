package com.example.product_module.email_sender;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void sendEmailWithImages(String sendSubject, String text, String sendTo, List<byte[]> imageBytes) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("barik.alex95@yandex.ru");
            helper.setTo(sendTo);
            helper.setSubject(sendSubject);
            helper.setText(text);

            int i = 0;
            for (byte[] p : imageBytes) {
                helper.addAttachment("example" + i + ".jpg", new ByteArrayResource(p));
                i++;
            }
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public void sendEmailWithImage(String sendSubject, String text, String sendTo, byte[] imageBytes) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("barik.alex95@yandex.ru");
            helper.setTo(sendTo);
            helper.setSubject(sendSubject);
            helper.setText(text);
            helper.addAttachment("example", new ByteArrayResource(imageBytes));

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}


