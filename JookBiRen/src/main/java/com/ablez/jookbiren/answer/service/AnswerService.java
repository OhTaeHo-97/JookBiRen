package com.ablez.jookbiren.answer.service;

import com.ablez.jookbiren.answer.dto.AnswerDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.FindAnswerDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.FindAnswerResponseDto;
import com.ablez.jookbiren.answer.entity.AnswerEp01;
import com.ablez.jookbiren.answer.repository.AnswerRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerDto.FindAnswerResponseDto findAnswer(FindAnswerDto quizInfo) {
        Optional<AnswerEp01> optionalAnswer = answerRepository.findByQuiz(quizInfo.getPlaceCode(),
                quizInfo.getQuizNumber());
        AnswerEp01 answer = optionalAnswer.orElseThrow(() -> new NoSuchElementException("해당 문제가 존재하지 않습니다."));
        return new FindAnswerResponseDto(answer.getAnswer());
    }
}
