
package com.beyond.Team3.bonbon.franchise.service;


import com.beyond.Team3.bonbon.common.enums.RegionName;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseSummaryDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseLocationDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchisePageResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseRequestDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseUpdateRequestDto;
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

    private Manager getManagerFromPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
        return managerRepository.findByUserId(user)
                .orElseThrow(() -> new FranchiseException(ExceptionMessage.MANAGER_NOT_FOUND));
    };

    private Franchise getFranchiseId(Long franchiseId) {
        return franchiseRepository.findByFranchiseId(franchiseId)
                .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));
    }

    private void authorizeManagerRegion(Manager manager, Region region) {
        if (!Objects.equals(manager.getRegionCode(), region)) {
            throw new FranchiseException(ExceptionMessage.UNAUTHORIZED_FRANCHISE_MODIFY);
        }
    }


    @Override
    public FranchisePageResponseDto findAll(int page, int size) {

        if (page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Franchise> franchisePage = franchiseRepository.findAll(pageable);

        if (franchisePage.isEmpty()) {
            throw new IllegalArgumentException("franchise is empty");
        }

        List<FranchiseResponseDto> responseDto = franchisePage.getContent().stream()
                .map(franchise -> {
                    // 지역 이름 조회 (RegionName enum)
                    RegionName regionName = franchise.getRegionCode() != null
                            ? franchise.getRegionCode().getRegionName()
                            : null;

                    return new FranchiseResponseDto(franchise, regionName);
                })
                .toList();

        FranchisePageResponseDto pageResponseDto = new FranchisePageResponseDto(responseDto, franchisePage.getTotalElements());

        return pageResponseDto;
    }

    @Override
    public FranchiseResponseDto findByFranchiseId(Long franchiseId) {

        Franchise franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));

        return new FranchiseResponseDto( franchise, franchise.getRegionCode().getRegionName());
    }

    @Override
    @Transactional
    public void createFranchise(Principal principal, FranchiseRequestDto requestDto) {

        log.info("requestDto: {}", requestDto);
        String email = principal.getName();
        User user  = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));


        // 매니저 또는 본사만  프랜차이즈 생성 가능
        if (user.getUserType() != Role.MANAGER && user.getUserType() != Role.HEADQUARTER) {
            throw new FranchiseException(ExceptionMessage.UNAUTHORIZED_FRANCHISE_CREATE);
        }

        // 매니저가 본사 소속인지 확인
        if (user.getHeadquarterId() == null) {
            throw new FranchiseException(ExceptionMessage.HEADQUARTER_NOT_FOUND);
        }

        Headquarter headquarter = headquarterRepository.findByHeadquarterId(
                user.getHeadquarterId().getHeadquarterId()
        );

        RegionName regionName = requestDto.getRegionName();

        Region regionCode = regionRepository.findByRegionName(regionName);

        Franchise franchise = requestDto.toEntity(headquarter, regionCode);
        franchiseRepository.save(franchise);
    }

    @Override
    @Transactional
    public void updateFranchiseInfo(Long franchiseId, FranchiseUpdateRequestDto requestDto, Principal principal) {

        log.info("requestDto: {}", requestDto);

        // 사용자 조회
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        Franchise franchise = getFranchiseId(franchiseId);

        // 매니저일 경우: 지역 검증
        if (user.getUserType() == Role.MANAGER) {
            Manager manager = managerRepository.findByUserId(user)
                    .orElseThrow(() -> new FranchiseException(ExceptionMessage.MANAGER_NOT_FOUND));
            authorizeManagerRegion(manager, franchise.getRegionCode());
        } else if (user.getUserType() == Role.HEADQUARTER) {
        } else {
            throw new FranchiseException(ExceptionMessage.UNAUTHORIZED_FRANCHISE_MODIFY);
        }

        franchise.update(requestDto);
        franchiseRepository.save(franchise);
    }


    @Override
    public FranchiseSummaryDto findByFranchiseName(String name) {

        Franchise franchise = franchiseRepository.findByName(name)
                .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));


        Region region = franchise.getRegionCode();
        Manager manager = managerRepository.findByRegionCode(region)
                .orElseThrow(()-> new FranchiseException(ExceptionMessage.MANAGER_NOT_FOUND));

        User managerInfo = userRepository.findByUserId(manager.getUserId().getUserId())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));


        log.info("Franchise found: {}", franchise.getFranchiseId());
        log.info("Manager name: {}, phone: {}", managerInfo.getName(), managerInfo.getPhone());

        return new FranchiseSummaryDto(
                franchise.getFranchiseId(),
                franchise.getFranchiseTel(),
                managerInfo.getName(),
                managerInfo.getPhone()
        );
    }

    @Override
    public void deleteFranchise(Long franchiseId, Principal principal) {

        // 사용자 조회
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        Franchise franchise = getFranchiseId(franchiseId);

        // 매니저일 경우: 지역 검증
        if (user.getUserType() == Role.MANAGER) {
            Manager manager = managerRepository.findByUserId(user)
                    .orElseThrow(() -> new FranchiseException(ExceptionMessage.MANAGER_NOT_FOUND));
            authorizeManagerRegion(manager, franchise.getRegionCode());
        } else if (user.getUserType() == Role.HEADQUARTER) {

        } else {
            throw new FranchiseException(ExceptionMessage.UNAUTHORIZED_FRANCHISE_DELETE);
        }

        franchiseRepository.delete(franchise);
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


}
