package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.config.RedisUtil;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Optional;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {


    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    private final long CODE_EXPIRE_TIME_MINUTES = 60 * 30L;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // 랜덤한 인증 문자 생성
    public String createNumber(){
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) { // 인증 코드 8자리
            int index = random.nextInt(3); // 0~2까지 랜덤, 랜덤값으로 switch문 실행

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97)); // 소문자
                case 1 -> key.append((char) (random.nextInt(26) + 65)); // 대문자
                case 2 -> key.append(random.nextInt(10)); // 숫자
            }
        }
        return key.toString();
    }

    // HTML 이메일 템플릿 생성
    private String buildEmailContent(String code) {
        return "<div style=\"max-width:400px; margin:auto; padding:40px 30px; background:white; border-radius:12px; text-align:center; box-shadow:0 4px 10px rgba(0,0,0,0.1);\">" +
                "<div style=\"font-size:22px; font-weight:bold; margin-bottom:20px;\">[BONBON] 인증 코드 메일입니다.</div>" +
                "<div style=\"font-size:16px; color:#555; margin-bottom:30px;\">아래 코드를 사이트에 입력해주십시오.</div>" +
                "<div style=\"font-size:24px; font-weight:bold; color:#d1004d;\">" + code + "</div>" +
                "<div style=\"font-size:14px; color:#999;\">인증 코드는 " + 30 + "분 후 만료됩니다.</div>" +
                "</div>";
    }

    // 이메일 폼 생성
    public MimeMessage createMail(String mail, String authCode) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("이메일 인증");
        message.setText(buildEmailContent(authCode), "UTF-8", "html");

        log.info("Saving to Redis: key = {}, value = {}", mail, authCode);
        // redis에 저장
        redisUtil.setDataExpire(mail, authCode, CODE_EXPIRE_TIME_MINUTES);
        log.info(redisUtil.getData(mail));

        return message;
    }

    // 메일 발송
    public String sendMessageToEmail(String sendEmail) throws MessagingException {

        // 이미 redis에 존재하는 이메일인 경우
        if (redisUtil.existData(sendEmail)) {
            redisUtil.deleteData(sendEmail);
        }

        String authCode = createNumber(); // 랜덤 인증번호 생성
        MimeMessage message = createMail(sendEmail, authCode); // 메일 생성

        try {
            mailSender.send(message); // 메일 발송
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
        }

        return authCode; // 생성된 인증번호 반환
    }

    // 코드 검증
    public Boolean verifyEmailCode(String email, String code) {

        String codeFoundByEmail = redisUtil.getData(email);
        log.info("code found by email: " + codeFoundByEmail);
        log.info("Code email : " + email);

        // 인증 실패! -> false
        if (codeFoundByEmail == null || !codeFoundByEmail.equals(code)) {
            log.info("code found by email: " + codeFoundByEmail);
            log.info("code found by email: " + code);
            return false;
        }

        // 인증 성공으로 플래그 설정 -> 인증을 완료했지만, 만료시간 안에 회원가입을 해야 함
        redisUtil.setDataExpire("email_verified:" + email, "true", CODE_EXPIRE_TIME_MINUTES);
        // redis에 저장한 인증 코드 삭제
        redisUtil.deleteData(email);
        return true;
    }

}
