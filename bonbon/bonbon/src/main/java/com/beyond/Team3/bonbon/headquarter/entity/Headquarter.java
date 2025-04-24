package com.beyond.Team3.bonbon.headquarter.entity;


import com.beyond.Team3.bonbon.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "headquarter")
public class Headquarter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long headquarterId;

    private String name;

    private String email;

    private String password;

    private String headquarterTel;

    private String roadAddress;

    private String detailAddress;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;
}
