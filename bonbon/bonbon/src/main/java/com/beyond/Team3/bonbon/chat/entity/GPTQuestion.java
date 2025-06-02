package com.beyond.Team3.bonbon.chat.entity;

import com.beyond.Team3.bonbon.common.base.EntityDate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "gpt_question")
public class GPTQuestion extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "gpt_question", length = 1500, nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private GPTAnswer answer;

    public GPTQuestion(String question, GPTAnswer answer) {
        this.question = question;
        this.answer = answer;
    }
}