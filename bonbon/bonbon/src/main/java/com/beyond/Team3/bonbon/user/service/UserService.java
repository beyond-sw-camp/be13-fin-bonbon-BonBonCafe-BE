package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.user.dto.FranchiseInfoDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseFilterDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseeInfoDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseeModifyDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseeRegisterDto;
import com.beyond.Team3.bonbon.user.dto.ManagerInfoDto;
import com.beyond.Team3.bonbon.user.dto.ManagerModifyDto;
import com.beyond.Team3.bonbon.user.dto.ManagerRegisterDto;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfo;
import com.beyond.Team3.bonbon.user.dto.UserModifyDto;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface UserService {

    void joinManager(ManagerRegisterDto managerRegisterDto);

    void joinFranchisee(FranchiseeRegisterDto franchiseeRegisterDto);

    UserInfo getUser(Principal principal);

    void modifyPassword(Principal principal, PasswordModifyDto passwordModifyDto);

    void update(Principal principal, UserModifyDto userModifyDto);

    void registUserUpdate(Long userId, UserModifyDto userModifyDto, Principal principal);

    UserInfo getAccountDetail(Long userId, Principal principal);

    Page<FranchiseeInfoDto> getFranchiseeAccounts(int page, int size, Role role, Principal principal, String name);

    Page<ManagerInfoDto> getManagerAccounts(int page, int size, Principal principal, String name);

    ManagerInfoDto getManagerDetail(Long userId, Principal principal);

    FranchiseeInfoDto getFranchiseeDetail(Long userId, Principal principal);

    void deleteFranchisee(Long userId, Principal principal);

    void deleteManager(Long userId, Principal principal);

    Page<FranchiseFilterDto> findFranchiseWithoutOwner(int page, int size, Long headquarterId, String name);

    Page<FranchiseFilterDto> getFranchiseInRegion(int regionCode, int page, int size, Principal principal, String name);

    void franchiseeUpdate(Long userId, FranchiseeModifyDto franchiseeModifyDto, Principal principal);

    void managerUpdate(Long managerId, ManagerModifyDto managerModifyDto, Principal principal);

    Page<FranchiseFilterDto> findFranchiseWithoutOwnerPrincipal(int page, int size, Principal principal, String name);
}
