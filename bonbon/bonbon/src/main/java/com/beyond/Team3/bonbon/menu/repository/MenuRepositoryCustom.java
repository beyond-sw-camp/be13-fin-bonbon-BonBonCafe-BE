package com.beyond.Team3.bonbon.menu.repository;

import com.beyond.Team3.bonbon.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuRepositoryCustom {
    Page<Menu> findAllMenu(Pageable pageable, Long headquarterId, String search);

    List<Menu> findMenusByCategoryAndHeadquarter(Long categoryId, Long headquarterId);

    Page<Menu> findAllByFranchise(Pageable pageable, Long franchiseId);
}
