package com.beyond.Team3.bonbon.user.repository;

import com.beyond.Team3.bonbon.common.enums.AccountStatus;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<User> findByParentIdAndUserType(User parentId, Role userType, Pageable pageable);

    List<User> findByStatusAndDeletedAtBefore(AccountStatus status, LocalDateTime deletedAtBefore);

    Optional<User> findByUserId(Long userId);

    boolean existsByEmail(String email);

    Optional<User> findByUserTypeAndHeadquarterId(Role userType, Headquarter headquarterId);

    List<User> findAllByUserTypeAndHeadquarterId(Role role, Headquarter headquarter);
}
