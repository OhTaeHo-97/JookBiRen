package com.ablez.jookbiren.quiz.entity;

import com.ablez.jookbiren.user.entity.UserEp01;
import java.time.LocalDateTime;
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
public class Quiz0Ep01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quiz0Id;
    private Integer quizNumber;
    private LocalDateTime firstAccessTime;
    private LocalDateTime firstAnswerTie;
    private LocalDateTime getHintTime;
    private LocalDateTime getAnswerTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEp01 userId;
}
