package com.ablez.jookbiren.answer.controller;

import com.ablez.jookbiren.answer.dto.AnswerDto.CheckAnswerDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.SuspectDto;
import com.ablez.jookbiren.answer.service.AnswerService;
import com.ablez.jookbiren.dto.JookBiRenDto.Quiz;
import com.ablez.jookbiren.security.interceptor.JwtParseInterceptor;
import com.ablez.jookbiren.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/answers")
public class AnswerController {
    private final AnswerService answerService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity findAnswer(String quiz) {
        return new ResponseEntity<>(answerService.findAnswer(new Quiz(quiz),
                userService.findByUserId(Long.parseLong(JwtParseInterceptor.getAuthenticatedUsername()))),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity checkAnswer(@RequestBody CheckAnswerDto dto) {
        return new ResponseEntity<>(answerService.checkAnswer(dto,
                userService.findByUserId(Long.parseLong(JwtParseInterceptor.getAuthenticatedUsername()))),
                HttpStatus.OK);
    }

    @PostMapping("/pick")
    public ResponseEntity pickSuspect(@RequestBody SuspectDto suspectInfo) {
        return new ResponseEntity(answerService.pickSuspect(
                userService.findByUserId(Long.parseLong(JwtParseInterceptor.getAuthenticatedUsername())), suspectInfo),
                HttpStatus.OK);
    }
}
