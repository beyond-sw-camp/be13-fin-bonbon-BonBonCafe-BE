package com.beyond.Team3.bonbon.menu.repository;

import com.beyond.Team3.bonbon.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {
    Page<Menu> findAllMenu(Pageable pageable, Long headquarterId, String search);

    List<Menu> findMenusByCategoryAndHeadquarter(Long categoryId, Long headquarterId);
}
