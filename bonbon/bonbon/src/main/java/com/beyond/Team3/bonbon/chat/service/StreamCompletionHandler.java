package com.beyond.Team3.bonbon.chat.service;

import com.beyond.Team3.bonbon.chat.dto.request.GPTCompletionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreamCompletionHandler extends TextWebSocketHandler {

    // Spring WebSocket에서 쓸 ConcurrentHashMap
    private final Map<String, WebSocketSession> sessionHashMap = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // OpenAiService 빈은 생성자로 자동 주입됨
    private final OpenAiService openAiService;

    /**
     * 클라이언트(WebSocket)가 연결될 때 호출
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionHashMap.put(session.getId(), session);
        log.info("WebSocket 연결됨: 세션ID={}", session.getId());
    }

    /**
     * 클라이언트(WebSocket)가 연결을 종료할 때 호출
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionHashMap.remove(session.getId());
        log.info("WebSocket 연결 종료: 세션ID={} 상태={}", session.getId(), status);
    }

    /**
     * 클라이언트로부터 텍스트 메시지가 도착했을 때 호출
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 1) JSON payload를 GPTCompletionRequest로 역직렬화
        GPTCompletionRequest completionRequest = objectMapper.readValue(
                message.getPayload(), GPTCompletionRequest.class);

        // 2) 모든 연결 세션에 대해 스트리밍 호출 수행
        sessionHashMap.keySet().forEach(key -> {
            streamCompletion(key, completionRequest);
        });
    }

    /**
     * WebSocket 세션 키(key)에 해당하는 클라이언트에게
     * OpenAI 스트리밍 응답을 보내는 내부 헬퍼 메서드
     */
    private void streamCompletion(String key, GPTCompletionRequest completionRequest) {
        // OpenAI Service의 blocking 스트리밍 API 호출
        openAiService.streamCompletion(GPTCompletionRequest.of(completionRequest))
                .blockingForEach(completion -> {
                    try {
                        // JSON 직렬화 후, 해당 세션에 TextMessage로 전송
                        String json = objectMapper.writeValueAsString(completion);
                        WebSocketSession ws = sessionHashMap.get(key);
                        if (ws != null && ws.isOpen()) {
                            ws.sendMessage(new TextMessage(json));
                        }
                    } catch (Exception e) {
                        log.error("WebSocket 전송 오류 (세션ID={})", key, e);
                    }
                });
    }
}
