package com.ablez.jookbiren.answer.service;

import com.ablez.jookbiren.answer.constant.AnswerConstant;
import com.ablez.jookbiren.answer.dto.AnswerDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.CheckAnswerDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.CheckAnswerResponseDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.FindAnswerResponseDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.SuspectDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.SuspectResponseDto;
import com.ablez.jookbiren.answer.entity.AnswerEp01;
import com.ablez.jookbiren.answer.repository.AnswerRepository;
import com.ablez.jookbiren.dto.JookBiRenDto.Quiz;
import com.ablez.jookbiren.user.entity.UserEp01;
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

    public AnswerDto.FindAnswerResponseDto findAnswer(Quiz quizInfo) {
        Optional<AnswerEp01> optionalAnswer = answerRepository.findByQuiz(quizInfo.getPlaceCode(),
                quizInfo.getQuizNumber());
        AnswerEp01 answer = optionalAnswer.orElseThrow(() -> new NoSuchElementException("해당 문제가 존재하지 않습니다."));
        return new FindAnswerResponseDto(answer.getAnswer());
    }

    public CheckAnswerResponseDto checkAnswer(CheckAnswerDto answerInfo) {
        Optional<AnswerEp01> optionalAnswer = answerRepository.findByQuizAndAnswer(
                answerInfo.getQuizInfo().getPlaceCode(),
                answerInfo.getQuizInfo().getQuizNumber(), answerInfo.getAnswer());
        return new CheckAnswerResponseDto(optionalAnswer.isPresent());
    }

    public SuspectResponseDto pickSuspect(UserEp01 user, SuspectDto suspectInfo) {
        if (user.getCriminal() == 0) {
            user.updateCriminal(suspectInfo.getSuspect());
            return new SuspectResponseDto();
        } else {
            return new SuspectResponseDto(AnswerConstant.SUSPECT.get(user.getCriminal()));
        }
    }
}
