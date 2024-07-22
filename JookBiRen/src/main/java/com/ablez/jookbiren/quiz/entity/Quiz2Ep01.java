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
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class Quiz2Ep01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quiz2Id;
    private Integer quizNumber;
    @Setter
    private LocalDateTime firstAccessTime;
    @Setter
    private LocalDateTime firstAnswerTime;
    @Setter
    private LocalDateTime getHintTime;
    @Setter
    private LocalDateTime getAnswerTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEp01 userId;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private QuizEp01 quiz;

    public Quiz2Ep01(Integer quizNumber, UserEp01 userId, QuizEp01 quiz) {
        this.quizNumber = quizNumber;
        this.firstAccessTime = LocalDateTime.now();
        this.userId = userId;
        this.quiz = quiz;
    }
}
