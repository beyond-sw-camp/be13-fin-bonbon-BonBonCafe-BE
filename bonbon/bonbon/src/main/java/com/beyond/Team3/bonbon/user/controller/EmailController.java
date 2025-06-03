package com.beyond.Team3.bonbon.user.controller;


import com.beyond.Team3.bonbon.user.dto.EmailDto;
import com.beyond.Team3.bonbon.user.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bonbon/email")
public class EmailController {

    private final EmailService emailService;

//    // 인증코드 메일 발송
    @PostMapping("/send")
    public ResponseEntity<String> mailSend(@RequestBody EmailDto emailDto) throws MessagingException {
        log.info("EmailController.mailSend()");
        emailService.sendMessageToEmail(emailDto.getEmail());
        return ResponseEntity.ok("이메일 전송 완료!");
    }

    // 인증코드 인증
    @PostMapping("/verify")
    public Boolean verify(@RequestBody EmailDto emailDto) {
        log.info("EmailController.verify()");
        return emailService.verifyEmailCode(emailDto.getEmail(), emailDto.getEmailCode());
    }
}
