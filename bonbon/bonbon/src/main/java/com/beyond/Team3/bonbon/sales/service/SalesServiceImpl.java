package com.beyond.Team3.bonbon.sales.service;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.handler.exception.FranchiseException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.entity.SalesDetail;
import com.beyond.Team3.bonbon.sales.entity.SalesRecord;
import com.beyond.Team3.bonbon.sales.repository.SalesDetailRepository;
import com.beyond.Team3.bonbon.sales.repository.SalesRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService{

    private final SalesRecordRepository salesRecordRepository;
    private final SalesDetailRepository salesDetailRepository;
    private final FranchiseRepository franchiseRepository;

    @Override
    @Transactional
    public DailySalesDto getDailySales(Long franchiseId, LocalDate salesDate) {

        // 가맹점 확인
        Franchise franchise = franchiseRepository.findByFranchiseId(franchiseId)
                .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));

        // 만약 SalesRecord 없으면 생성
        // 가맹점 등록하면 SalesRecord 생성할 수 있도록 추후에 수정
        SalesRecord salesRecord = salesRecordRepository.findByFranchiseAndSalesDate(franchise, salesDate)
                .orElse(null);

        if(salesRecord == null) {
            salesRecord = SalesRecord.builder()
                    .franchise(franchise)
                    .salesDate(salesDate)
                    .salesAmount(0) // 기본 매출 0
                    .build();
            salesRecord = salesRecordRepository.save(salesRecord);
        }

        //SalesDetail 모든 판매 조회
        List<SalesDetail> salesDetails = salesDetailRepository.findBySalesRecordAndSalesDate(salesRecord,salesDate);

        // SalesDetail 리스트로 해서 amount 합산(totalAmount)
        int totalAmount = salesDetails.stream()
                .mapToInt(SalesDetail::getAmount)
                .sum();

        salesRecord.updateSalesAmount(totalAmount);
        salesRecordRepository.save(salesRecord);

        return new DailySalesDto(salesDate, totalAmount);
    }
}
