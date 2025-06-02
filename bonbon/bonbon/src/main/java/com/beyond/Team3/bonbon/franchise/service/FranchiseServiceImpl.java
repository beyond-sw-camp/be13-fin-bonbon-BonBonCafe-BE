
package com.beyond.Team3.bonbon.franchise.service;


import com.beyond.Team3.bonbon.common.enums.FranchiseStatus;
import com.beyond.Team3.bonbon.common.enums.RegionName;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.franchise.dto.*;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.FranchiseLocation;
import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseLocationRepository;
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
import com.beyond.Team3.bonbon.user.repository.FranchiseeRepository;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import com.beyond.Team3.bonbon.user.repository.ManagerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final FranchiseLocationRepository franchiseLocationRepository;
    private final FranchiseeRepository franchiseeRepository;

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
    public Page<FranchiseResponseDto> findAll(int page, int size, String region, String district) {

        Pageable pageable = PageRequest.of(page, size);
        if (page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }


        Page<Franchise> franchisePage = franchiseRepository.findAll(pageable);
//        Page<Franchise> franchisePage;

//        if (region != null && district != null) {
//            // 조건에 맞는 가맹점만 조회
//            franchisePage = franchiseRepository.findByRegionAndDistrict(region, district, pageable);
//        } else {
//            // 전체 목록
//            franchisePage = franchiseRepository.findAll(pageable);
//        }


        if (franchisePage.isEmpty()) {
            throw new IllegalArgumentException("franchise is empty");
        }

        List<FranchiseResponseDto> responseDto = franchisePage.getContent().stream()
                .map(franchise -> {
                    // 지역 이름 조회 (RegionName enum)
                    RegionName regionName = franchise.getRegionCode() != null
                            ? franchise.getRegionCode().getRegionName()
                            : null;
                    // 점주 이름 조회
                    String franchiseeName = franchiseeRepository.findByFranchise(franchise)
                            .map(franchisee -> {
                                Long userId = franchisee.getUserId() != null ? franchisee.getUserId().getUserId() : null;
                                if (userId != null) {
                                    return userRepository.findById(userId)
                                            .map(User::getName)
                                            .orElse(null);
                                } else {
                                    return null;
                                }
                            })
                            .orElse(null); // 점주 없으면 null

                    return new FranchiseResponseDto(franchise, regionName, franchiseeName);

                })
                .toList();

//        FranchisePageResponseDto pageResponseDto = new FranchisePageResponseDto(responseDto, franchisePage.getTotalElements());

//        return pageResponseDto;
        return new PageImpl<>(responseDto, pageable, franchisePage.getTotalElements());
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

        // 권한 검사:  매니저 또는 본사만  프랜차이즈 생성 가능
        String email = principal.getName();
        User user  = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

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

        // 가정 : 본사 정책상 같은 건물엔 한 가맹점만 허용 roadAddress만 같아도 중복으로 판단
        // 주소 중복 체크: 도로명
        if(franchiseRepository.existsByRoadAddress(requestDto.getRoadAddress())){
            throw new FranchiseException(ExceptionMessage.ALREADY_REGISTERED_ADDRESS);
        }



        // 가맹점 등록
        RegionName regionName = requestDto.getRegionName();
        Region regionCode = regionRepository.findByRegionName(regionName);

        Franchise franchise = requestDto.toEntity(headquarter, regionCode);
        franchiseRepository.save(franchise);

        saveFranchiseLocation(franchise);
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
    @Transactional
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

        // 운영 중일 때만 영구 폐점 가능
        if (franchise.getStatus() == FranchiseStatus.OPERATING) {
            franchise.closePermanently();
        } else {
            throw new FranchiseException(ExceptionMessage.ALREADY_PERMANENT_CLOSED);
        }
        franchiseRepository.save(franchise);

        // 단계별 폐점?? 운영중 -> 임시 휴점 -> 영구 폐점
//        if (franchise.getStatus() == FranchiseStatus.OPERATING) {
//            franchise.closeTemporarily();// 운영 중인 상태에서만 임시 휴점 가능
//
//        } else if (franchise.getStatus() == FranchiseStatus.CLOSED_TEMP) {
//            franchise.closePermanently();// 임시 휴점 상태에서만 영구 폐점 가능
//        } else {
//            throw new FranchiseException(ExceptionMessage.ALREADY_PERMANENT_CLOSED);
//        }

    }




    @Transactional
    public void saveFranchiseLocation(Franchise franchise) {
        String roadAddress = franchise.getRoadAddress();

        log.info("roadAddress: {}", franchise);

        WebClient webClient = webClientBuilder
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK " + kakaoApiKey)
                .build();


        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", roadAddress)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Kakao API response: {}", response);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode documents = root.path("documents");

            if (!documents.isEmpty()) {
                JsonNode first = documents.get(0);
                double latitude = first.path("y").asDouble();
                double longitude = first.path("x").asDouble();


                FranchiseLocation location = new FranchiseLocation(franchise, latitude, longitude);
                franchiseLocationRepository.save(location);
            } else {
                log.warn("Kakao API에서 주소 결과를 찾을 수 없음: {}", roadAddress);
            }
        } catch (Exception e) {
            log.error("Kakao 주소 API 호출 실패", e);
            throw new FranchiseException(ExceptionMessage.KAKAO_API_FAILED); // 커스텀 예외 권장
        }



    }

    @Override
    public List<LocationResponseDto> findAllLocation() {
        List<FranchiseLocation> franchiseLocation = franchiseLocationRepository.findAll();

        return franchiseLocation.stream()
                .map(location -> new LocationResponseDto(
                        location.getFranchise().getName(),  // 가맹점 이름
                        location.getLatitude(),
                        location.getLongitude()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<LocationResponseDto> getLocationsTest() {
        List<Franchise> franchises = franchiseRepository.findAll();

        WebClient webClient = webClientBuilder
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK " + kakaoApiKey)
                .build();

        List<LocationResponseDto> locationList = new ArrayList<>();

        for (Franchise franchise : franchises) {
            // 이미 위치 정보가 저장되어 있다면 패스
            if (franchiseLocationRepository.existsByFranchise(franchise)) {
                FranchiseLocation existingLocation = franchiseLocationRepository.findByFranchise(franchise);
                locationList.add(new LocationResponseDto(
                        franchise.getName(),
                        existingLocation.getLatitude(),
                        existingLocation.getLongitude()
                ));
                continue;
            }

            // 위치 정보가 없다면 Kakao API 호출
            String address = franchise.getRoadAddress();

            try {
                String response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v2/local/search/address.json")
                                .queryParam("query", address)
                                .build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response);
                JsonNode documents = root.path("documents");

                if (!documents.isEmpty()) {
                    JsonNode first = documents.get(0);
                    double latitude = first.path("y").asDouble();
                    double longitude = first.path("x").asDouble();

                    // DB 저장
                    FranchiseLocation location = new FranchiseLocation(franchise, latitude, longitude);
                    franchiseLocationRepository.save(location);

                    // 반환용 리스트에도 추가
                    locationList.add(new LocationResponseDto(franchise.getName(), latitude, longitude));
                }
            } catch (Exception e) {
                log.error("Kakao API 호출 실패 (프랜차이즈: {}): {}", franchise.getName(), e.getMessage());
            }
        }

        return locationList;
    }
}

//
//    @Override
//    public List<FranchiseLocationDto> getFranchiseLocations() {
//
//        WebClient webClient = webClientBuilder
//                .baseUrl("https://dapi.kakao.com")
//                .defaultHeader("Authorization", "KakaoAK " + kakaoApiKey)
//                .build();
//
//        List<Franchise> franchises = franchiseRepository.findAll();
//
//        return franchises.stream()
//                .map(franchise -> {
//                    String address = franchise.getRoadAddress();
//                    String name = franchise.getName();
//
//                    String response = webClient.get()
//                            .uri(uriBuilder -> uriBuilder
//                                    .path("/v2/local/search/address.json")
//                                    .queryParam("query", address)
//                                    .build())
//                            .retrieve()
//                            .bodyToMono(String.class)
//                            .block();
//
//                    try {
//                        ObjectMapper mapper = new ObjectMapper();
//                        JsonNode root = mapper.readTree(response);
//                        JsonNode documents = root.path("documents");
//
//                        if (!documents.isEmpty()) {
//                            JsonNode first = documents.get(0);
//                            double latitude = first.path("y").asDouble();
//                            double longitude = first.path("x").asDouble();
//
//                            return new FranchiseLocation(name, latitude, longitude);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    return null;
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }


