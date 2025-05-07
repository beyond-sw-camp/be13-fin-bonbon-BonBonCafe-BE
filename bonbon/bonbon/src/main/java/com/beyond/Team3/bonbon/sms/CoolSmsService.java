package com.beyond.Team3.bonbon.sms;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoolSmsService {

    private final DefaultMessageService messageService;
    private final String fromNumber;

    public CoolSmsService(
            @Value("${coolsms.apikey}") String apiKey,
            @Value("${coolsms.apisecret}") String apiSecret,
            @Value("${coolsms.fromnumber}") String fromNumber
    ) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
        this.fromNumber = fromNumber;
    }

    public void sendBulkMessage(List<String> phoneNumbers, String content) {
        List<Message> messages = new ArrayList<>();
        for (String to : phoneNumbers) {
            Message message = new Message();
            message.setFrom(fromNumber);
            message.setTo(to);
            message.setText(content);
            messages.add(message);
        }

        try {
            this.messageService.send(messages, false, true);
        } catch (NurigoMessageNotReceivedException e) {
            System.out.println("실패한 메시지 목록: " + e.getFailedMessageList());
        } catch (Exception e) {
            System.out.println("에러: " + e.getMessage());
        }
    }
}