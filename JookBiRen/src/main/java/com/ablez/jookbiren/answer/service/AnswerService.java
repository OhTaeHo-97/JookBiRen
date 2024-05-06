package com.ablez.jookbiren.answer.service;

import com.ablez.jookbiren.answer.constant.AnswerConstant;
import com.ablez.jookbiren.answer.dto.AnswerDto.CheckAnswerDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.CheckAnswerResponseDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.FindAnswerResponseDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.SuspectDto;
import com.ablez.jookbiren.answer.dto.AnswerDto.SuspectResponseDto;
import com.ablez.jookbiren.answer.entity.AnswerEp01;
import com.ablez.jookbiren.answer.repository.AnswerRepository;
import com.ablez.jookbiren.dto.JookBiRenDto.Quiz;
import com.ablez.jookbiren.quiz.entity.Quiz0Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz1Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz2Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz3Ep01;
import com.ablez.jookbiren.quiz.entity.QuizEp01;
import com.ablez.jookbiren.quiz.entity.WrongAnswerEp01;
import com.ablez.jookbiren.quiz.repository.QuizRepository;
import com.ablez.jookbiren.quiz.repository.WrongAnswerRepository;
import com.ablez.jookbiren.quiz.service.QuizInfoService;
import com.ablez.jookbiren.user.entity.UserEp01;
import java.time.LocalDateTime;
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
    private final QuizInfoService quizInfoService;
    private final QuizRepository quizRepository;
    private final WrongAnswerRepository wrongAnswerRepository;

    public FindAnswerResponseDto findAnswer(Quiz quizInfo) {
        AnswerEp01 answer = answerRepository.findByQuiz(quizInfo.getPlaceCode(), quizInfo.getQuizNumber())
                .orElseThrow(() -> new NoSuchElementException("해당 문제가 존재하지 않습니다."));
        return new FindAnswerResponseDto(answer.getAnswer());
    }

    public FindAnswerResponseDto findAnswer(Quiz quizInfo, UserEp01 user) {
        AnswerEp01 answer = answerRepository.findByQuiz(quizInfo.getPlaceCode(), quizInfo.getQuizNumber())
                .orElseThrow(() -> new NoSuchElementException("해당 문제가 존재하지 않습니다."));
        if (quizInfo.getPlaceCode() == 0) {
            Quiz0Ep01 quiz = quizInfoService.findByQuizNumberAndUser0(quizInfo.getQuizNumber(), user).orElseThrow();
            quiz.setGetAnswerTime(LocalDateTime.now());
        } else if (quizInfo.getPlaceCode() == 1) {
            Quiz1Ep01 quiz = quizInfoService.findByQuizNumberAndUser1(quizInfo.getQuizNumber(), user).orElseThrow();
            quiz.setGetAnswerTime(LocalDateTime.now());
        } else if (quizInfo.getPlaceCode() == 2) {
            Quiz2Ep01 quiz = quizInfoService.findByQuizNumberAndUser2(quizInfo.getQuizNumber(), user).orElseThrow();
            quiz.setGetAnswerTime(LocalDateTime.now());
        } else if (quizInfo.getPlaceCode() == 3) {
            Quiz3Ep01 quiz = quizInfoService.findByQuizNumberAndUser3(quizInfo.getQuizNumber(), user).orElseThrow();
            quiz.setGetAnswerTime(LocalDateTime.now());
        }

        return new FindAnswerResponseDto(answer.getAnswer());
    }

    public CheckAnswerResponseDto checkAnswer(CheckAnswerDto answerInfo, UserEp01 user) {
        Optional<AnswerEp01> optionalAnswer = answerRepository.findByQuizAndAnswer(
                answerInfo.getQuizInfo().getPlaceCode(),
                answerInfo.getQuizInfo().getQuizNumber(), answerInfo.getAnswer());
        if (optionalAnswer.isPresent()) {
            return new CheckAnswerResponseDto(true);
        } else {
            QuizEp01 quiz = quizRepository.findQuiz(answerInfo.getQuizInfo().getPlaceCode(),
                    answerInfo.getQuizInfo().getQuizNumber()).orElseThrow();
            wrongAnswerRepository.save(new WrongAnswerEp01(answerInfo.getAnswer(), LocalDateTime.now(), user, quiz));
            return new CheckAnswerResponseDto(false);
        }
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
