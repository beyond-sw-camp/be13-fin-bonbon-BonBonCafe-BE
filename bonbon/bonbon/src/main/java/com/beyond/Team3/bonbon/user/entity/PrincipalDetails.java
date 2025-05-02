package com.beyond.Team3.bonbon.user.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class PrincipalDetails implements UserDetails {

    private final User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // user 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        authorities.add((GrantedAuthority) () -> {
            return "ROLE_" + user.getUserType().name();   // ROLE_HEADQUARTER, ROLE_FRANCHISEE, ROLE_MANAGER
        });
        log.info("Authorities : {}", authorities.stream()
                .map(GrantedAuthority::getAuthority) // 권한의 문자열 값을 가져와서 로그에 표시
                .collect(Collectors.joining(", ")));
        return authorities;
    }

    // 사용자 비밀번호
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 사용자 이름 -> 이메일
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    // 계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    // 비밀번호 오래 썼는지 확인
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
