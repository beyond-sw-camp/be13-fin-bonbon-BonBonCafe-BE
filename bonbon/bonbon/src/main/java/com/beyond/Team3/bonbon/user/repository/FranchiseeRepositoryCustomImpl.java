package com.beyond.Team3.bonbon.user.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FranchiseeRepositoryCustomImpl implements FranchiseeRepositoryCustom {

    @Override
    public Page<Franchisee> findAll(Pageable pageable, String search, Long headquarterId) {
        return null;
    }
}
