package com.beyond.Team3.bonbon.user.repository;

import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.region.entity.Region;
import com.beyond.Team3.bonbon.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByUserId(User userId);

    void deleteByUserId(User userId);

    Optional<Manager> findByRegionCode(Region region);

    User userId(User userId);

    @Query("select m " +
            "from Manager m " +
            "left Join m.userId mu " +
            "where mu.parentId = :parentId " +
            "and (:name is null or mu.name like %:name%)")
    Page<Manager> findManagerFromHeadquarter(@Param("parentId") User parentId,
                                             Pageable pageable,
                                             @Param("name") String name);
}
