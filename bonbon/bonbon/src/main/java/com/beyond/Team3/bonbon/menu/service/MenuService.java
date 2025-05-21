package com.beyond.Team3.bonbon.menu.service;

import com.beyond.Team3.bonbon.handler.exception.UserException;
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
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.beyond.Team3.bonbon.handler.message.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final MenuDetailRepository menuDetailRepository;
    private final IngredientRepository ingredientRepository;
    private final HeadquarterRepository headquarterRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    public Page<MenuResponseDto> getAllMenu(Pageable pageable, Principal principal, String search) {
        User user = getLoginUser(principal);
        Page<Menu> menu = menuRepository.findAllMenu(pageable, user.getHeadquarterId().getHeadquarterId(), search);
        return menu.map(MenuResponseDto::from);
    }

    @Transactional
    public MenuResponseDto getMenu(Long menuId, Principal principal) {
        User user = getLoginUser(principal);
        Menu menu = findMenuWithHeadquarterValidation(menuId, user.getHeadquarterId().getHeadquarterId());
        return MenuResponseDto.from(menu);
    }

    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto dto, Principal principal) {
        User user = getLoginUser(principal);

        Menu menu = dto.toEntity(user.getHeadquarterId());
        menuRepository.save(menu);

        applyCategories(menu, dto.getCategoryIds());
        applyIngredients(menu, dto.getMenuDetails());

        return MenuResponseDto.from(menu);
    }

    @Transactional
    public MenuResponseDto updateMenu(Long menuId, Principal principal, MenuRequestDto dto) {
        User user = getLoginUser(principal);
        Menu menu = findMenuWithHeadquarterValidation(menuId, user.getHeadquarterId().getHeadquarterId());
        menu.updateMenu(dto);

        if (dto.getCategoryIds() != null) {
            menu.getCategories().clear();
            menuCategoryRepository.deleteByMenu(menu);
            applyCategories(menu, dto.getCategoryIds());
        }

        if (dto.getMenuDetails() != null) {
            menu.getDetails().clear();
            menuDetailRepository.deleteByMenu(menu);
            applyIngredients(menu, dto.getMenuDetails());
        }

        return MenuResponseDto.from(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId, Principal principal) {
        User user = getLoginUser(principal);
        findMenuWithHeadquarterValidation(menuId, user.getHeadquarterId().getHeadquarterId());
        menuRepository.deleteById(menuId);
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getMenusByCategoryAndHeadquarter(Long categoryId, Principal principal) {
        User user = getLoginUser(principal);
        List<Menu> menus = menuRepository.findMenusByCategoryAndHeadquarter(categoryId, user.getHeadquarterId().getHeadquarterId());
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

    private User getLoginUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}