package com.beyond.Team3.bonbon.config;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

// 스레드 풀 커스터마이징
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 스프링 스케줄링 전용 스레드 풀 생성
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        // 최대 5개 스레드 동시 사용 가능
        threadPoolTaskScheduler.setPoolSize(5);
        // 생성 스레드가 속할 스레드 그룹 이름 지정
        threadPoolTaskScheduler.setThreadGroupName("bonbon scheduler thread pool");
        // 생성 스레드 이름 앞에 붙는 글자
        threadPoolTaskScheduler.setThreadNamePrefix("bonbon-scheduler-");
        // 스레드 풀 초기화
        threadPoolTaskScheduler.initialize();
        // 스프링한테 커스텀 스케줄러로 대체하라고 등록
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
