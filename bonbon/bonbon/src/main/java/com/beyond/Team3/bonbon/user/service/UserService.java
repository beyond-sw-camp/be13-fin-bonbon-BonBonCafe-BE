package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.auth.dto.JwtToken;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
import com.beyond.Team3.bonbon.user.dto.UserRegisterDto;
import com.beyond.Team3.bonbon.user.entity.User;

import java.security.Principal;

public interface UserService {

    void join(UserRegisterDto userRegisterDto);

    UserInfoDto getUser(Principal principal);

    void modifyPassword(Principal principal, PasswordModifyDto passwordModifyDto);
}
