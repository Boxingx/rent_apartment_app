package com.example.product_module.email_sender;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

//            String htmlText = "<html><body>" +
//                    "<p>" + text + "</p>" +
//                    "<p>Ваша квартира:</p>" +
//                    "<img src='cid:example0'><br>" +
//                    "<img src='cid:example1'><br>" +
//                    "<img src='cid:example2'><br>" +
//                    "<img src='cid:example3'><br>" +
//                    "<img src='cid:example4'><br>" +
//                    "</body></html>";

            helper.setText(text);

//            helper.addInline("example0", new ClassPathResource(new String(imageBytes.get(0))));
//            helper.addInline("example1", new ClassPathResource(new String(imageBytes.get(1))));
            int i = 0;
            // Прикрепляем изображение
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

            // Прикрепляем изображение
            helper.addAttachment("example", new ByteArrayResource(imageBytes));

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}


