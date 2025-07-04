package com.beyond.Team3.bonbon.franchiseMenu.service;

import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.franchiseMenu.dto.FranchiseMenuRequestDto;
import com.beyond.Team3.bonbon.franchiseMenu.dto.FranchiseMenuResponseDto;
import com.beyond.Team3.bonbon.franchiseMenu.dto.FranchiseSimpleResponseDto;
import com.beyond.Team3.bonbon.franchiseMenu.entity.FranchiseMenu;
import com.beyond.Team3.bonbon.franchiseMenu.entity.FranchiseMenuId;
import com.beyond.Team3.bonbon.franchiseMenu.repository.FranchiseMenuRepository;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menu.repository.MenuRepository;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.FranchiseeRepository;
import com.beyond.Team3.bonbon.user.repository.ManagerRepository;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.beyond.Team3.bonbon.handler.message.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FranchiseMenuService {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final ManagerRepository managerRepository;
    private final FranchiseRepository franchiseRepository;
    private final FranchiseeRepository franchiseeRepository;
    private final FranchiseMenuRepository franchiseMenuRepository;

    @Transactional(readOnly = true)
    public FranchiseMenuResponseDto getMenuByFranchise(Principal principal, Long menuId) {
        Franchise franchise = getFranchiseByPrincipal(principal);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 없습니다."));
        ;
        if (!franchiseMenuRepository.existsByFranchiseIdAndMenuId(franchise, menu)) {
            throw new IllegalArgumentException("해당 가맹점은 이 메뉴를 판매하지 않습니다.");
        }

        return FranchiseMenuResponseDto.from(menu);
    }

    @Transactional(readOnly = true)
    public List<FranchiseMenuResponseDto> findMenusByCategory(Principal principal, Long categoryId) {
        Franchise franchise = getFranchiseByPrincipal(principal);

        // 2. 해당 가맹점이 가지고 있는 메뉴들 중, 메뉴의 카테고리가 일치하는 것만 필터링
        List<Menu> menus = franchiseMenuRepository.findByFranchiseAndCategory(franchise.getFranchiseId(), categoryId);

        // 3. DTO 변환
        return menus.stream()
                .map(FranchiseMenuResponseDto::from)
                .collect(Collectors.toList());
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
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 없습니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 없습니다"));

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

    @Transactional(readOnly = true)
    public List<FranchiseSimpleResponseDto> getFranchisesByMenu(Principal principal, Long menuId) {
        User user = getLoginUser(principal);

        List<FranchiseMenu> franchiseMenus = franchiseMenuRepository.findByMenuId_MenuId(menuId);

        // 매니저인 경우 지역코드로 필터링
        if (user.getUserType() == Role.MANAGER) {
            Manager manager = managerRepository.findByUserId(user)
                    .orElseThrow(() -> new IllegalStateException("매니저 정보가 없습니다."));
            int managerRegionCode = manager.getRegionCode().getRegionCode();

            franchiseMenus = franchiseMenus.stream()
                    .filter(fm -> fm.getFranchiseId().getRegionCode().getRegionCode() == managerRegionCode)
                    .toList();
        }

        return franchiseMenus.stream()
                .map(fm -> FranchiseSimpleResponseDto.from(fm.getFranchiseId()))
                .toList();
    }
}