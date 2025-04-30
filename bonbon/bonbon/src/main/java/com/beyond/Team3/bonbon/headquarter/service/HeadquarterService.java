package com.beyond.Team3.bonbon.headquarter.service;

import com.beyond.Team3.bonbon.menuCategory.dto.CategoryResponseDto;
import com.beyond.Team3.bonbon.menuCategory.entity.Category;
import com.beyond.Team3.bonbon.menuCategory.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeadquarterService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> getCategoriesByHeadquarter(Long headquarterId) {
        List<Category> categories = categoryRepository.findDistinctByHeadquarterId(headquarterId);
        return categories.stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }
}
