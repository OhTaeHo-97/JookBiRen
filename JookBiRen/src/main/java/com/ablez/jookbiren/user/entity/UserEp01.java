package com.ablez.jookbiren.user.entity;

import java.time.LocalDateTime;
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
public class UserEp01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false)
    private String code;
    private Integer score = 0;
    private Integer answerStatusCode = 0;
    private Integer answerCount = 0;
    private Integer solvedQuizCount = 0;
    private Integer criminal = 0;
    private LocalDateTime answerTime;
    private LocalDateTime firstLoginTime;

    public UserEp01(Long userId, String code) {
        this.userId = userId;
        this.code = code;
    }

    public void updateCriminal(int criminal) {
        this.criminal = criminal;
    }
}
