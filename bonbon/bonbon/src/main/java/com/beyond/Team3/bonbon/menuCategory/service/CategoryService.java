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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MenuCategoryRepository menuCategoryRepository;

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

    @Transactional
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto) {
        Category category = findCategoryById(categoryId);
        category.updateCategory(requestDto.getName());

        return CategoryResponseDto.from(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        categoryRepository.delete(category);
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id=" + categoryId));
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }
}
