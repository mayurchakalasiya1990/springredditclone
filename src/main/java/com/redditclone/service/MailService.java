package com.redditclone.service;

import com.redditclone.exception.SpringRedditException;
import com.redditclone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j //from lombok lib for log object
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder contentBuilder;


    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator mimeMessagePreparator=mimeMessage -> {
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("springreddit@gmail.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(contentBuilder.build(notificationEmail.getBody()));
        };
        try {
                mailSender.send(mimeMessagePreparator);
                log.info("Activation email send !!!");
        }catch (MailException e){
                e.printStackTrace();
                throw new SpringRedditException("Exception occurred when sending email to "+notificationEmail.getRecipient());
        }
    }


}
