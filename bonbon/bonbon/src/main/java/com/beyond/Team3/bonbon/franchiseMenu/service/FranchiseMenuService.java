package com.beyond.Team3.bonbon.franchiseMenu.service;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.franchiseMenu.dto.FranchiseMenuRequestDto;
import com.beyond.Team3.bonbon.franchiseMenu.dto.FranchiseMenuResponseDto;
import com.beyond.Team3.bonbon.franchiseMenu.entity.FranchiseMenu;
import com.beyond.Team3.bonbon.franchiseMenu.entity.FranchiseMenuId;
import com.beyond.Team3.bonbon.franchiseMenu.repository.FranchiseMenuRepository;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menu.repository.MenuRepository;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.FranchiseeRepository;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static com.beyond.Team3.bonbon.handler.message.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FranchiseMenuService {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final FranchiseRepository franchiseRepository;
    private final FranchiseeRepository franchiseeRepository;
    private final FranchiseMenuRepository franchiseMenuRepository;

    public FranchiseMenuResponseDto getMenuByFranchise(Principal principal, Long menuId) {
        Franchise franchise = getFranchiseByPrincipal(principal);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        if (!franchiseMenuRepository.existsByFranchiseIdAndMenuId(franchise, menu)) {
            throw new IllegalArgumentException("해당 가맹점은 이 메뉴를 판매하지 않습니다.");
        }

        return FranchiseMenuResponseDto.from(menu);
    }

    public Page<MenuResponseDto> getAllMenusByFranchise(Pageable pageable, Principal principal) {
        Franchise franchise = getFranchiseByPrincipal(principal);
        Page<Menu> menus = menuRepository.findAllByFranchise(pageable, franchise.getFranchiseId());
        return menus.map(MenuResponseDto::from);
    }

    public Page<MenuResponseDto> getMenusByFranchise(Pageable pageable, Principal principal, Long franchiseId) {
        User user = getLoginUser(principal);

        Franchise franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가맹점이 존재하지 않습니다."));

        if (!user.getHeadquarterId().getHeadquarterId().equals(franchise.getHeadquarterId().getHeadquarterId())) {
            throw new IllegalArgumentException("해당 본사의 가맹점이 아닙니다.");
        }

        Page<Menu> menus = menuRepository.findAllByFranchise(pageable, franchiseId);
        return menus.map(MenuResponseDto::from);
    }

    @Transactional
    public FranchiseMenuResponseDto create(Principal principal, FranchiseMenuRequestDto dto) {
        Franchise franchise = getFranchiseByPrincipal(principal);

        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        if (menu.getHeadquarter() == null || !menu.getHeadquarter().equals(franchise.getHeadquarterId())) {
            throw new IllegalArgumentException("해당 본사의 메뉴가 아닙니다");
        }

        if (franchiseMenuRepository.existsByFranchiseIdAndMenuId(franchise, menu)) {
            throw new IllegalArgumentException("이미 등록된 메뉴입니다.");
        }

        FranchiseMenu franchiseMenu = FranchiseMenu.create(franchise, menu);
        franchiseMenuRepository.save(franchiseMenu);

        return FranchiseMenuResponseDto.from(menu);
    }

    @Transactional
    public void delete(Principal principal, FranchiseMenuRequestDto dto) {
        Franchise franchise = getFranchiseByPrincipal(principal);

        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        FranchiseMenuId id = new FranchiseMenuId(franchise.getFranchiseId(), menu.getMenuId());

        FranchiseMenu franchiseMenu = franchiseMenuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴는 가맹점에서 등록되지 않았습니다."));

        franchiseMenuRepository.delete(franchiseMenu);
    }

    private User getLoginUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private Franchise getFranchiseByPrincipal(Principal principal) {
        User user = getLoginUser(principal);
        return franchiseeRepository.findByUserId(user)
                .orElseThrow(() -> new IllegalArgumentException("가맹점 정보 없음"))
                .getFranchise();
    }
}