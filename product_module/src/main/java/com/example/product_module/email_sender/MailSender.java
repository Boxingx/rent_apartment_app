package com.example.product_module.email_sender;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    public void sendEmail(String sendSubject, String text, String sendTo) {

        logger.info("Класс MailSender метод sendEmail начал выполнять отправку письма, тема: {}, получатель: {}", sendSubject, sendTo);
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("barik.alex95@yandex.ru");
            simpleMailMessage.setTo(sendTo);
            simpleMailMessage.setSubject(sendSubject);
            simpleMailMessage.setText(text);

            javaMailSender.send(simpleMailMessage);
            logger.info("Класс MailSender метод sendEmail успешно выполнил отправку письма, тема: {}, получатель: {}", sendSubject, sendTo);
        } catch (Exception e ) {
            e.printStackTrace();
            logger.error("Класс MailSender метод sendEmail получил ошибку при отправке письма, тема: {}, получатель: {}", sendSubject, sendTo);
        }
    }

    public void sendEmailWithImages(String sendSubject, String text, String sendTo, List<byte[]> imageBytes) {

        logger.info("Класс MailSender метод sendEmailWithImages начал выполнять отправку письма с изображениями, тема: {}, получатель: {}", sendSubject, sendTo);
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
            logger.info("Класс MailSender метод sendEmailWithImages успешно выполнил отправку письма с изображениями, тема: {}, получатель: {}", sendSubject, sendTo);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Класс MailSender метод sendEmailWithImages получил ошибку при отправке письма с изображениями, тема: {}, получатель: {}",
                    sendSubject, sendTo);
        }
    }


    public void sendEmailWithImage(String sendSubject, String text, String sendTo, byte[] imageBytes) {

        logger.info("Класс MailSender метод sendEmailWithImage начал выполнять отправку письма с изображением, тема: {}, получатель: {}", sendSubject, sendTo);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("barik.alex95@yandex.ru");
            helper.setTo(sendTo);
            helper.setSubject(sendSubject);
            helper.setText(text);
            helper.addAttachment("example", new ByteArrayResource(imageBytes));

            javaMailSender.send(mimeMessage);
            logger.info("Класс MailSender метод sendEmailWithImage успешно выполнил отправку письма с изображением, тема: {}, получатель: {}", sendSubject, sendTo);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Класс MailSender метод sendEmailWithImage получил ошибку при отправке письма с изображением, тема: {}, получатель: {}", sendSubject, sendTo);
        }
    }
}


