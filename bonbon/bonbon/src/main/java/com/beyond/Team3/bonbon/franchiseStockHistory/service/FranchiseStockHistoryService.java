package com.beyond.Team3.bonbon.franchiseStockHistory.service;

import com.beyond.Team3.bonbon.common.enums.HistoryStatus;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.common.validator.QuantityValidator;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.franchiseStock.entity.FranchiseStock;
import com.beyond.Team3.bonbon.franchiseStock.repository.FranchiseStockRepository;
import com.beyond.Team3.bonbon.franchiseStockHistory.dto.FranchiseStockHistoryRequestDto;
import com.beyond.Team3.bonbon.franchiseStockHistory.dto.FranchiseStockHistoryResponseDto;
import com.beyond.Team3.bonbon.franchiseStockHistory.entity.FranchiseStockHistory;
import com.beyond.Team3.bonbon.franchiseStockHistory.repository.FranchiseStockHistoryRepository;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.headquaterStock.repository.HeadquarterStockRepository;
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

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Objects;

import static com.beyond.Team3.bonbon.handler.message.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FranchiseStockHistoryService {
    private final UserRepository userRepository;
    private final FranchiseRepository franchiseRepository;
    private final IngredientRepository ingredientRepository;
    private final FranchiseeRepository franchiseeRepository;
    private final HeadquarterRepository headquarterRepository;
    private final FranchiseStockRepository franchiseStockRepository;
    private final HeadquarterStockRepository headquarterStockRepository;
    private final FranchiseStockHistoryRepository franchiseStockHistoryRepository;

    @Transactional(readOnly = true)
    public FranchiseStockHistoryResponseDto getHistory(Principal principal, Long historyId) {
        // 1. 해당 히스토리 조회
        FranchiseStockHistory history = getHistoryById(historyId);

        // 2. 로그인한 유저 정보 조회
        User user = getLoginUser(principal);

        // 3. 해당 유저의 Franchisee 정보 조회
        if (user.getUserType() == Role.FRANCHISEE) {
            Franchisee franchisee = getFranchiseeByPrincipal(principal);

            Long userFranchiseId = franchisee.getFranchise().getFranchiseId(); // 유저의 프랜차이즈 아이디
            Long historyFranchiseId = history.getFranchiseId().getFranchiseId();

            // 히스토리의 가맹점이 로그인한 유저의 가맹점인지 확인
            if (!userFranchiseId.equals(historyFranchiseId)) {
                throw new IllegalArgumentException("본인의 신청 내역이 아닙니다.");
            }
        }
        if (user.getUserType() == Role.HEADQUARTER) {
            if (!user.getHeadquarterId().getHeadquarterId()
                    .equals(history.getFranchiseId().getHeadquarterId().getHeadquarterId())) {
                throw new IllegalArgumentException("해당 본사의 가맹점이 아닙니다.");
            }
        }

        return FranchiseStockHistoryResponseDto.from(history);
    }

    public Page<FranchiseStockHistoryResponseDto> getAllFranchiseHistory(Pageable pageable, Principal principal) {
        // 본사 입장
        User user = getLoginUser(principal);

        Page<FranchiseStockHistory> histories = franchiseStockHistoryRepository.getAllFranchiseHistory(pageable, user.getHeadquarterId().getHeadquarterId());
        return histories.map(FranchiseStockHistoryResponseDto::from);
    }

    public Page<FranchiseStockHistoryResponseDto> getAllHistory(Pageable pageable, Principal principal) {
        // 로그인한 유저 정보 조회
        User user = getLoginUser(principal);

        // 해당 유저의 Franchisee 정보 조회
        Franchisee franchisee = getFranchiseeByPrincipal(principal);

        Page<FranchiseStockHistory> histories = franchiseStockHistoryRepository.getAllHistory(pageable, franchisee.getFranchise().getFranchiseId());
        return histories.map(FranchiseStockHistoryResponseDto::from);
    }

    @Transactional
    public FranchiseStockHistoryResponseDto createFranchiseStockHistory(Principal principal, FranchiseStockHistoryRequestDto dto) {
        QuantityValidator.validate(dto.getQuantity()); // 1백만 이하로

        User user = getLoginUser(principal);

        Franchisee franchisee = getFranchiseeByPrincipal(principal);

        Headquarter headquarter = user.getHeadquarterId();

        Franchise franchise = franchisee.getFranchise(); // 가맹점 정보
        validateHeadquarter(franchise, headquarter.getHeadquarterId()); // 본사에 있는 가맹점인지 확인

        Ingredient ingredient = getIngredient(dto.getIngredientId()); // 재료 정보
        HeadquarterStock stock = getHeadquarterStock(headquarter.getHeadquarterId(), dto.getIngredientId()); // 본사 재고 정보

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
    public void deleteFranchiseStockHistory(Principal principal, Long historyId) {
        FranchiseStockHistory history = getHistoryById(historyId);

        User user = getLoginUser(principal);
        Franchise franchise = history.getFranchiseId();
        Ingredient ingredient = history.getIngredientId();
        BigDecimal quantity = history.getQuantity();

        // 권한 체크
        if (user.getUserType() == Role.FRANCHISEE) {
            Franchisee franchisee = getFranchiseeByPrincipal(principal);
            history.validateFranchise(franchisee.getFranchise().getFranchiseId());
        } else if (user.getUserType() == Role.HEADQUARTER) {
            if (!franchise.getHeadquarterId().equals(user.getHeadquarterId())) {
                throw new IllegalArgumentException("해당 본사의 가맹점의 신청내역이 아닙니다.");
            }
        }

        //  가맹점 재고 차감은 있으면
        franchiseStockRepository.findByFranchiseIdAndIngredientId(franchise, ingredient)
                .ifPresent(fs -> fs.subtractQuantity(quantity));

        //  본사 재고 복구
        HeadquarterStock headquarterStock = getHeadquarterStock(
                franchise.getHeadquarterId().getHeadquarterId(),
                ingredient.getIngredientId()
        );
        if (headquarterStock != null) {
            headquarterStock.addQuantity(quantity);
        }

        //  삭제
        franchiseStockHistoryRepository.delete(history);
    }

    @Transactional
    public FranchiseStockHistoryResponseDto updateHistory(Principal principal, Long historyId, FranchiseStockHistoryRequestDto dto) {
        QuantityValidator.validate(dto.getQuantity());

        FranchiseStockHistory history = getHistoryById(historyId);

        // 로그인 유저 조회
        User user = getLoginUser(principal);

        // 권한 체크
        if (user.getUserType() == Role.FRANCHISEE) {
            Franchisee franchisee = getFranchiseeByPrincipal(principal);

            if (!history.getFranchiseId().getFranchiseId()
                    .equals(franchisee.getFranchise().getFranchiseId())) {
                throw new IllegalArgumentException("본인의 신청 내역이 아닙니다.");
            }

        } else if (user.getUserType() == Role.HEADQUARTER) {
            if (!history.getFranchiseId().getHeadquarterId().getHeadquarterId()
                    .equals(user.getHeadquarterId().getHeadquarterId())) {
                throw new IllegalArgumentException("해당 본사의 가맹점이 아닙니다.");
            }

        } else {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        // 권한 체크

        // 상태 검사
        if (history.getHistoryStatus() != HistoryStatus.REQUESTED &&
                history.getQuantity().compareTo(dto.getQuantity()) != 0) {
            throw new IllegalStateException("수량을 변경할 수 없습니다.");
        }

        if (history.getHistoryStatus() == HistoryStatus.DELIVERED ||
                history.getHistoryStatus() == HistoryStatus.CANCELLED) {
            throw new IllegalStateException("배송 완료 또는 취소된 신청은 수정할 수 없습니다.");
        }

        Franchise franchise = history.getFranchiseId();
        Ingredient oldIngredient = history.getIngredientId();
        Ingredient newIngredient = getIngredient(dto.getIngredientId());

        BigDecimal oldQuantity = history.getQuantity();
        BigDecimal newQuantity = dto.getQuantity();

        HeadquarterStock oldStock = getHeadquarterStock(franchise.getHeadquarterId().getHeadquarterId(), oldIngredient.getIngredientId());
        if (oldStock != null) {
            oldStock.addQuantity(oldQuantity);
        }

        HeadquarterStock newStock = getHeadquarterStock(franchise.getHeadquarterId().getHeadquarterId(), newIngredient.getIngredientId());
        if (newStock == null) {
            throw new IllegalArgumentException("수정하려는 재료가 본사 재고에 없습니다.");
        }
        validateStockQuantity(newStock.getQuantity(), newQuantity);
        newStock.subtractQuantity(newQuantity);

        if (history.getHistoryStatus() != HistoryStatus.DELIVERED &&
                dto.getStatus() == HistoryStatus.DELIVERED) {

            FranchiseStock newFranchiseStock = franchiseStockRepository
                    .findByFranchiseIdAndIngredientId(franchise, newIngredient)
                    .orElseGet(() -> franchiseStockRepository.save(
                            FranchiseStock.createFranchiseStock(franchise, newIngredient, dto)
                    ));
            newFranchiseStock.addQuantity(newQuantity);
        }

        if (dto.getStatus() == HistoryStatus.CANCELLED) {
            HeadquarterStock cancelledStock = getHeadquarterStock(franchise.getHeadquarterId().getHeadquarterId(), newIngredient.getIngredientId());
            cancelledStock.addQuantity(newQuantity);
        }

        if (user.getUserType() == Role.FRANCHISEE) {
            history.updateHistory(newQuantity, dto.getStatus());
        } else if (user.getUserType() == Role.HEADQUARTER) {
            history.updateHistoryByHeadquarter(newQuantity, dto.getStatus());
        }

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
                .orElse(null);
    }

    private FranchiseStock getFranchiseStock(Franchise franchise, Ingredient ingredient) {
        return franchiseStockRepository.findByFranchiseIdAndIngredientId(franchise, ingredient)
                .orElseThrow(() -> new IllegalArgumentException("해당 가맹점에 재고가 없습니다."));
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

    private void validateFranchiseBelongsToUser(Franchise franchise, User user) {
        if (!Objects.equals(user.getHeadquarterId(), franchise.getHeadquarterId())) {
            throw new IllegalArgumentException("해당 본사에 있는 가맹점이 아닙니다");
        }
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