package com.ablez.jookbiren.quiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class HintEp01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hintId;
    @Column(nullable = false)
    private String hint;

    @OneToOne
    @JoinColumn(name = "quiz_id")
    private QuizEp01 quiz;
}
