package com.ablez.jookbiren.answer.controller;

import com.ablez.jookbiren.answer.dto.AnswerDto.CheckAnswerDto;
import com.ablez.jookbiren.answer.service.AnswerService;
import com.ablez.jookbiren.dto.JookBiRenDto.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/answers")
public class AnswerController {
    private final AnswerService answerService;

    @GetMapping
    public ResponseEntity findAnswer(String quiz) {
        return new ResponseEntity<>(answerService.findAnswer(new Quiz(quiz)), HttpStatus.OK);
    }

    @GetMapping("/quiz")
    public ResponseEntity checkAnswer(CheckAnswerDto dto) {
        return new ResponseEntity<>(answerService.checkAnswer(dto), HttpStatus.OK);
    }
}
