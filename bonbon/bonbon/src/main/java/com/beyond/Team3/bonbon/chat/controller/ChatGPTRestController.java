package com.beyond.Team3.bonbon.chat.controller;

import com.beyond.Team3.bonbon.chat.dto.request.GPTCompletionChatRequest;
import com.beyond.Team3.bonbon.chat.dto.request.GPTCompletionRequest;
import com.beyond.Team3.bonbon.chat.dto.response.CompletionChatResponse;
import com.beyond.Team3.bonbon.chat.dto.response.CompletionResponse;
import com.beyond.Team3.bonbon.chat.service.GPTChatRestService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/chatgpt/rest")
@RequiredArgsConstructor
@Tag(name = "챗봇", description = "챗봇 관련 기능")
public class ChatGPTRestController {

    private final GPTChatRestService gptChatRestService;

    @PostMapping("/completion")
    public CompletionResponse completion(
            @RequestBody GPTCompletionRequest gptCompletionRequest) {
        return gptChatRestService.completion(gptCompletionRequest);
    }

    @PostMapping("/completion/chat")
    public ResponseEntity<CompletionChatResponse> completionChat(
            Principal principal,
            @RequestBody GPTCompletionChatRequest gptCompletionChatRequest) {

        // 첫 채팅 시 가이드 라인 제시
        if (gptCompletionChatRequest.getConversation() == null
                || gptCompletionChatRequest.getConversation().isEmpty()) {
            CompletionChatResponse guide = gptChatRestService.buildInitialGuideResponse(
                    gptCompletionChatRequest.getModel(),
                    gptCompletionChatRequest.getMaxTokens()
            );
            return ResponseEntity.ok(guide);
        }

        CompletionChatResponse resp = gptChatRestService.completionChat(
                principal,
                gptCompletionChatRequest
        );
        return ResponseEntity.ok(resp);
    }

}