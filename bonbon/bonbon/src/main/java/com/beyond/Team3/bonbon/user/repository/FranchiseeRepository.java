package com.beyond.Team3.bonbon.user.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FranchiseeRepository extends JpaRepository<Franchisee, Long> {

    void deleteByUserId(User userId);

    Optional<Franchisee> findByUserId(User userId);

    void deleteByFranchiseeId(Long franchiseeId);
}
