package com.beyond.Team3.bonbon.headquarter.service;

import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.headquarter.dto.HeadquarterRequestDto;
import com.beyond.Team3.bonbon.headquarter.dto.HeadquarterResponseDto;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryResponseDto;
import com.beyond.Team3.bonbon.menuCategory.entity.Category;
import com.beyond.Team3.bonbon.menuCategory.repository.CategoryRepository;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.beyond.Team3.bonbon.handler.message.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class HeadquarterService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final HeadquarterRepository headquarterRepository;

    public HeadquarterResponseDto getHeadquarter(Principal principal) {
        Headquarter headquarter = getUserHeadquarter(principal);
        return HeadquarterResponseDto.from(headquarter);
    }

    @Transactional
    public HeadquarterResponseDto updateHeadquarter(Principal principal, HeadquarterRequestDto headquarterRequestDto) {
        Headquarter headquarter = getUserHeadquarter(principal);
        headquarter.updateHeadquarter(headquarterRequestDto);
        return HeadquarterResponseDto.from(headquarter);
    }

    public List<CategoryResponseDto> getCategoriesByHeadquarter(Principal principal) {
        Headquarter headquarter = getUserHeadquarter(principal);
        List<Category> categories = categoryRepository.findDistinctByHeadquarterId(headquarter.getHeadquarterId());
        return categories.stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }

    private Headquarter getUserHeadquarter(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return headquarterRepository.findById(user.getHeadquarterId().getHeadquarterId())
                .orElseThrow(() -> new IllegalArgumentException("해당 본사가 없습니다"));
    }
}
