package com.beyond.Team3.bonbon.menu.service;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.ingredient.repository.IngredientRepository;
import com.beyond.Team3.bonbon.menu.dto.MenuRequestDto;
import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menu.repository.MenuRepository;
import com.beyond.Team3.bonbon.menuCategory.entity.Category;
import com.beyond.Team3.bonbon.menuCategory.entity.MenuCategory;
import com.beyond.Team3.bonbon.menuCategory.repository.CategoryRepository;
import com.beyond.Team3.bonbon.menuCategory.repository.MenuCategoryRepository;
import com.beyond.Team3.bonbon.menuDetail.dto.MenuDetailRequestDto;
import com.beyond.Team3.bonbon.menuDetail.entity.MenuDetail;
import com.beyond.Team3.bonbon.menuDetail.repository.MenuDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final MenuDetailRepository menuDetailRepository;
    private final IngredientRepository ingredientRepository;
    private final HeadquarterRepository headquarterRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    public Page<MenuResponseDto> getAllMenu(Pageable pageable, Long headquarterId, String search) {
        Page<Menu> menu = menuRepository.findAllMenu(pageable, headquarterId, search);
        return menu.map(MenuResponseDto::from);
    }

    public MenuResponseDto getMenu(Long menuId, Long headquarterId) {
        Menu menu = findMenuWithHeadquarterValidation(menuId, headquarterId);
        return MenuResponseDto.from(menu);
    }

    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto dto, Long headquarterId) {
        Headquarter headquarter = headquarterRepository.findById(headquarterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 본사가 없습니다."));

        Menu menu = Menu.createMenu(dto, headquarter);
        menuRepository.save(menu);

        applyCategories(menu, dto.getCategoryIds());
        applyIngredients(menu, dto.getMenuDetails());

        return MenuResponseDto.from(menu);
    }

    @Transactional
    public MenuResponseDto updateMenu(Long menuId, Long headquarterId, MenuRequestDto dto) {
        Menu menu = findMenuWithHeadquarterValidation(menuId, headquarterId);
        menu.updateMenu(dto);

        menu.getCategories().clear();
        menuCategoryRepository.deleteByMenu(menu);
        applyCategories(menu, dto.getCategoryIds());

        menu.getDetails().clear();
        menuDetailRepository.deleteByMenu(menu);
        applyIngredients(menu, dto.getMenuDetails());

        return MenuResponseDto.from(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId, Long headquarterId) {
        findMenuWithHeadquarterValidation(menuId, headquarterId);
        menuRepository.deleteById(menuId);
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getMenusByCategoryAndHeadquarter(Long categoryId, Long headquarterId) {
        List<Menu> menus = menuRepository.findMenusByCategoryAndHeadquarter(categoryId, headquarterId);
        return menus.stream().map(MenuResponseDto::from).collect(Collectors.toList());
    }

    private Menu findMenuWithHeadquarterValidation(Long menuId, Long headquarterId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 없습니다."));
        if (!menu.hasSameHeadquarter(headquarterId)) {
            throw new IllegalArgumentException("해당 메뉴가 없습니다.");
        }
        return menu;
    }

    private void applyCategories(Menu menu, List<Long> categoryIds) {
        if (categoryIds != null) {
            for (Long categoryId : categoryIds) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id=" + categoryId));

                // 이미 존재하는 MenuCategory 확인
                boolean exists = menu.getCategories().stream()
                        .anyMatch(mc -> mc.getCategory().equals(category));
                if (!exists) {
                    menu.addCategory(new MenuCategory(menu, category));
                }
            }
        }
    }

    private void applyIngredients(Menu menu, List<MenuDetailRequestDto> menuDetails) {
        if (menuDetails != null) {
            for (MenuDetailRequestDto detailDto : menuDetails) {
                Ingredient ingredient = ingredientRepository.findById(detailDto.getIngredientId())
                        .orElseThrow(() -> new IllegalArgumentException("재료 없음"));
                menu.addDetail(new MenuDetail(menu, ingredient, detailDto.getQuantity()));
            }
        }
    }
}