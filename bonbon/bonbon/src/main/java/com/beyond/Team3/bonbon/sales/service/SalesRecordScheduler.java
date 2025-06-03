package com.beyond.Team3.bonbon.sales.service;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.handler.exception.FranchiseException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.sales.dto.DailyTotalDto;
import com.beyond.Team3.bonbon.sales.entity.SalesRecord;
import com.beyond.Team3.bonbon.sales.repository.SalesDetailRepository;
import com.beyond.Team3.bonbon.sales.repository.SalesRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SalesRecordScheduler {

    private final SalesDetailRepository salesDetailRepository;
    private final SalesRecordRepository salesRecordRepository;
    private final FranchiseRepository franchiseRepository;

    // 매일 자정에 어제 기준으로 Sales_record 업데이트
    @Scheduled(cron = "0 0 0 * * *")
    // 테스트용 : 1분마다 업데이트
//    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void updateSalesRecord() {
        LocalDate date = LocalDate.now().minusDays(1);

        // 어제 날짜 가맹점 일 매출 조회
        List<DailyTotalDto> total = salesDetailRepository.findDailyTotalsByDate(date);

        // SalesRecord 없으면 생성 및 업데이트
        for(DailyTotalDto dto : total) {
            Franchise franchise = franchiseRepository.findByFranchiseId(dto.getFranchiseId())
                    .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));


            SalesRecord salesRecord = salesRecordRepository
                    .findByFranchiseAndSalesDate(franchise, date)
                    .orElseGet(() -> {
                        SalesRecord result = SalesRecord.builder()
                                .franchise(franchise)
                                .salesDate(date)
                                .salesAmount(dto.getTotalAmount())
                                .build();
                        return salesRecordRepository.save(result);
                    });
            salesRecord.updateSalesAmount(dto.getTotalAmount());
            salesRecordRepository.save(salesRecord);
        }

    }


//    // DDL 생성 후 한번만
//    @Scheduled(initialDelay = 0, fixedDelay = 60_000)
//    @Transactional
//    public void loadFiveYearsByLoop() {
//        LocalDate start = LocalDate.now().minusYears(4);
//        LocalDate end   = LocalDate.now().minusDays(1);
//
//        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
//
//            LocalDate targetDate = d;
//
//            List<DailyTotalDto> totals = salesDetailRepository.findDailyTotalsByDate(targetDate);
//            for (DailyTotalDto dto : totals) {
//                Franchise franchise = franchiseRepository.findByFranchiseId(dto.getFranchiseId())
//                        .orElseThrow(() -> new FranchiseException(ExceptionMessage.FRANCHISE_NOT_FOUND));
//
//                SalesRecord record = salesRecordRepository
//                        .findByFranchiseAndSalesDate(franchise, targetDate)
//                        .orElseGet(() -> salesRecordRepository.save(
//                                SalesRecord.builder()
//                                        .franchise(franchise)
//                                        .salesDate(targetDate)
//                                        .salesAmount(dto.getTotalAmount())
//                                        .build()
//                        ));
//
//                record.updateSalesAmount(dto.getTotalAmount());
//                salesRecordRepository.save(record);
//            }
//        }
//    }
}
