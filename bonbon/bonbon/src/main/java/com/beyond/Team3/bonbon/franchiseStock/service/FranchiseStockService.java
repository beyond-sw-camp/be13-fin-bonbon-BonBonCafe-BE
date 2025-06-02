package com.beyond.Team3.bonbon.franchiseStock.service;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.franchiseStock.dto.FranchiseStockRequestDto;
import com.beyond.Team3.bonbon.franchiseStock.dto.FranchiseStockResponseDto;
import com.beyond.Team3.bonbon.franchiseStock.entity.FranchiseStock;
import com.beyond.Team3.bonbon.franchiseStock.repository.FranchiseStockRepository;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.ingredient.repository.IngredientRepository;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.FranchiseeRepository;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static com.beyond.Team3.bonbon.handler.message.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FranchiseStockService {
    private final UserRepository userRepository;
    private final FranchiseRepository franchiseRepository;
    private final IngredientRepository ingredientRepository;
    private final FranchiseeRepository franchiseeRepository;
    private final FranchiseStockRepository franchiseStockRepository;

    @Transactional(readOnly = true)
    public FranchiseStockResponseDto getFranchiseStock(Principal principal, Long franchiseStockId) {
        Franchisee franchisee = getFranchiseeByPrincipal(principal);
        FranchiseStock franchiseStock = findStockByIdOrThrow(franchiseStockId);
        System.out.println(franchisee.getFranchise().getFranchiseId());
        System.out.println("ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ");
        System.out.println(franchiseStock.getFranchiseId().getFranchiseId() + "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ");
        if (!franchisee.getFranchise().getFranchiseId().equals(franchiseStock.getFranchiseId().getFranchiseId())) {
            throw new IllegalArgumentException("해당 가맹점의 재고가 아닙니다.");
        }
        return FranchiseStockResponseDto.from(franchiseStock);
    }

    public Page<FranchiseStockResponseDto> getAllStock(Pageable pageable, Principal principal) {
        Franchisee franchisee = getFranchiseeByPrincipal(principal);
        Page<FranchiseStock> franchiseStocks = franchiseStockRepository.getAllStock(pageable, franchisee.getFranchise().getFranchiseId());
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
    public void deleteFranchiseStock(Principal principal, Long franchiseStockId) {
        Franchisee franchisee = getFranchiseeByPrincipal(principal);
        FranchiseStock franchiseStock = findStockByIdOrThrow(franchiseStockId);

        if (!franchisee.getFranchise().equals(franchiseStock.getFranchiseId())) {
            throw new IllegalArgumentException("해당 가맹점의 재고가 아닙니다.");
        }
        franchiseStockRepository.delete(franchiseStock);
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

    private User getLoginUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private Franchisee getFranchiseeByPrincipal(Principal principal) {
        User user = getLoginUser(principal);
        return franchiseeRepository.findByUserId(user)
                .orElseThrow(() -> new IllegalArgumentException("가맹점 정보 없음"));
    }
}