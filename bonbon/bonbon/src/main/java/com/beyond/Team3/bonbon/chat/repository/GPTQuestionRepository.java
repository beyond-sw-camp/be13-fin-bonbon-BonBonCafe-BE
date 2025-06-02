package com.beyond.Team3.bonbon.chat.repository;

import com.beyond.Team3.bonbon.chat.entity.GPTQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GPTQuestionRepository extends JpaRepository<GPTQuestion, Integer> {

}