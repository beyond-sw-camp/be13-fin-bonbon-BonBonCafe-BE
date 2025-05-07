package com.beyond.Team3.bonbon.headquaterStock.service;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.headquaterStock.dto.HeadquarterStockRequestDto;
import com.beyond.Team3.bonbon.headquaterStock.dto.HeadquarterStockResponseDto;
import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.headquaterStock.repository.HeadquarterStockRepository;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeadquarterStockService {
    private final IngredientRepository ingredientRepository;
    private final HeadquarterRepository headquarterRepository;
    private final HeadquarterStockRepository headquarterStockRepository;

    public HeadquarterStockResponseDto getHeadquarterStock(Long headquarterStockId) {
        HeadquarterStock headquarterStock = findStockByIdOrThrow(headquarterStockId);

//        if (!headquarterStock.getHeadquarter().getHeadquarterId().equals(headquarterId)) {
//            throw new IllegalArgumentException("해당 본사의 재고가 아닙니다.");
//        } 로그인 한 정보로..
        return HeadquarterStockResponseDto.from(headquarterStock);
    }

    public Page<HeadquarterStockResponseDto> getAllStock(Pageable pageable, Long headquarterId) {
        Page<HeadquarterStock> headquarterStocks = headquarterStockRepository.getAllStock(pageable, headquarterId);
        return headquarterStocks.map(HeadquarterStockResponseDto::from);
    }

    @Transactional
    public HeadquarterStockResponseDto createHeadquarterStock(Long headquarterId, HeadquarterStockRequestDto dto) {
        Headquarter headquarter = findHeadquarterOrThrow(headquarterId);
        Ingredient ingredient = findIngredientOrThrow(dto.getIngredientName());
        if (headquarterStockRepository.existsByHeadquarterAndIngredient(headquarter, ingredient)) {
            throw new IllegalArgumentException("이미 등록된 재료입니다.");
        }

        HeadquarterStock headquarterStock = HeadquarterStock.createHeadquarterStock(headquarter, ingredient, dto);
        HeadquarterStock saved = headquarterStockRepository.save(headquarterStock);

        return HeadquarterStockResponseDto.from(saved);
    }


    @Transactional
    public void deleteHeadquarterStock(Long headquarterStockId) {
        HeadquarterStock stock = findStockByIdOrThrow(headquarterStockId);

//        if (!headquarterStock.getHeadquarter().getHeadquarterId().equals(headquarterId)) {
//            throw new IllegalArgumentException("해당 본사의 재고가 아닙니다.");
//        } 로그인 한 정보로..
        headquarterStockRepository.delete(stock);
    }

    @Transactional
    public HeadquarterStockResponseDto updateHeadquarterStock(Long headquarterStockId, HeadquarterStockRequestDto headquarterStockRequestDto) {
        HeadquarterStock headquarterStock = findStockByIdOrThrow(headquarterStockId);
        Ingredient ingredient = findIngredientOrThrow(headquarterStockRequestDto.getIngredientName());

        headquarterStock.updateStock(ingredient, headquarterStockRequestDto.getQuantity());

        return HeadquarterStockResponseDto.from(headquarterStock);
    }

    private Headquarter findHeadquarterOrThrow(Long id) {
        return headquarterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 본사가 없습니다."));
    }

    private Ingredient findIngredientOrThrow(String name) {
        return ingredientRepository.findByIngredientName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 재료가 없습니다."));
    }

    private HeadquarterStock findStockByIdOrThrow(Long stockId) {
        return headquarterStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("해당 재고 정보가 없습니다."));
    }


}
