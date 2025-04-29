package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.auth.dto.JwtToken;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
import com.beyond.Team3.bonbon.user.dto.UserModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserRegisterDto;
import com.beyond.Team3.bonbon.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface UserService {

    void join(UserRegisterDto userRegisterDto, Principal principal);

    UserInfoDto getUser(Principal principal);

    void modifyPassword(Principal principal, PasswordModifyDto passwordModifyDto);

    void update(Principal principal, UserModifyDto userModifyDto);

    Page<UserInfoDto> getAccounts(int page, int size, Principal principal);
}
