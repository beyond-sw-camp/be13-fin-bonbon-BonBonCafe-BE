package com.beyond.Team3.bonbon.chat.service;

import com.beyond.Team3.bonbon.chat.dto.request.GPTCompletionChatRequest;
import com.beyond.Team3.bonbon.chat.dto.request.GPTCompletionRequest;
import com.beyond.Team3.bonbon.chat.dto.response.CompletionChatResponse;
import com.beyond.Team3.bonbon.chat.dto.response.CompletionResponse;
import com.beyond.Team3.bonbon.chat.dto.response.CompletionResponse.Message;
import com.beyond.Team3.bonbon.chat.entity.GPTAnswer;
import com.beyond.Team3.bonbon.chat.entity.GPTQuestion;
import com.beyond.Team3.bonbon.chat.repository.GPTAnswerRepository;
import com.beyond.Team3.bonbon.chat.repository.GPTQuestionRepository;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.dto.MenuRankingDto;
import com.beyond.Team3.bonbon.sales.service.SalesService;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.FranchiseeRepository;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.Usage;
import com.theokanning.openai.service.OpenAiService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GPTChatRestService {

    private final OpenAiService openAiService;
    private final UserRepository userRepository;
    private final GPTAnswerRepository answerRepository;
    private final GPTQuestionRepository questionRepository;
    private final FranchiseeRepository franchiseeRepository;
    private final SalesService salesService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CompletionChatResponse buildInitialGuideResponse(String model, Integer maxTokens) {
        // 가이드 텍스트 작성
        String guideText = """
            안녕하세요! 매출/메뉴 조회 챗봇입니다. 아래 예시를 참고하여 질문해주세요:

            1. 특정 일자의 매출을 조회하려면:
               매출 조회 YYYY-MM-DD
               예) 매출 조회 2025-06-01

            2. 기간별 매출을 조회하려면:
               기간별 매출 YYYY-MM-DD YYYY-MM-DD
               예) 기간별 매출 2025-06-01 2025-06-07

            3. 지난주 매출 조회는:
               지난주 매출 조회

            4. 특정 기간 동안 메뉴별 판매량을 조회하려면:
               메뉴 판매량 조회 YYYY-MM-DD YYYY-MM-DD
               예) 메뉴 판매량 조회 2025-06-01 2025-06-07

            5. 지난주 메뉴 판매량 조회는:
               지난주 메뉴 판매량

            • 날짜 형식은 반드시 'YYYY-MM-DD' 형태로 입력하세요.
         

            가이드를 확인하셨다면, 궁금한 내용을 입력해주세요!
            """;

        ChatCompletionResult fakeResult = new ChatCompletionResult();
        fakeResult.setId("manual-response");
        fakeResult.setObject("chat.completion");
        fakeResult.setCreated(System.currentTimeMillis() / 1000L);
        fakeResult.setModel(model);

        ChatCompletionChoice choice = new ChatCompletionChoice();
        ChatMessage assistantMsg = new ChatMessage("assistant", guideText);
        choice.setMessage(assistantMsg);
        choice.setIndex(0);
        choice.setFinishReason("stop");
        fakeResult.setChoices(List.of(choice));

        Usage fakeUsage = new Usage();
        fakeUsage.setPromptTokens(0L);
        fakeUsage.setCompletionTokens(0L);
        fakeUsage.setTotalTokens(0L);
        fakeResult.setUsage(fakeUsage);

        return CompletionChatResponse.of(fakeResult);
    }

    @Transactional
    public CompletionResponse completion(final GPTCompletionRequest restRequest) {
        CompletionResult result = openAiService.createCompletion(
                com.beyond.Team3.bonbon.chat.dto.request.GPTCompletionRequest.of(restRequest)
        );
        CompletionResponse response = CompletionResponse.of(result);

        List<String> messages = response.getMessages().stream()
                .map(Message::getText)
                .toList();
        GPTAnswer saved = saveAnswer(messages);
        saveQuestion(restRequest.getPrompt(), saved);

        return response;
    }

    @Transactional
    public CompletionChatResponse completionChat(
            Principal principal,
            GPTCompletionChatRequest chatRequest
    ) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        if (user.getUserType() == Role.FRANCHISEE) {
            return handleFranchiseeChat(principal, user, chatRequest);
        } else if (user.getUserType() == Role.MANAGER
                || user.getUserType() == Role.HEADQUARTER) {
            return handleHeadquarterChat(chatRequest);
        } else {
            throw new UserException(ExceptionMessage.USER_NOT_FOUND);
        }
    }

    // 메시지 패턴에 따라 매출/메뉴 조회
    private CompletionChatResponse handleFranchiseeChat(
            Principal principal,
            User user,
            GPTCompletionChatRequest chatRequest
    ) {

        Franchisee franchisee = franchiseeRepository.findByUserId(user)
                .orElseThrow(() -> new UserException(ExceptionMessage.FRANCHISE_NOT_FOUND));
        Long franchiseId = franchisee.getFranchise().getFranchiseId();

        // 사용자가 보낸 원문 메시지
        String userMessage = chatRequest.getMessage().trim();

        //  패턴 감지
        if (userMessage.contains("지난주 매출 조회")) {
            return handleLastWeekSales(franchiseId, chatRequest, userMessage);
        }
        if (userMessage.startsWith("매출 조회")) {
            return handleSingleDateSales(franchiseId, chatRequest, userMessage);
        }
        if (userMessage.startsWith("기간별 매출")) {
            return handlePeriodSales(franchiseId, chatRequest, userMessage);
        }
        if (userMessage.contains("지난주 메뉴 판매량")) {
            return handleLastWeekMenuSales(principal, franchiseId, chatRequest, userMessage);
        }

        if (userMessage.startsWith("메뉴 판매량 조회")) {
            return handlePeriodMenuSales(principal, franchiseId, chatRequest, userMessage);
        }

        // 위 조건 없으면 그냥 GPT가 대답
        String apology = """
            죄송합니다. 해당 가이드 질문에 맞게 다시 질문해 주세요.
            예를 들어, “매출 조회 YYYY-MM-DD” 또는 “기간별 매출 YYYY-MM-DD YYYY-MM-DD” 등의 형식으로 질문해 주십시오.
            """;
        return buildManualResponse(
                chatRequest.getModel(),
                chatRequest.getMaxTokens(),
                apology,
                userMessage
        );
    }


    private CompletionChatResponse handleHeadquarterChat(GPTCompletionChatRequest chatRequest) {
        return callOpenAIChat(chatRequest);
    }

    private CompletionChatResponse handleLastWeekSales(
            Long franchiseId,
            GPTCompletionChatRequest chatRequest,
            String originalMessage
    ) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate startOfWeek = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<DailySalesDto> list = salesService.getPeriodSales(
                franchiseId,
                startOfWeek,
                endOfWeek
        );

        String replyText;
        if (list.isEmpty()) {
            replyText = String.format(
                    "죄송합니다. 지난주(%s부터 %s까지) 매출 데이터가 없습니다.",
                    startOfWeek, endOfWeek
            );
        } else {
            long totalSum = list.stream()
                    .mapToLong(DailySalesDto::getTotalAmount)
                    .sum();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format(
                    "지난주(%s ~ %s) 총 매출은 %,d원입니다.%n",
                    startOfWeek, endOfWeek, totalSum
            ));
            sb.append("일별 매출 내역은 아래와 같습니다:\n");
            for (DailySalesDto d : list) {
                sb.append(String.format(
                        "- %s: %,d원%n",
                        d.getSalesDate(), d.getTotalAmount()
                ));
            }
            replyText = sb.toString();
        }
        return buildManualResponse(
                chatRequest.getModel(),
                chatRequest.getMaxTokens(),
                replyText,
                originalMessage
        );
    }

    private CompletionChatResponse handleSingleDateSales(
            Long franchiseId,
            GPTCompletionChatRequest chatRequest,
            String originalMessage
    ) {
        String[] tokens = originalMessage.split("\\s+");
        if (tokens.length == 3) {
            try {
                LocalDate date = LocalDate.parse(tokens[2]);
                DailySalesDto dto = salesService.getDailySales(franchiseId, date);
                String replyText = String.format(
                        "%s일 매출 총액은 %,d원입니다.",
                        dto.getSalesDate(), dto.getTotalAmount()
                );
                return buildManualResponse(
                        chatRequest.getModel(),
                        chatRequest.getMaxTokens(),
                        replyText,
                        originalMessage
                );
            } catch (DateTimeParseException e) {
                String err = "날짜 형식이 잘못되었습니다. 예시) 매출 조회 2025-06-01";
                return buildManualResponse(
                        chatRequest.getModel(),
                        chatRequest.getMaxTokens(),
                        err,
                        originalMessage
                );
            }
        }
        // 형식이 다르면 그냥 GPT 호출
        return callOpenAIChat(chatRequest);
    }

    private CompletionChatResponse handlePeriodSales(
            Long franchiseId,
            GPTCompletionChatRequest chatRequest,
            String originalMessage
    ) {
        String[] tokens = originalMessage.split("\\s+");
        if (tokens.length == 4) {
            try {
                LocalDate startDate = LocalDate.parse(tokens[2]);
                LocalDate endDate   = LocalDate.parse(tokens[3]);
                if (startDate.isAfter(endDate)) {
                    String err = "시작일이 종료일보다 이후일 수 없습니다. 예시) 기간별 매출 2025-06-01 2025-06-07";
                    return buildManualResponse(
                            chatRequest.getModel(),
                            chatRequest.getMaxTokens(),
                            err,
                            originalMessage
                    );
                }

                List<DailySalesDto> list = salesService.getPeriodSales(franchiseId, startDate, endDate);
                String replyText;
                if (list.isEmpty()) {
                    replyText = String.format(
                            "죄송합니다. %s부터 %s까지 기간에 매출 데이터가 없습니다.",
                            startDate, endDate
                    );
                } else {
                    long totalSum = list.stream()
                            .mapToLong(DailySalesDto::getTotalAmount)
                            .sum();
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format(
                            "%s부터 %s까지 총 매출은 %,d원입니다.%n",
                            startDate, endDate, totalSum
                    ));
                    sb.append("기간별 일별 매출 내역은 다음과 같습니다:\n");
                    for (DailySalesDto d : list) {
                        sb.append(String.format(
                                "- %s: %,d원%n",
                                d.getSalesDate(), d.getTotalAmount()
                        ));
                    }
                    replyText = sb.toString();
                }
                return buildManualResponse(
                        chatRequest.getModel(),
                        chatRequest.getMaxTokens(),
                        replyText,
                        originalMessage
                );
            } catch (DateTimeParseException e) {
                String err = "날짜 형식이 잘못되었습니다. 예시) 기간별 매출 2025-06-01 2025-06-07";
                return buildManualResponse(
                        chatRequest.getModel(),
                        chatRequest.getMaxTokens(),
                        err,
                        originalMessage
                );
            }
        }
        return callOpenAIChat(chatRequest);
    }

    // 메뉴 판매량 조회
    private CompletionChatResponse handleLastWeekMenuSales(
            Principal principal,
            Long franchiseId,
            GPTCompletionChatRequest chatRequest,
            String originalMessage
    ) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate startOfWeek = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // SalesService.getMenuSalesRanking(Principal, franchiseId, startDate, endDate)
        List<MenuRankingDto> ranking = salesService.getMenuSalesRanking(
                principal, franchiseId, startOfWeek, endOfWeek
        );

        String replyText;
        if (ranking.isEmpty()) {
            replyText = String.format(
                    "죄송합니다. 지난주(%s부터 %s까지) 메뉴 판매량 데이터가 없습니다.",
                    startOfWeek, endOfWeek
            );
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format(
                    "지난주(%s ~ %s) 메뉴별 판매량 상위 %d위입니다:\n",
                    startOfWeek, endOfWeek, ranking.size()
            ));
            int rank = 1;
            for (MenuRankingDto dto : ranking) {
                sb.append(String.format(
                        "%d. %s – %d개%n",
                        rank++,
                        dto.getMenuName(),
                        dto.getTotalAmount()
                ));
            }
            replyText = sb.toString();
        }
        return buildManualResponse(
                chatRequest.getModel(),
                chatRequest.getMaxTokens(),
                replyText,
                originalMessage
        );
    }

    private CompletionChatResponse handlePeriodMenuSales(
            Principal principal,
            Long franchiseId,
            GPTCompletionChatRequest chatRequest,
            String originalMessage
    ) {
        String[] tokens = originalMessage.split("\\s+");
        if (tokens.length == 4) {
            try {
                LocalDate startDate = LocalDate.parse(tokens[2]);
                LocalDate endDate   = LocalDate.parse(tokens[3]);
                if (startDate.isAfter(endDate)) {
                    String err = "시작일이 종료일보다 이후일 수 없습니다. 예시) 메뉴 판매량 조회 2025-06-01 2025-06-07";
                    return buildManualResponse(
                            chatRequest.getModel(),
                            chatRequest.getMaxTokens(),
                            err,
                            originalMessage
                    );
                }

                List<MenuRankingDto> ranking = salesService.getMenuSalesRanking(
                        principal, franchiseId, startDate, endDate
                );

                String replyText;
                if (ranking.isEmpty()) {
                    replyText = String.format(
                            "죄송합니다. %s부터 %s까지 기간에 메뉴 판매량 데이터가 없습니다.",
                            startDate, endDate
                    );
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format(
                            "%s부터 %s까지 메뉴별 판매량 상위 %d위입니다:\n",
                            startDate, endDate, ranking.size()
                    ));
                    int rank = 1;
                    for (MenuRankingDto dto : ranking) {
                        sb.append(String.format(
                                "%d. %s – %d개%n",
                                rank++,
                                dto.getMenuName(),
                                dto.getTotalAmount()
                        ));
                    }
                    replyText = sb.toString();
                }
                return buildManualResponse(
                        chatRequest.getModel(),
                        chatRequest.getMaxTokens(),
                        replyText,
                        originalMessage
                );
            } catch (DateTimeParseException e) {
                String err = "날짜 형식이 잘못되었습니다. 예시) 메뉴 판매량 조회 2025-06-01 2025-06-07";
                return buildManualResponse(
                        chatRequest.getModel(),
                        chatRequest.getMaxTokens(),
                        err,
                        originalMessage
                );
            }
        }
        return callOpenAIChat(chatRequest);
    }

    // GPT ChatCompletion 요청 헬퍼
    private CompletionChatResponse callOpenAIChat(GPTCompletionChatRequest chatRequest) {
        List<ChatMessage> messages = singletonList(
                new ChatMessage(chatRequest.getRole(), chatRequest.getMessage())
        );

        ChatCompletionRequest apiReq = ChatCompletionRequest.builder()
                .model(chatRequest.getModel())
                .messages(messages)
                .maxTokens(chatRequest.getMaxTokens())
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(apiReq);
        CompletionChatResponse response = CompletionChatResponse.of(result);

        // 답변 저장
        List<String> assistantMsgs = response.getMessages().stream()
                .map(CompletionChatResponse.Message::getMessage)
                .toList();
        GPTAnswer saved = saveAnswer(assistantMsgs);
        saveQuestion(chatRequest.getMessage(), saved);

        return response;
    }

    // 직접 만든 응답을 리턴할 때 사용하는 헬퍼
    private CompletionChatResponse buildManualResponse(
            String model,
            Integer maxTokens,
            String replyText,
            String originalUserMessage
    ) {
        ChatCompletionResult fakeResult = new ChatCompletionResult();
        fakeResult.setId("manual-response");
        fakeResult.setObject("chat.completion");
        fakeResult.setCreated(System.currentTimeMillis() / 1000L);
        fakeResult.setModel(model);

        ChatCompletionChoice choice = new ChatCompletionChoice();
        ChatMessage assistantMsg = new ChatMessage("assistant", replyText);
        choice.setMessage(assistantMsg);
        choice.setIndex(0);
        choice.setFinishReason("stop");
        fakeResult.setChoices(List.of(choice));

        Usage fakeUsage = new Usage();
        fakeUsage.setPromptTokens(0L);
        fakeUsage.setCompletionTokens(0L);
        fakeUsage.setTotalTokens(0L);
        fakeResult.setUsage(fakeUsage);

        CompletionChatResponse response = CompletionChatResponse.of(fakeResult);

        GPTAnswer saved = saveAnswer(List.of(replyText));
        saveQuestion(originalUserMessage, saved);

        return response;
    }

    // DB 저장
    private GPTAnswer saveAnswer(List<String> responses) {
        String combined = responses.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
        return answerRepository.save(new GPTAnswer(combined));
    }

    private void saveQuestion(String question, GPTAnswer answer) {
        GPTQuestion q = new GPTQuestion(question, answer);
        questionRepository.save(q);
    }
}
