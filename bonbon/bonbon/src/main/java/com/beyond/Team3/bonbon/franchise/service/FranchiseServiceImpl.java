
package com.beyond.Team3.bonbon.franchise.service;


import com.beyond.Team3.bonbon.franchise.controller.FranchiseSummaryDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseLocationDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchisePageResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseRequestDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseUpdateRequestDto;
import com.beyond.Team3.bonbon.franchise.dto.*;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.handler.exception.FranchiseException;
import com.beyond.Team3.bonbon.handler.exception.PageException;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.region.entity.Region;
import com.beyond.Team3.bonbon.region.repository.RegionRepository;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import com.beyond.Team3.bonbon.user.repository.ManagerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final HeadquarterRepository headquarterRepository;
    private final WebClient.Builder webClientBuilder;
    private final ManagerRepository managerRepository;

    @Value("${kakao.map.api.key}")
    private String kakaoApiKey;


    @Override
    public FranchisePageResponseDto findAll(int page, int size) {

        if (page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Franchise> franchisePage = franchiseRepository.findAll(pageable);

        List<FranchiseResponseDto> responseDto = franchisePage.stream().map(FranchiseResponseDto::new).toList();
        FranchisePageResponseDto pageResponseDto = new FranchisePageResponseDto(responseDto, franchisePage.getTotalElements());

        if (franchisePage.isEmpty()){
            throw new IllegalArgumentException("franchise is empty");
        }
        return pageResponseDto;

    }

    @Override
    public FranchiseResponseDto findByFranchiseId(Long franchiseId) {

        Optional<Franchise> franchise = franchiseRepository.findById(franchiseId);

        if (franchise.isEmpty()){
            throw new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND);
        }

        log.info("Franchise found: " + franchise.get().getFranchiseId());


        return new FranchiseResponseDto(franchise.get());
    }

    @Override
    @Transactional
    public void createFranchise(Principal principal, FranchiseRequestDto requestDto) {

        // 로그인 유저 본사Id 가져오기
        String email = principal.getName();
        User headquerterUser = userRepository.findByEmail(email).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
        Headquarter headquarter = headquarterRepository.findByHeadquarterId(headquerterUser.getHeadquarterId().getHeadquarterId());

        // 지역 코드 확인 -> message 추가 예정
        Region regionCode = regionRepository.findByRegionCode(requestDto.getRegionCode());
        Franchise franchise = requestDto.toEntity(headquarter, regionCode);
        franchiseRepository.save(franchise);
    }

    @Override
    @Transactional
    public void updateFranchiseInfo(Long franchiseId, FranchiseUpdateRequestDto requestDto) {
        // 해당 프랜차이즈 존재여부 확인
        Optional<Franchise> optionalFranchise = franchiseRepository.findByFranchiseId(franchiseId);
        if(optionalFranchise.isEmpty()){
            throw new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND);
        }

        // 담당자 일치??매니저??
        // 가맹 주점은 권한 없음..??

        Franchise franchise = optionalFranchise.get();
        franchise.update(requestDto);
        franchiseRepository.save(franchise);
    }

    @Override
    public List<FranchiseLocationDto> getFranchiseLocations() {
        WebClient webClient = webClientBuilder
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK " + kakaoApiKey)
                .build();

        List<Franchise> franchises = franchiseRepository.findAll();

        return franchises.stream()
                .map(franchise -> {
                    String address = franchise.getRoadAddress();
                    String name = franchise.getName();

                    String response = webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/v2/local/search/address.json")
                                    .queryParam("query", address)
                                    .build())
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);
                        JsonNode documents = root.path("documents");

                        if (!documents.isEmpty()) {
                            JsonNode first = documents.get(0);
                            double lat = first.path("y").asDouble();
                            double lng = first.path("x").asDouble();

                            return new FranchiseLocationDto(name, lat, lng);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public FranchiseSummaryDto findByFranchiseNam(String name) {
        // franchiseId, franchiseTel
        Franchise franchise = franchiseRepository.findByName(name);
        log.info("Franchise found: " + franchise.getFranchiseId());
        log.info("Franchise found: " + franchise.getFranchiseTel());
        
        // 지역 코드로 담당자 찾기
        Region region = franchise.getRegionCode();

        Manager manager = managerRepository.findByRegionCode(region);
        
        // 담당자의 유저 아이디로 이름, 전화번호 찾기
        User managerInfo = userRepository.findByUserId(manager.getUserId().getUserId());

        log.info("User found: " + managerInfo.getName());
        log.info("User found: " + managerInfo.getPhone());

        return new FranchiseSummaryDto(franchise.getFranchiseId(), franchise.getFranchiseTel(), managerInfo.getName(), managerInfo.getPhone());
    }

}
