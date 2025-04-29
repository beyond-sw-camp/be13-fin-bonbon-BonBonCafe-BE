package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.user.entity.User;
import lombok.Data;

@Data
public class UserInfoDto {

    private String email;

//    private String password; // 비밀번호

    private String name;

    private Role role;

    private String phone; // 전화번호

    public UserInfoDto(User user) {
        this.email = user.getEmail();
//        this.password = user.getPassword();
        this.name = user.getName();
        this.role = user.getUserType();
        this.phone = user.getPhone();
    }
}
