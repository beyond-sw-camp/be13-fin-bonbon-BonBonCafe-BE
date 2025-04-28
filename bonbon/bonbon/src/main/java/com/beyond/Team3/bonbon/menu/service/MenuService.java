package com.beyond.Team3.bonbon.menu.service;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.menu.dto.MenuRequestDto;
import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final HeadquarterRepository headquarterRepository;

    public Page<MenuResponseDto> getAllMenu(Pageable pageable) {

        return null;
    }

    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto menuRequestDto, Long headquarterId) {
        Headquarter headquarter = headquarterRepository.findById(headquarterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 본사가 없습니다."));
        Menu menu = Menu.createMenu(menuRequestDto, headquarter);
        menuRepository.save(menu);
        return MenuResponseDto.menuResponseDto(menu);
    }

    public MenuResponseDto updateMenu(Long menuId, MenuRequestDto menuRequestDto) {

        return null;
    }

    public MenuResponseDto deleteMenu(Long menuId) {

        return null;
    }
}
