package com.ablez.jookbiren.quiz.repository;

import com.ablez.jookbiren.quiz.entity.WrongAnswerEp01;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WrongAnswerRepository extends JpaRepository<WrongAnswerEp01, Long> {
}
