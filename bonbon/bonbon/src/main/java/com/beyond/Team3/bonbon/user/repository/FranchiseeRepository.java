package com.beyond.Team3.bonbon.user.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FranchiseeRepository extends JpaRepository<Franchisee, Long> {

    void deleteByUserId(User userId);

    Optional<Franchisee> findByUserId(User userId);

    void deleteByFranchiseeId(Long franchiseeId);

    @Query("select f " +
            "from Franchisee f " +
            "left Join f.userId fu " +
            "where fu.parentId = :parentId")
    Page<Franchisee> findFranchiseesFromHeadquarter(@Param("parentId") User parentId, Pageable pageable);
}
