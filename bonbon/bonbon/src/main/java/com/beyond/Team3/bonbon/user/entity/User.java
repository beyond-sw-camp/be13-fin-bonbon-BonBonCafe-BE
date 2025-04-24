package com.beyond.Team3.bonbon.user.entity;

import com.beyond.Team3.bonbon.common.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@Entity
@AllArgsConstructor
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role userType;

    private String phone;

    private String name;
}
