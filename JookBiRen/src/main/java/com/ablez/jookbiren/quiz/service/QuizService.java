package com.ablez.jookbiren.quiz.service;

import com.ablez.jookbiren.answer.dto.AnswerDto.FindAnswerResponseDto;
import com.ablez.jookbiren.answer.service.AnswerService;
import com.ablez.jookbiren.dto.JookBiRenDto.Quiz;
import com.ablez.jookbiren.hint.entity.HintEp01;
import com.ablez.jookbiren.quiz.dto.QuizDto.HintDto;
import com.ablez.jookbiren.quiz.dto.QuizDto.PageDto;
import com.ablez.jookbiren.quiz.dto.QuizDto.QuizPageDto;
import com.ablez.jookbiren.quiz.entity.Quiz0Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz1Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz2Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz3Ep01;
import com.ablez.jookbiren.quiz.repository.QuizRepository;
import com.ablez.jookbiren.user.entity.UserEp01;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class QuizService {
    private final QuizRepository quizRepository;
    private final AnswerService answerService;

    public PageDto getCurrentSituationAndSolvedProblems(UserEp01 user) {
        // UserEp01 user = userService.findUser(코드);
        user.getSolvedQuizCount();
        user.getAnswerCount();
        List<Quiz0Ep01> solvedQuizzes = quizRepository.findAllQuiz0(user);

        return new PageDto(user.getSolvedQuizCount(), user.getAnswerCount(), quizzesToQuizNumbers(solvedQuizzes));
    }

    private List<Integer> quizzesToQuizNumbers(List<Quiz0Ep01> quizzes) {
        return quizzes.stream().map(quiz -> quiz.getQuiz().getQuizNumber()).collect(Collectors.toList());
    }

    public QuizPageDto checkAlreadySolvedQuiz(UserEp01 user, Quiz quizInfo) {
        if (quizInfo.getPlaceCode() == 0) {
            return findAnswerOfAngukStation(user, quizInfo);
        } else if (quizInfo.getPlaceCode() == 1) {
            return findAnswerOfChangdeokgung(user, quizInfo);
        } else if (quizInfo.getPlaceCode() == 2) {
            return findAnswerOfBukchon(user, quizInfo);
        } else if (quizInfo.getPlaceCode() == 3) {
            return findAnswerOfSsamzigil(user, quizInfo);
        } else {
            throw new RuntimeException();
        }
    }

    private QuizPageDto findAnswerOfSsamzigil(UserEp01 user, Quiz quizInfo) {
        Optional<Quiz3Ep01> optionalQuiz = quizRepository.findByQuizNumberAndUser3(quizInfo.getQuizNumber(), user);
        Quiz3Ep01 quiz = optionalQuiz.orElse(null);
        if (quiz == null) {
            return new QuizPageDto();
        } else {
            FindAnswerResponseDto answer = answerService.findAnswer(
                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
            return new QuizPageDto(answer.getAnswer());
        }
    }

    private QuizPageDto findAnswerOfBukchon(UserEp01 user, Quiz quizInfo) {
        Optional<Quiz2Ep01> optionalQuiz = quizRepository.findByQuizNumberAndUser2(quizInfo.getQuizNumber(), user);
        Quiz2Ep01 quiz = optionalQuiz.orElse(null);
        if (quiz == null) {
            return new QuizPageDto();
        } else {
            FindAnswerResponseDto answer = answerService.findAnswer(
                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
            return new QuizPageDto(answer.getAnswer());
        }
    }

    private QuizPageDto findAnswerOfChangdeokgung(UserEp01 user, Quiz quizInfo) {
        Optional<Quiz1Ep01> optionalQuiz = quizRepository.findByQuizNumberAndUser1(quizInfo.getQuizNumber(), user);
        Quiz1Ep01 quiz = optionalQuiz.orElse(null);
        if (quiz == null) {
            return new QuizPageDto();
        } else {
            FindAnswerResponseDto answer = answerService.findAnswer(
                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
            return new QuizPageDto(answer.getAnswer());
        }
    }

    private QuizPageDto findAnswerOfAngukStation(UserEp01 user, Quiz quizInfo) {
        Optional<Quiz0Ep01> optionalQuiz = quizRepository.findByQuizNumberAndUser0(quizInfo.getQuizNumber(), user);
        Quiz0Ep01 quiz = optionalQuiz.orElse(null);
        if (quiz == null) {
            return new QuizPageDto();
        } else {
            FindAnswerResponseDto answer = answerService.findAnswer(
                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
            return new QuizPageDto(answer.getAnswer());
        }
    }

    public HintDto findHint(UserEp01 user, Quiz quizInfo) {
        QuizPageDto checkAlreadySolvedQuiz = checkAlreadySolvedQuiz(user, quizInfo);
        if (!checkAlreadySolvedQuiz.getAnswer().isEmpty()) {
            throw new RuntimeException();
        } else {
            Optional<HintEp01> optionalHint = quizRepository.findHintByQuiz(quizInfo.getPlaceCode(),
                    quizInfo.getQuizNumber());
            HintEp01 hint = optionalHint.orElseThrow(() -> new RuntimeException());
            return new HintDto(hint.getHint());
        }
    }
}
