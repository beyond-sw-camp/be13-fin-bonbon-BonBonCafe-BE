package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseResponseDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseeRegisterDto;
import com.beyond.Team3.bonbon.user.dto.ManagerInfoDto;
import com.beyond.Team3.bonbon.user.dto.ManagerRegisterDto;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfo;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
import com.beyond.Team3.bonbon.user.dto.UserModifyDto;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface UserService {

    void joinManager(ManagerRegisterDto managerRegisterDto, Principal principal);

    void joinFranchisee(FranchiseeRegisterDto franchiseeRegisterDto, Principal principal);

    UserInfo getUser(Principal principal);

    void modifyPassword(Principal principal, PasswordModifyDto passwordModifyDto);

    void update(Principal principal, UserModifyDto userModifyDto);

    void registUserUpdate(Long userId, UserModifyDto userModifyDto, Principal principal);

    UserInfo getAccountDetail(Long userId, Principal principal);

    Page<UserInfoDto> getAccountsByRole(int page, int size, Role role, Principal principal);

    ManagerInfoDto getManagerDetail(Long userId, Principal principal);

    ManagerInfoDto getFranchiseeDetail(Long userId, Principal principal);

    void deleteFranchisee(Long userId, Principal principal);

    void deleteManager(Long userId, Principal principal);

//    List<FranchiseResponseDto> findFranchiseWithoutOwner();
}
