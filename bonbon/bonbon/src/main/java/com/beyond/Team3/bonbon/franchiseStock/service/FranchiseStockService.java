package com.beyond.Team3.bonbon.franchiseStock.service;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.franchiseStock.dto.FranchiseStockRequestDto;
import com.beyond.Team3.bonbon.franchiseStock.dto.FranchiseStockResponseDto;
import com.beyond.Team3.bonbon.franchiseStock.entity.FranchiseStock;
import com.beyond.Team3.bonbon.franchiseStock.repository.FranchiseStockRepository;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseStockService {
    private final IngredientRepository ingredientRepository;
    private final FranchiseRepository franchiseRepository;
    private final FranchiseStockRepository franchiseStockRepository;

    public FranchiseStockResponseDto getFranchiseStock(Long franchiseStockId) {
        FranchiseStock franchiseStock = findStockByIdOrThrow(franchiseStockId);
        return FranchiseStockResponseDto.from(franchiseStock);
    }

    public Page<FranchiseStockResponseDto> getAllStock(Pageable pageable, Long franchiseId) {
        Page<FranchiseStock> franchiseStocks = franchiseStockRepository.getAllStock(pageable, franchiseId);
        return franchiseStocks.map(FranchiseStockResponseDto::from);
    }

//    @Transactional
//    public FranchiseStockResponseDto createFranchiseStock(Long franchiseId, FranchiseStockRequestDto dto) {
//        Franchise franchise = findFranchiseOrThrow(franchiseId);
//        Ingredient ingredient = findIngredientOrThrow(dto.getIngredientId());
//        if (franchiseStockRepository.existsByFranchiseIdAndIngredientId(franchise, ingredient)) {
//            throw new IllegalArgumentException("이미 등록된 재료입니다.");
//        }
//
//        FranchiseStock franchiseStock = FranchiseStock.createFranchiseStock(franchise, ingredient, dto);
//        FranchiseStock saved = franchiseStockRepository.save(franchiseStock);
//
//        return FranchiseStockResponseDto.from(saved);
//    }

    @Transactional
    public void deleteFranchiseStock(Long franchiseStockId) {
        FranchiseStock stock = findStockByIdOrThrow(franchiseStockId);
        franchiseStockRepository.delete(stock);
    }

    @Transactional
    public FranchiseStockResponseDto updateFranchiseStock(Long franchiseStockId, FranchiseStockRequestDto dto) {
        FranchiseStock franchiseStock = findStockByIdOrThrow(franchiseStockId);
        Ingredient ingredient = findIngredientOrThrow(dto.getIngredientId());

        franchiseStock.updateStock(ingredient, dto.getQuantity());

        return FranchiseStockResponseDto.from(franchiseStock);
    }

    private Franchise findFranchiseOrThrow(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프랜차이즈가 없습니다."));
    }

    private Ingredient findIngredientOrThrow(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("해당 재료가 없습니다."));
    }

    private FranchiseStock findStockByIdOrThrow(Long stockId) {
        return franchiseStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("해당 재고 정보가 없습니다."));
    }
}