package com.beyond.Team3.bonbon.menu.repository;

import com.beyond.Team3.bonbon.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuRepositoryCustom {
    Page<Menu> findAllMenu(Pageable pageable, Long headquarterId, String search);
}
