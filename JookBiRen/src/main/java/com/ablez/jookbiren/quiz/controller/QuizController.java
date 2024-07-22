package com.ablez.jookbiren.quiz.controller;

import com.ablez.jookbiren.dto.JookBiRenDto.Quiz;
import com.ablez.jookbiren.quiz.service.QuizService;
import com.ablez.jookbiren.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/quizzes")
public class QuizController {
    private final QuizService quizService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity getCurrentSituationAndSolvedProblems(@RequestHeader("Authorization") String accessToken,
                                                               int place) {
        return new ResponseEntity(
                quizService.getCurrentSituationAndSolvedProblems(place, userService.findCurrentUser(accessToken)),
                HttpStatus.OK);
    }

    @GetMapping("/answer")
    public ResponseEntity checkAlreadySolvedQuiz(@RequestHeader("Authorization") String accessToken, String quiz) {
        return new ResponseEntity(
                quizService.checkAlreadySolvedQuiz(userService.findCurrentUser(accessToken), new Quiz(quiz)),
                HttpStatus.OK);
    }

    @GetMapping("/hint")
    public ResponseEntity getHint(@RequestHeader("Authorization") String accessToken, String quiz) {
        return new ResponseEntity(quizService.findHint(userService.findCurrentUser(accessToken), new Quiz(quiz)),
                HttpStatus.OK);
    }
}
