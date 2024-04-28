package com.ablez.jookbiren.quiz.entity;

import com.ablez.jookbiren.user.entity.UserEp01;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class WrongAnswerEp01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wrongAnswerId;
    private String answer;
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEp01 user;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private QuizEp01 quiz;
}
