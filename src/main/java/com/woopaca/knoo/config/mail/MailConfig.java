package com.woopaca.knoo.config.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.yml")
public class MailConfig {

    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.smtp.socket_factory.port}")
    private int socketPort;
    @Value("${mail.smtp.socket_factory.fallback}")
    private boolean fallback;
    @Value("${mail.smtp.auth}")
    private boolean auth;
    @Value("${mail.smtp.start_tls.enable}")
    private boolean startTls;
    @Value("${mail.smtp.start_tls.required}")
    private boolean startTlsRequired;
    @Value("${admin_mail.id}")
    private String id;
    @Value("${admin_mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername(id);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.socket_factory.port", socketPort);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.start_tls.enable", startTls);
        properties.put("mail.smtp.start_tls.required", startTlsRequired);
        properties.put("mail.smtp.socket_factory.fallback", fallback);
        properties.put("mail.smtp.socket_factory.class", "javax.net.ssl.SSLSocketFactory");
        return properties;
    }
}
