package com.beyond.Team3.bonbon.user.repository;

import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.region.entity.Region;
import com.beyond.Team3.bonbon.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByUserId(User userId);

    void deleteByUserId(User userId);

    Manager findByRegionCode(Region region);
}
