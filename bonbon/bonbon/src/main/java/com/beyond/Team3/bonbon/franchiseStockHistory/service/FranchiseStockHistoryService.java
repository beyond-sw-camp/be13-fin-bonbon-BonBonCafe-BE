package com.beyond.Team3.bonbon.franchiseStockHistory.service;

import com.beyond.Team3.bonbon.common.enums.HistoryStatus;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.franchiseStock.entity.FranchiseStock;
import com.beyond.Team3.bonbon.franchiseStock.repository.FranchiseStockRepository;
import com.beyond.Team3.bonbon.franchiseStockHistory.dto.FranchiseStockHistoryRequestDto;
import com.beyond.Team3.bonbon.franchiseStockHistory.dto.FranchiseStockHistoryResponseDto;
import com.beyond.Team3.bonbon.franchiseStockHistory.entity.FranchiseStockHistory;
import com.beyond.Team3.bonbon.franchiseStockHistory.repository.FranchiseStockHistoryRepository;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.headquaterStock.repository.HeadquarterStockRepository;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FranchiseStockHistoryService {
    private final FranchiseRepository franchiseRepository;
    private final IngredientRepository ingredientRepository;
    private final HeadquarterRepository headquarterRepository;
    private final FranchiseStockRepository franchiseStockRepository;
    private final HeadquarterStockRepository headquarterStockRepository;
    private final FranchiseStockHistoryRepository franchiseStockHistoryRepository;

    public FranchiseStockHistoryResponseDto getHistory(Long franchiseId, Long historyId) {
        Franchise franchise = getFranchise(franchiseId);
        FranchiseStockHistory history = getHistoryById(historyId);

        return FranchiseStockHistoryResponseDto.from(history);
    }

    public Page<FranchiseStockHistoryResponseDto> getAllFranchiseHistory(Pageable pageable, Long headquarterId) {
        Page<FranchiseStockHistory> histories = franchiseStockHistoryRepository.getAllFranchiseHistory(pageable, headquarterId);
        return histories.map(FranchiseStockHistoryResponseDto::from);
    }

    public Page<FranchiseStockHistoryResponseDto> getAllHistory(Pageable pageable, Long franchiseId) {
        Page<FranchiseStockHistory> histories = franchiseStockHistoryRepository.getAllHistory(pageable, franchiseId);
        return histories.map(FranchiseStockHistoryResponseDto::from);
    }

    @Transactional
    public FranchiseStockHistoryResponseDto createFranchiseStockHistory(Long headquarterId, Long franchiseId, FranchiseStockHistoryRequestDto dto) {
        validateQuantity(dto.getQuantity()); // 주문 수량이 0보다 큰지 확인

        Franchise franchise = getFranchise(franchiseId); // 가맹점 정보
        validateHeadquarter(franchise, headquarterId); // 본사에 있는 가맹점인지 확인

        Ingredient ingredient = getIngredient(dto.getIngredientId()); // 재료 정보
        HeadquarterStock stock = getHeadquarterStock(headquarterId, dto.getIngredientId()); // 본사 재고 정보

        validateStockQuantity(stock.getQuantity(), dto.getQuantity()); // 본사 재고가 신청 재고보다 많은지

        FranchiseStockHistory history = dto.toEntity(franchise, ingredient);
        franchiseStockHistoryRepository.save(history);

        stock.subtractQuantity(dto.getQuantity()); // 본사 재고 감소

//        FranchiseStock franchiseStock = franchiseStockRepository
//                .findByFranchiseIdAndIngredientId(franchise, ingredient)
//                .orElseGet(() -> franchiseStockRepository.save(
//                        FranchiseStock.createFranchiseStock(franchise, ingredient, dto)
//                ));

//        franchiseStock.addQuantity(dto.getQuantity());

        return FranchiseStockHistoryResponseDto.from(history);
    }

    @Transactional
    public void deleteFranchiseStockHistory(Long franchiseId, Long historyId) {
        FranchiseStockHistory history = getHistoryById(historyId);
        history.validateFranchise(franchiseId);


        BigDecimal quantity = history.getQuantity();
        Ingredient ingredient = history.getIngredientId();
        Franchise franchise = history.getFranchiseId();

        FranchiseStock franchiseStock = getFranchiseStock(franchise, ingredient);
        franchiseStock.subtractQuantity(quantity);

        HeadquarterStock headquarterStock = getHeadquarterStock(franchise.getHeadquarterId().getHeadquarterId(), ingredient.getIngredientId());
        headquarterStock.addQuantity(quantity);

        franchiseStockHistoryRepository.delete(history);
    }

    @Transactional
    public FranchiseStockHistoryResponseDto updateHistory(Long headquarterId, Long franchiseId, Long historyId, FranchiseStockHistoryRequestDto requestDto) {
        validateQuantity(requestDto.getQuantity()); // 주문 수량은 0보다 크게

        FranchiseStockHistory history = getHistoryById(historyId);
        history.validateFranchise(franchiseId); // 히스토리의 가맹점과 파라미터로 받은 가맹점의 정보가 같은지

        // 이미 배송완료 또는 취소된 상태라면 수정 못하게 막기
        if (history.getHistoryStatus() != HistoryStatus.REQUESTED &&
                history.getQuantity().compareTo(requestDto.getQuantity()) != 0) {
            throw new IllegalStateException("수량을 변경할 수 없습니다.");
        }

        if (history.getHistoryStatus() == HistoryStatus.DELIVERED || history.getHistoryStatus() == HistoryStatus.CANCELLED) {
            throw new IllegalStateException("배송 완료 또는 취소된 신청은 수정할 수 없습니다.");
        }

        Franchise franchise = history.getFranchiseId();
        Ingredient oldIngredient = history.getIngredientId();                   // 기존 재료
        Ingredient newIngredient = getIngredient(requestDto.getIngredientId()); // 변경할 재료

        BigDecimal oldQuantity = history.getQuantity();     // 기존 수량
        BigDecimal newQuantity = requestDto.getQuantity();  // 변경할 수량

        HeadquarterStock oldStock = getHeadquarterStock(headquarterId, oldIngredient.getIngredientId()); // 기존 본사 재고
        oldStock.addQuantity(oldQuantity); // 재고 수량을 수량만큼 더함 (이후 변경된 수량만큼 감소)

        HeadquarterStock newStock = getHeadquarterStock(headquarterId, newIngredient.getIngredientId());
        validateStockQuantity(newStock.getQuantity(), newQuantity);
        newStock.subtractQuantity(newQuantity); // 본사 재고 감소

        // ✅ 가맹점 재고는 배송완료 상태일 때만 증가
        if (history.getHistoryStatus() != HistoryStatus.DELIVERED &&
                requestDto.getStatus() == HistoryStatus.DELIVERED) {

            FranchiseStock newFranchiseStock = franchiseStockRepository
                    .findByFranchiseIdAndIngredientId(franchise, newIngredient)
                    .orElseGet(() -> franchiseStockRepository.save(
                            FranchiseStock.createFranchiseStock(franchise, newIngredient, requestDto)
                    ));
            newFranchiseStock.addQuantity(newQuantity);
        }

        // ✅ 상태가 취소일 경우 본사 재고 다시 증가
        if (requestDto.getStatus() == HistoryStatus.CANCELLED) {
            HeadquarterStock cancelledStock = getHeadquarterStock(headquarterId, newIngredient.getIngredientId());
            cancelledStock.addQuantity(newQuantity);
        }

        history.updateHistory(newQuantity, requestDto.getStatus());

        return FranchiseStockHistoryResponseDto.from(history);
    }

    // ======================= 공통 메서드 =======================

    private Franchise getFranchise(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가맹점이 존재하지 않습니다."));
    }

    private Ingredient getIngredient(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("해당 재료가 존재하지 않습니다."));
    }

    private FranchiseStockHistory getHistoryById(Long historyId) {
        return franchiseStockHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록이 없습니다."));
    }

    private HeadquarterStock getHeadquarterStock(Long headquarterId, Long ingredientId) {
        return headquarterStockRepository
                .findByHeadquarter_HeadquarterIdAndIngredient_IngredientId(headquarterId, ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("해당 본사에 재고가 존재하지 않습니다."));
    }

    private FranchiseStock getFranchiseStock(Franchise franchise, Ingredient ingredient) {
        return franchiseStockRepository.findByFranchiseIdAndIngredientId(franchise, ingredient)
                .orElseThrow(() -> new IllegalArgumentException("해당 가맹점에 재고가 없습니다."));
    }

    private void validateQuantity(BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
    }

    private void validateStockQuantity(BigDecimal stockQuantity, BigDecimal requestedQuantity) {
        if (stockQuantity.compareTo(requestedQuantity) < 0) {
            throw new IllegalArgumentException(String.format(
                    "본사 재고가 부족합니다. 요청 수량: %s, 수정 가능 최대 재고: %s",
                    requestedQuantity.toPlainString(),
                    stockQuantity.toPlainString()
            ));
        }
    }

    private void validateHeadquarter(Franchise franchise, Long headquarterId) {
        if (!headquarterId.equals(franchise.getHeadquarterId().getHeadquarterId())) {
            throw new IllegalArgumentException("해당 본사에 있는 가맹점이 아닙니다");
        }
    }
}