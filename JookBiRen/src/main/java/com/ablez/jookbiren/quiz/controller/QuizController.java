package com.ablez.jookbiren.quiz.controller;

import com.ablez.jookbiren.quiz.service.QuizService;
import com.ablez.jookbiren.user.entity.UserEp01;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/quizzes")
public class QuizController {
    private final QuizService quizService;

    @GetMapping
    public ResponseEntity getCurrentSituationAndSolvedProblems() {
        return new ResponseEntity(quizService.getCurrentSituationAndSolvedProblems(new UserEp01(1L, "abc")),
                HttpStatus.OK);
    }
}
