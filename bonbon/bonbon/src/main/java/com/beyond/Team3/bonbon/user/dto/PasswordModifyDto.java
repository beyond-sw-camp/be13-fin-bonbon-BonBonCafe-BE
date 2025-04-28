package com.beyond.Team3.bonbon.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordModifyDto {
    @NotBlank(message = "현재 비밀번호를 입력하세요")
    private String oldPassword;
    @NotBlank(message = "새로운 비밀번호를 입력하세요")
    private String newPassword;
    @NotBlank(message = "새로운 비밀번호를 확인하세요")
    private String newPasswordConfirm;
}
