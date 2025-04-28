package com.beyond.Team3.bonbon.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "password")     // 비밀번호 등의 민감한 정보는 ToString에서 제외
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @Email(message = "유효한 이메일 형식을 입력해주세요.")
    @NotBlank(message = "이메일 입력은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    private String password;
}
