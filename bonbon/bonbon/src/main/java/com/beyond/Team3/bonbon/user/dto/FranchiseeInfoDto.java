package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FranchiseeInfoDto extends UserInfo {

    private Long franchiseeId;

    private Long franchiseId;

    private String franchiseName;

    public FranchiseeInfoDto(User user) {
        super(user);
    }

    public FranchiseeInfoDto(Franchisee franchisee) {
        super(franchisee.getUserId());
        this.franchiseeId = franchisee.getFranchiseeId();
        if(franchisee.getFranchise() != null){
            this.franchiseId = franchisee.getFranchise().getFranchiseId();
            this.franchiseName = franchisee.getFranchise().getName();
        }
    }
}
