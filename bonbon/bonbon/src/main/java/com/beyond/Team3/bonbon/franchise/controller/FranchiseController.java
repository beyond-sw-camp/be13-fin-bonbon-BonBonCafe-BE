package com.beyond.Team3.bonbon.franchise.controller;

import com.beyond.Team3.bonbon.franchise.dto.FranchiseLocationDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchisePageResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseRequestDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseUpdateRequestDto;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.dto.*;
import com.beyond.Team3.bonbon.franchise.service.FranchiseService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Tag(name = "Franchise", description = "가맹점 관리")
@Slf4j
@RestController
@RequestMapping("/franchise")
@RequiredArgsConstructor
public class FranchiseController {

//    @Value("${kakao.map.api.key}")
    private String kakaoApiKey;

    private final FranchiseService franchiseService;

    @GetMapping
    @Operation(summary = "모든 가맹점 조회", description = "본사와 계약한 모든 가맹점을 조회한다.")
    public ResponseEntity<FranchisePageResponseDto> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        FranchisePageResponseDto responseDto = franchiseService.findAll(page, size);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{franchiseId}")
    @Operation(summary = "가맹점 조회", description = "본사와 계약한 특정 가맹점을 조회한다.")
    public ResponseEntity<FranchiseResponseDto> findByFranchiseId(@PathVariable Long franchiseId){
        FranchiseResponseDto responseDto = franchiseService.findByFranchiseId(franchiseId);

        return ResponseEntity.ok(responseDto);

    }


    // 프렌차이즈 등록
    @PostMapping
    @Operation(summary = "가맹점 등록", description = "본사에서 매니저 역할을 가진 직원이 가맹점을 등록한다.")
    public ResponseEntity<FranchiseResponseDto> createFranchise(Principal principal, @RequestBody @Valid FranchiseRequestDto requestDto){
        franchiseService.createFranchise(principal, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{franciseId}")
    @Operation(summary = "가맹점 정보 수정", description = "가맹점 담당 매니저가 가맹점의 정보를 수정할 수 있다.")
    public ResponseEntity<FranchiseResponseDto> updateFranchiseInfo(@PathVariable Long franciseId, @RequestBody @Valid FranchiseUpdateRequestDto requestDto){
        franchiseService.updateFranchiseInfo(franciseId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }



//    @GetMapping("/test/{region}")
//    public void test(@PathVariable String region){
//
//
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + region;
//
//
//        // 요청 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                entity,
//                String.class
//        );
//
//        System.out.println(response.getBody());
//
//    }
    @GetMapping("/search")
    public String sarch(String query){
        Mono<String> mono = WebClient.builder().baseUrl("https://dapi.kakao.com")
                .build().get()
                .uri(builder -> builder.path("/v2/local/search/address.json")
                        .queryParam("query", query)
                        .build()
                )
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .exchangeToMono(response -> response.bodyToMono(String.class));
        return mono.block();
    }


    @GetMapping("/locations")
    public ResponseEntity<List<FranchiseLocationDto>> getLocations() {
        List<FranchiseLocationDto> locations = franchiseService.getFranchiseLocations();
        return ResponseEntity.ok(locations);
    }

}
