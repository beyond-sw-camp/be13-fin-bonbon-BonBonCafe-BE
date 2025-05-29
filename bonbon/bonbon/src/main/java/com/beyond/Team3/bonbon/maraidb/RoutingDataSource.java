//package com.beyond.Team3.bonbon.maraidb;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//
//@Slf4j
//public class RoutingDataSource extends AbstractRoutingDataSource {
//    @Override
//    protected Object determineCurrentLookupKey() {
//        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
//        log.info("🔍 Is transaction read-only? {}", isReadOnly); // 이 줄 추가
//        String dbType = isReadOnly ? "slave" : "master";
//
//        try {
//            // 현재 DataSource Map에서 선택된 DataSource를 가져옴
//            DataSource selectedDataSource = (DataSource) this.resolveSpecifiedDataSource(this.getResolvedDataSources().get(dbType));
//            try (Connection connection = selectedDataSource.getConnection()) {
//                DatabaseMetaData metaData = connection.getMetaData();
//                String dbUrl = metaData.getURL();  // 예: jdbc:mariadb://ec2-xxx.compute.amazonaws.com:3306/dbname
//                log.info("🛠 Routing to [{}] database on URL: {}", dbType, dbUrl);
//            }
//        } catch (Exception e) {
//            log.warn("⚠ Could not log DB URL for [{}] due to: {}", dbType, e.getMessage());
//        }
//
//        return dbType;
//    }
//}
