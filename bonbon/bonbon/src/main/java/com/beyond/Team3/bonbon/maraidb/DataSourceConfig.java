package com.beyond.Team3.bonbon.maraidb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@Slf4j
@EnableTransactionManagement
@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = {
                "com.beyond.Team3.bonbon.franchise.repository",
                "com.beyond.Team3.bonbon.franchiseStock.repository",
                "com.beyond.Team3.bonbon.franchiseMenu.repository",
                "com.beyond.Team3.bonbon.franchiseStockHistory.repository",
                "com.beyond.Team3.bonbon.headquarter.repository",
                "com.beyond.Team3.bonbon.headquaterStock.repository",
                "com.beyond.Team3.bonbon.ingredient.repository",
                "com.beyond.Team3.bonbon.menu.repository",
                "com.beyond.Team3.bonbon.menuCategory.repository",
                "com.beyond.Team3.bonbon.menuDetail.repository",
                "com.beyond.Team3.bonbon.notice.repository",
                "com.beyond.Team3.bonbon.region.repository",
                "com.beyond.Team3.bonbon.sales.repository",
                "com.beyond.Team3.bonbon.user.repository"
        },
        entityManagerFactoryRef = "masterEntityManagerFactory",
        transactionManagerRef = "masterTransactionManager"
)
public class DataSourceConfig {

    private final MariaDBProperties mariaDBProperties;

    // Master 데이터베이스의 DataSource를 생성하는 빈
    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(mariaDBProperties.getMaster().getDriverClassName())
                .url(mariaDBProperties.getMaster().getUrl())
                .username(mariaDBProperties.getMaster().getUsername())
                .password(mariaDBProperties.getMaster().getPassword())
                .build();
    }

    // Slave 데이터베이스의 DataSource를 생성하는 빈
    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(mariaDBProperties.getSlave().getDriverClassName())
                .url(mariaDBProperties.getSlave().getUrl())
                .username(mariaDBProperties.getSlave().getUsername())
                .password(mariaDBProperties.getSlave().getPassword())
                .build();
    }


    // 라우팅 데이터베이스 생성 빈 설정
    @Bean
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("slaveDataSource") DataSource slaveDataSource) {

        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource);
        dataSourceMap.put("slave", slaveDataSource);

        Map<Object, Object> immutableDataSourceMap = Collections.unmodifiableMap(dataSourceMap);

        routingDataSource.setTargetDataSources(immutableDataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    // 라우팅 데이터베이스를 기본 DB로 설정하는 빈 설정
    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        // 연결이 불필요하게 일찍 만들어지지 않도록 -> 시스템 자원 절약, 성능 최적화
        return routingDataSource;
    }

    @Bean(name = "masterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(@Qualifier("dataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(dataSource);
        // JPA 엔티티 클래스가 포함된 패키지 설정
        factory.setPackagesToScan("com.beyond.Team3.bonbon");
        // JPA 벤더 어댑터
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        // Hibernate 속성을 설정하기 위해 별도의 properties 객체를 사용함
        factory.setJpaPropertyMap(hibernateProperties());

        return factory;
    }

    @Bean(name = "masterTransactionManager")
    public PlatformTransactionManager masterTransactionManager(
            LocalContainerEntityManagerFactoryBean masterEntityManagerFactory) {
        return new JpaTransactionManager(
                Objects.requireNonNull(masterEntityManagerFactory.getObject()));
    }

    private Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.use_sql_comments", true);
        properties.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        return properties;
    }

}
