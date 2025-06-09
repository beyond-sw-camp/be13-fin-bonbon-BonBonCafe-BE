package com.beyond.Team3.bonbon.sales.service;

import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseDto;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.handler.exception.FranchiseException;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.dto.MenuRankingDto;
import com.beyond.Team3.bonbon.sales.dto.SalesRankingDto;
import com.beyond.Team3.bonbon.sales.entity.SalesDetail;
import com.beyond.Team3.bonbon.sales.entity.SalesRecord;
import com.beyond.Team3.bonbon.sales.repository.SalesDetailRepository;
import com.beyond.Team3.bonbon.sales.repository.SalesRecordRepository;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {

    private final SalesRecordRepository salesRecordRepository;
    private final SalesDetailRepository salesDetailRepository;
    private final FranchiseRepository franchiseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DailySalesDto getDailySales(Long franchiseId, LocalDate salesDate) {

        // 현재 날짜보다 이상으로 조회할 수 없게 예외처리
        if(salesDate.isAfter(LocalDate.now()))
            throw new FranchiseException(ExceptionMessage.NO_SALES_RECORDS);

        // 가맹점 확인
        Franchise franchise = franchiseRepository.findByFranchiseId(franchiseId)
                .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));

        //SalesDetail 모든 판매 조회
        List<SalesDetail> salesDetails = salesDetailRepository
                .findBySalesDetailId_FranchiseIdAndSalesDetailId_SalesDate(franchiseId, salesDate);

        // SalesDetail 리스트로 해서 amount 합산(totalAmount)
        int totalAmount = salesDetails.stream()
                .mapToInt(SalesDetail::getAmount)
                .sum();

        // 만약 SalesRecord 없으면 생성 및 매출 업데이트
        SalesRecord salesRecord = salesRecordRepository
                .findByFranchiseAndSalesDate(franchise, salesDate)
                .orElseGet(() -> {
                    SalesRecord result = SalesRecord.builder()
                            .franchise(franchise)
                            .salesDate(salesDate)
                            .salesAmount(0)
                            .build();
                    return salesRecordRepository.save(result);
                });

        salesRecord.updateSalesAmount(totalAmount);
        salesRecordRepository.save(salesRecord);

        return new DailySalesDto(salesDate, totalAmount);
    }

    @Override
    @Transactional
    public List<DailySalesDto> getPeriodSales(Long franchiseId, LocalDate startDate, LocalDate endDate) {

        // 기간 설정 예외처리
        if(startDate.isAfter(endDate)) {
            throw new FranchiseException(ExceptionMessage.INVALID_DATE_RANGE);
        }
        long diffMonths = ChronoUnit.MONTHS.between(startDate, endDate);
        if (diffMonths > 2) {
            throw new FranchiseException(ExceptionMessage.INVALID_MONTH);
        }

        // 현재 날짜보다 이상으로 조회할 수 없게 예외처리
        if(endDate.isAfter(LocalDate.now()))
            throw new FranchiseException(ExceptionMessage.NO_SALES_RECORDS);

        // getDailySales 서비스 로직 호출
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            getDailySales(franchiseId, date);
        }

        return salesRecordRepository.getDailySalesByPeriod(franchiseId, startDate, endDate);

    }


    @Override
    public Page<SalesRankingDto> getFranchiseRanking(Principal principal, int regionCode, Integer year, Integer month, int page, int size) {
        // 해당 사용자 확인
        userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);
        Page<SalesRankingDto> result = salesRecordRepository.getFranchiseRanking(regionCode, year, month, pageable);

        if(result.isEmpty()) {
            throw new FranchiseException(ExceptionMessage.NO_SALES_RECORDS);
        }

        return result;
    }

    @Override
    public List<MenuRankingDto> getMenuSalesRanking(Principal principal, Long franchiseId, LocalDate startDate, LocalDate endDate) {
        // 해당 사용자 확인
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        // 기간 설정 예외처리
        if(startDate.isAfter(endDate)) {
            throw new FranchiseException(ExceptionMessage.INVALID_DATE_RANGE);
        }

        // 본사, 매니저만 접근 가능하게(매니저도 자기 지역만 조회하도록 해야 하나?)
//        if (user.getUserType() == Role.FRANCHISEE) {
//            throw new UserException(ExceptionMessage.INVALID_USER_ROLE);
//        }
        // 가맹점 확인
        franchiseRepository.findByFranchiseId(franchiseId)
                .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));

        return salesDetailRepository.findMenuRanking(franchiseId, startDate, endDate, 7);
    }

    @Override
    public List<DailySalesDto> getHistory(Long franchiseId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new FranchiseException(ExceptionMessage.INVALID_DATE_RANGE);
        }

        return salesRecordRepository.getDailySalesByPeriod(franchiseId, startDate, endDate);
    }

    @Override
    public List<DailySalesDto> getAllFranchisePeriodSales(LocalDate startDate, LocalDate endDate) {
        return salesRecordRepository.findAllFranchiseDailySalesByPeriod(startDate, endDate);
    }

    @Override
    public List<MenuRankingDto> getAllMenuSalesRanking(LocalDate startDate, LocalDate endDate) {
        return salesDetailRepository.findAllMenuRanking(startDate, endDate, 7);
    }

    @Override
    public List<SalesRankingDto> getAllFranchiseRanking(LocalDate startDate, LocalDate endDate, int limit) {
        return salesRecordRepository.findAllFranchiseRanking(startDate, endDate, 10);
    }

}