//package com.beyond.Team3.bonbon.chat.service;
//
//import com.theokanning.openai.completion.chat.ChatCompletionRequest;
//import com.theokanning.openai.completion.chat.ChatCompletionResult;
//import com.theokanning.openai.completion.chat.ChatMessage;
//import com.theokanning.openai.service.OpenAiService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AiChatService {
//    private final OpenAiService openAiService;
//    public AiChatService(OpenAiService openAiService) {
//        this.openAiService = openAiService;
//    }
//    public String askChatGpt(List<ChatMessage> chatMessages) {
//        String model = "gpt-3.5-turbo";
//        int maxTokens = 150;
//        double temperature = 1.0;
//        double topP = 1.0;
//        ChatCompletionRequest request = ChatCompletionRequest.builder()
//                .model(model)
//                .messages(chatMessages)
//                .maxTokens(maxTokens)
//                .temperature(temperature)
//                .topP(topP)
//                .build();
//        ChatCompletionResult result = openAiService.createChatCompletion(request);
//        return result.getChoices().get(0).getMessage().getContent().trim();
//    }
//}
