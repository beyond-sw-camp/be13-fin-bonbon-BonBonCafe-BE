
package com.beyond.Team3.bonbon.franchise.service;


import com.beyond.Team3.bonbon.franchise.dto.FranchisePageResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseRequestDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseUpdateRequestDto;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final HeadquarterRepository headquarterRepository;


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

}
