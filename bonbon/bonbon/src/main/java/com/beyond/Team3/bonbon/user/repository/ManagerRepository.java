package com.beyond.Team3.bonbon.user.repository;

import com.beyond.Team3.bonbon.franchise.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
