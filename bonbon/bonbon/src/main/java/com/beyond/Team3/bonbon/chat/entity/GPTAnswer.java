package com.beyond.Team3.bonbon.chat.entity;

import com.beyond.Team3.bonbon.common.base.EntityDate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "gpt_answer")
public class GPTAnswer extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "gpt_answer", length = 1500, nullable = false)
    private String answer;

    public GPTAnswer(String answer) {
        this.answer = answer;
    }
}