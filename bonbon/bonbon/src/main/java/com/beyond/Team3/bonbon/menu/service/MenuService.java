package com.beyond.Team3.bonbon.menu.service;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.menu.dto.MenuRequestDto;
import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menu.repository.MenuRepository;
import com.beyond.Team3.bonbon.menuCategory.entity.Category;
import com.beyond.Team3.bonbon.menuCategory.entity.MenuCategory;
import com.beyond.Team3.bonbon.menuCategory.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final HeadquarterRepository headquarterRepository;

    // 메뉴 전체 조회
    public Page<MenuResponseDto> getAllMenu(Pageable pageable, Long headquarterId, String search) {
//        if(!headquarterId.equals(path로받은본사아이디) {
//            throw new AccessDeniedException("접근할 수 없습니다.");
//        }
        Page<Menu> menu = menuRepository.findAllMenu(pageable, headquarterId, search);
        return menu.map(MenuResponseDto::from);
    }

    // 메뉴 단일 조회
    public MenuResponseDto getMenu(Long menuId, Long headquarterId) {
        Menu menu = findMenuWithHeadquarterValidation(menuId, headquarterId);

        return MenuResponseDto.from(menu);
    }

    // 메뉴 등록
    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto menuRequestDto, Long headquarterId) {
        Headquarter headquarter = headquarterRepository.findById(headquarterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 본사가 없습니다."));

        Menu menu = Menu.createMenu(menuRequestDto, headquarter);
        menuRepository.save(menu);

        if (menuRequestDto.getCategoryIds() != null) {
            for (Long categoryId : menuRequestDto.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id=" + categoryId));

                MenuCategory menuCategory = new MenuCategory(menu, category);
                menu.addCategory(menuCategory);
            }
        }
        return MenuResponseDto.from(menu);
    }

    // 메뉴 수정
    @Transactional
    public MenuResponseDto updateMenu(Long menuId, Long headquarterId, MenuRequestDto menuRequestDto) {
        Menu menu = findMenuWithHeadquarterValidation(menuId, headquarterId);
        menu.updateMenu(menuRequestDto);
        return MenuResponseDto.from(menu);
    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(Long menuId, Long headquarterId) {
        findMenuWithHeadquarterValidation(menuId, headquarterId);
        menuRepository.deleteById(menuId);
    }

    /**
     * 메서드
     */
    private Menu findMenuWithHeadquarterValidation(Long menuId, Long headquarterId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 없습니다."));

        if (!menu.hasSameHeadquarter(headquarterId)) {
            throw new IllegalArgumentException("해당 메뉴가 없습니다.");
        }

        return menu;
    }
}
