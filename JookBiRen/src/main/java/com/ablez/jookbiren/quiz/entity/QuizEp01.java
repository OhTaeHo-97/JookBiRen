package com.ablez.jookbiren.quiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class QuizEp01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;
    @Column(nullable = false)
    private Integer placeCode;
    @Column(nullable = false)
    private Integer quizNumber;
    @Column(nullable = false)
    private Integer quizCode;
}
