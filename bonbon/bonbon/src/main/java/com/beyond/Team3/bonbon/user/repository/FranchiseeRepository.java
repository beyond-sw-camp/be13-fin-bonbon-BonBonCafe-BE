package com.beyond.Team3.bonbon.user.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseeRepository extends JpaRepository<Franchisee, Long> {
}
