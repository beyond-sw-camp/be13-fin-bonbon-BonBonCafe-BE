package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.common.enums.AccountStatus;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public abstract class UserRegisterDto {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password; // 비밀번호

    @NotBlank(message = "비밀번호를 확인은 필수입니다.")
    private String passwordConfirm; // 비밀번호

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    private String phone; // 전화번호

    public Boolean passwordMatching(){
        return this.password.equals(this.passwordConfirm);
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .status(AccountStatus.ACTIVE)
                .phone(phone)
                .build();
    }

}
