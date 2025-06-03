package com.beyond.Team3.bonbon.franchise.controller;

import com.beyond.Team3.bonbon.franchise.dto.FranchisePageResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseRequestDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseUpdateRequestDto;
import com.beyond.Team3.bonbon.franchise.dto.*;
import com.beyond.Team3.bonbon.franchise.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;


@Tag(name = "가맹점", description = "가맹점 등록, 조회, 수정 및 위치 정보 등을 관리하는 API입니다.")
@Slf4j
@RestController
@RequestMapping("/franchise")
@RequiredArgsConstructor
public class FranchiseController {


    private final FranchiseService franchiseService;


    // 권한이 없으면 영업중인 가맹점만 조회 가능 매니저(모든 매니저)나 본사는 모든 가맹점 조회 가능
    @GetMapping
    @Operation(summary = "모든 가맹점 조회", description = "본사와 계약된 모든 가맹점 목록을 페이지 단위로 조회합니다.")
    public ResponseEntity<Page<FranchiseResponseDto>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "region", required = false) String region,     // 예: 서울특별시
            @RequestParam(name = "district", required = false) String district  // 예: 강남구
    ){
        Page<FranchiseResponseDto> responseDto = franchiseService.findAll(page, size, region, district);

        return ResponseEntity.ok(responseDto);
    }


    // 매니저나(모든 매니저) 본사는 상세조회 가능
    @GetMapping("/{franchiseId}")
    @Operation(summary = "가맹점 조회", description = "지정된 ID에 해당하는 가맹점의 상세 정보를 조회합니다.")
    public ResponseEntity<FranchiseResponseDto> findByFranchiseId(@PathVariable Long franchiseId){
        FranchiseResponseDto responseDto = franchiseService.findByFranchiseId(franchiseId);

        return ResponseEntity.ok(responseDto);

    }


    // 모든 권한 횽
    @GetMapping("/summary/{franchiseId}")
    @Operation(
            summary = "가맹점 요약 조회",
            description = "가맹점 이름을 기준으로 해당 가맹점의 요약 정보를 조회합니다."
    )
    public ResponseEntity<FranchiseSummaryDto> findByFranchiseName(@PathVariable Long franchiseId){
        FranchiseSummaryDto summaryDto = franchiseService.franchiseSummary(franchiseId);
        return ResponseEntity.ok(summaryDto);
    }


    // 해당 지역 매니저나 본사는 프렌차이즈 등록을 할 수 있음
    @PostMapping
    @Operation(summary = "가맹점 등록", description = "본사 소속 직원 중 가맹점 관리를 담당하는 사용자가 가맹점을 등록합니다."
                                                    + "사용자는 가맹점 이름, 전화번호, 주소, 개업일자, 사진, 평수, 좌석 수, 주차 가능 여부, 운영 상태, 운영 시간을 입력해야 합니다.")
    public ResponseEntity<FranchiseResponseDto> createFranchise(Principal principal, @RequestBody @Valid FranchiseRequestDto requestDto){
        franchiseService.createFranchise(principal, requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    // 해당 지역 매니저나 본사는 프렌차이즈 수정할 수 있음
    @PatchMapping("/{franciseId}")
    @Operation(summary = "가맹점 정보 수정", description = "가맹점 관리자는 해당 가맹점의 연락처, 사진, 평수, 좌석 수, 주차 가능 여부, 오픈 시간만 수정할 수 있음")
    public ResponseEntity<FranchiseResponseDto> updateFranchiseInfo(@PathVariable Long franciseId, @RequestBody FranchiseUpdateRequestDto requestDto, Principal principal){
        franchiseService.updateFranchiseInfo(franciseId, requestDto, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 해당 지역 매니저나 본사는 가맹점을 상태가 운영중인 것들을 임시 휴점으로 변경후?? 영구 폐점으로 변경
    @DeleteMapping("/{franchiseId}")
    @Operation(summary = "가맹점 삭제", description = "가맹전 관리자는 해당 가맹점을 삭제할 수 있음")
    public ResponseEntity<Void> deleteFranchise(@PathVariable Long franchiseId, Principal principal){
        franchiseService.deleteFranchise(franchiseId, principal);
        return ResponseEntity.ok().build();
    }

    // 모든 가맹점 위치 DB에서 조회 (모든 궘한)
    @GetMapping("/locations")
    @Operation(
            summary = "가맹점 위치 조회",
            description = "지도에 표시하기 위해 모든 가맹점의 위치 정보를 조회합니다. 위치 정보는 위도와 경도를 포함합니다."
    )
    public ResponseEntity<List<LocationResponseDto>> getLocation() {
        List<LocationResponseDto> responseDto = franchiseService.findAllLocation();
        return ResponseEntity.ok(responseDto);
    }



    // 더미
    @GetMapping("/test/")
    @Operation(
            summary = "franchiseLocation 더미를 위한 API",
            description = "등록된 가맹점들의 위도, 경도가 없을 경우 모든 가맹점들의 위도, 경도를 찾아서 저장"
    )
    public ResponseEntity<List<LocationResponseDto>> getLocationsTest() {
        List<LocationResponseDto> responseDto = franchiseService.getLocationsTest();
        return ResponseEntity.ok(responseDto);
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
//    @GetMapping("/search")
//    public String sarch(String query){
//        Mono<String> mono = WebClient.builder().baseUrl("https://dapi.kakao.com")
//                .build().get()
//                .uri(builder -> builder.path("/v2/local/search/address.json")
//                        .queryParam("query", query)
//                        .build()
//                )
//                .header("Authorization", "KakaoAK " + kakaoApiKey)
//                .exchangeToMono(response -> response.bodyToMono(String.class));
//        return mono.block();
//    }




}
