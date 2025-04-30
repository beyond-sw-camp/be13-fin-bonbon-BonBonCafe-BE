package com.beyond.Team3.bonbon.menuDetail.repository;

import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menuDetail.entity.MenuDetail;
import com.beyond.Team3.bonbon.menuDetail.entity.MenuDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDetailRepository extends JpaRepository<MenuDetail, MenuDetailId> {
    void deleteByMenu(Menu menu);
}
