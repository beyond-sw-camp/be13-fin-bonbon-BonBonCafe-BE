package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.common.enums.AccountStatus;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserCleanUpScheduler {

    private final UserRepository userRepository;

    // 매일 자정, 3일 전에 삭제로 바뀐 사람들 확인
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteUser(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deletedAt = now.minusDays(3);

        List<User> userToDel = userRepository.findByStatusAndDeletedAtBefore(AccountStatus.DELETED, deletedAt);

        userRepository.deleteAll(userToDel);
    }
}
