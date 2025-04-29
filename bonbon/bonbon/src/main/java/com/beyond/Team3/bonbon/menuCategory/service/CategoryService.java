package com.beyond.Team3.bonbon.menuCategory.service;

import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryRequestDto;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryResponseDto;
import com.beyond.Team3.bonbon.menuCategory.entity.Category;
import com.beyond.Team3.bonbon.menuCategory.entity.MenuCategory;
import com.beyond.Team3.bonbon.menuCategory.repository.CategoryRepository;
import com.beyond.Team3.bonbon.menuCategory.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category category = Category.createCategory(requestDto.getName());
        categoryRepository.save(category);
        return CategoryResponseDto.from(category);
    }

    public List<MenuResponseDto> getMenusByCategory(Long categoryId) {
        List<MenuCategory> menuCategories = menuCategoryRepository.findByCategoryCategoryId(categoryId);
        return menuCategories.stream()
                .map(menuCategory -> MenuResponseDto.from(menuCategory.getMenu()))
                .collect(Collectors.toList());
    }
}
