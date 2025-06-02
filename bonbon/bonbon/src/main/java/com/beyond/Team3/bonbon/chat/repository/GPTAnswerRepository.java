package com.beyond.Team3.bonbon.chat.repository;

import com.beyond.Team3.bonbon.chat.entity.GPTAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GPTAnswerRepository extends JpaRepository<GPTAnswer, Integer> {
}