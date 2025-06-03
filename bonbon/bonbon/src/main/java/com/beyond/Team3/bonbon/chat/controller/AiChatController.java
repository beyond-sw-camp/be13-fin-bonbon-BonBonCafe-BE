//package com.beyond.Team3.bonbon.chat.controller;
//import com.beyond.Team3.bonbon.chat.service.AiChatService;
//import com.theokanning.openai.completion.chat.ChatMessage;
//import lombok.Data;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/ai")
//public class AiChatController {
//    private final AiChatService aiChatService;
//    public AiChatController(AiChatService aiChatService) {
//        this.aiChatService = aiChatService;
//    }
//    @PostMapping("/chat")
//    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
//        List<ChatMessage> messages = request.getMessages();
//        String answer = aiChatService.askChatGpt(messages);
//        return ResponseEntity.ok(new ChatResponse(answer));
//    }
//    @Data
//    public static class ChatRequest {
//        private List<ChatMessage> messages;
//    }
//    @Data
//    public static class ChatResponse {
//        private final String answer;
//    }
//}
//
