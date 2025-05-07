package com.beyond.Team3.bonbon.headquarter.service;

import com.beyond.Team3.bonbon.headquarter.dto.HeadquarterRequestDto;
import com.beyond.Team3.bonbon.headquarter.dto.HeadquarterResponseDto;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryResponseDto;
import com.beyond.Team3.bonbon.menuCategory.entity.Category;
import com.beyond.Team3.bonbon.menuCategory.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeadquarterService {
    private final CategoryRepository categoryRepository;
    private final HeadquarterRepository headquarterRepository;


    public HeadquarterResponseDto getHeadquarter(Long headquarterId) {

        Headquarter headquarter = headquarterRepository.findById(headquarterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 본사가 없습니다"));

        return HeadquarterResponseDto.from(headquarter);
    }

    @Transactional
    public HeadquarterResponseDto updateHeadquarter(Long headquarterId, HeadquarterRequestDto headquarterRequestDto) {
// headquarterId -> 로그인 한 정보
        Headquarter headquarter = headquarterRepository.findById(headquarterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 본사가 없습니다"));

        headquarter.updateHeadquarter(headquarterRequestDto);
        return HeadquarterResponseDto.from(headquarter);
    }

    public List<CategoryResponseDto> getCategoriesByHeadquarter(Long headquarterId) {
        List<Category> categories = categoryRepository.findDistinctByHeadquarterId(headquarterId);
        return categories.stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }
}
