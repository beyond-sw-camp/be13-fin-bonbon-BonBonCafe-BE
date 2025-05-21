package com.beyond.Team3.bonbon.headquaterStock.service;

import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.headquaterStock.dto.HeadquarterStockRequestDto;
import com.beyond.Team3.bonbon.headquaterStock.dto.HeadquarterStockResponseDto;
import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.headquaterStock.repository.HeadquarterStockRepository;
import com.beyond.Team3.bonbon.ingredient.dto.IngredientRequestDto;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.ingredient.repository.IngredientRepository;
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
public class HeadquarterStockService {
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final HeadquarterRepository headquarterRepository;
    private final HeadquarterStockRepository headquarterStockRepository;

    @Transactional(readOnly = true)
    public HeadquarterStockResponseDto getHeadquarterStock(Principal principal, Long headquarterStockId) {
        User user = getLoginUser(principal);
        HeadquarterStock headquarterStock = findStockByIdOrThrow(headquarterStockId);

        if (!headquarterStock.getHeadquarter().getHeadquarterId().equals(user.getHeadquarterId().getHeadquarterId())) {
            throw new IllegalArgumentException("해당 본사의 재고가 아닙니다.");
        }
        return HeadquarterStockResponseDto.from(headquarterStock);
    }

    public Page<HeadquarterStockResponseDto> getAllStock(Pageable pageable, Principal principal, String search) {
        User user = getLoginUser(principal);
        Page<HeadquarterStock> headquarterStocks = headquarterStockRepository.getAllStock(pageable, user.getHeadquarterId().getHeadquarterId(), search);
        return headquarterStocks.map(HeadquarterStockResponseDto::from);
    }

    @Transactional
    public HeadquarterStockResponseDto createHeadquarterStock(Principal principal, HeadquarterStockRequestDto dto) {
        User user = getLoginUser(principal);
        Headquarter headquarter = findHeadquarterOrThrow(user.getHeadquarterId().getHeadquarterId());
        Ingredient ingredient = findIngredientOrThrow(dto.getIngredientId());
        if (headquarterStockRepository.existsByHeadquarterAndIngredient(headquarter, ingredient)) {
            throw new IllegalArgumentException("이미 등록된 재료입니다.");
        }

        HeadquarterStock headquarterStock = dto.toEntity(headquarter, ingredient);
        HeadquarterStock saved = headquarterStockRepository.save(headquarterStock);

        return HeadquarterStockResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<IngredientRequestDto> getIngredientList(Principal principal) {
        User user = getLoginUser(principal);
        Long headquarterId = user.getHeadquarterId().getHeadquarterId();
        List<HeadquarterStock> stocks = headquarterStockRepository.findByHeadquarterIdWithIngredient(headquarterId);

        return stocks.stream()
                .map(stock -> {
                    Ingredient ingredient = stock.getIngredient(); // var 대신 명시
                    return IngredientRequestDto.toEntity(ingredient);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteHeadquarterStock(Principal principal, Long headquarterStockId) {
        User user = getLoginUser(principal);
        HeadquarterStock headquarterStock = findStockByIdOrThrow(headquarterStockId);

        if (!headquarterStock.getHeadquarter().getHeadquarterId().equals(user.getHeadquarterId().getHeadquarterId())) {
            throw new IllegalArgumentException("해당 본사의 재고가 아닙니다.");
        }
        headquarterStockRepository.delete(headquarterStock);
    }

    @Transactional
    public HeadquarterStockResponseDto updateHeadquarterStock(Principal principal, Long headquarterStockId, HeadquarterStockRequestDto headquarterStockRequestDto) {
        User user = getLoginUser(principal);
        HeadquarterStock headquarterStock = findStockByIdOrThrow(headquarterStockId);
        Ingredient ingredient = findIngredientOrThrow(headquarterStockRequestDto.getIngredientId());

        if (!headquarterStock.getHeadquarter().getHeadquarterId().equals(user.getHeadquarterId().getHeadquarterId())) {
            throw new IllegalArgumentException("해당 본사의 재고가 아닙니다.");
        }

        headquarterStock.updateStock(ingredient, headquarterStockRequestDto.getQuantity());

        return HeadquarterStockResponseDto.from(headquarterStock);
    }

    private Headquarter findHeadquarterOrThrow(Long headquarterId) {
        return headquarterRepository.findById(headquarterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 본사가 없습니다."));
    }

    private Ingredient findIngredientOrThrow(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("해당 재료가 없습니다."));
    }

    private HeadquarterStock findStockByIdOrThrow(Long stockId) {
        return headquarterStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("해당 재고 정보가 없습니다."));
    }

    private User getLoginUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
