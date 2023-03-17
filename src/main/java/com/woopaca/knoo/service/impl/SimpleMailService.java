package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.exception.mail.MessageCreationException;
import com.woopaca.knoo.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleMailService implements MailService {

    private final JavaMailSender mailSender;

    private static final String HOST = "http://localhost:8888";

    @Override
    public void sendAuthMail(final String receivedUserMail, final String uuid) {
        log.info("인증 메일 발송 [{}]", receivedUserMail);

        MimeMessage message = createMessage(receivedUserMail, uuid);
        mailSender.send(message);
    }

    private MimeMessage createMessage(final String receivedUserMail, final String uuid) {
        MimeMessage message = mailSender.createMimeMessage();
        String messageContent = createMessageContent(uuid);

        try {
            message.addRecipients(Message.RecipientType.TO, receivedUserMail);
            message.setSubject("KNOO 회원가입 이메일 인증");
            message.setText(messageContent, StandardCharsets.UTF_8.name(), "HTML");
            message.setFrom("KNOO");
        } catch (MessagingException e) {
            throw new MessageCreationException();
        }
        return message;
    }

    private String createMessageContent(String uuid) {
        String url = HOST + "/auth/mail?code=" + uuid;
        return "<div>" +
                "<h3>[KNOO] 아래 URL을 통해 회원가입을 완료하세요.<h3><br>" +
                "<a href=\"" + url + "\">KNOO 이메일 인증하기</a>" +
                "</div>";
    }
}
