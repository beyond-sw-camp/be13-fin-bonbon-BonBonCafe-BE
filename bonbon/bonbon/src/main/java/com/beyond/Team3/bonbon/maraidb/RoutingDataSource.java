package com.beyond.Team3.bonbon.maraidb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

//@Slf4j
//public class RoutingDataSource extends AbstractRoutingDataSource {
//    @Override
//    protected Object determineCurrentLookupKey() {
//        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
//        String dbType = isReadOnly ? "slave" : "master";
//        log.info("🛠 Routing to [{}] database", dbType);  // <-- 이 줄 추가
//
//        // 현재 Transaction이 읽기 전용인 경우 slave, 아닌 경우 master를 반환
//        return isReadOnly ? "slave" : "master";
//    }
//}
