package com.beyond.Team3.bonbon.config;

import com.querydsl.core.annotations.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    // SMTP 서버 호스트
    @Value("${spring.mail.host}")
    private String host;

    // SMTP 포트 번호
    @Value("${spring.mail.port}")
    private int port;

    // SMTP 로그인 아이디
    @Value("${spring.mail.username}")
    private String username;

    // 로그인 비밀번호
    @Value("${spring.mail.password}")
    private String password;

    // SMTP 인증 사용 여부
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;


    // STARTTLS 암호화 사용 여부
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    // STARTTLS 필수 여부
    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean starttlsRequired;

    // 연결 타임 아웃 시간
    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private int connectionTimeout;

    // 입출력 타임아웃 시간
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    // 쓰기 타임아웃 시간
    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private int writeTimeout;


    @Bean
    public JavaMailSender javaMailSender() {
        // SMTP 서버 정보와 인증 정보, 인코딩 등을 설정하고 반환
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // SMTP 서버 기본 정보 설정
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");

        // SMTP 세부 프로퍼티 설정
        mailSender.setJavaMailProperties(getMailProperties());

        return mailSender;
    }

    // SMTP 세부 프로퍼티 설정
    private Properties getMailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.starttls.required", starttlsRequired);
        props.put("mail.smtp.connectiontimeout", connectionTimeout);
        props.put("mail.smtp.timeout", timeout);
        props.put("mail.smtp.writetimeout", writeTimeout);

        return props;
    }
}
