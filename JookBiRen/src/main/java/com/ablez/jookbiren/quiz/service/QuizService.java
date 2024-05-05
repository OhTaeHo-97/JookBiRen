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
import com.ablez.jookbiren.quiz.repository.Quiz0Repository;
import com.ablez.jookbiren.quiz.repository.Quiz1Repository;
import com.ablez.jookbiren.quiz.repository.Quiz2Repository;
import com.ablez.jookbiren.quiz.repository.Quiz3Repository;
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
    private final Quiz0Repository quiz0Repository;
    private final Quiz1Repository quiz1Repository;
    private final Quiz2Repository quiz2Repository;
    private final Quiz3Repository quiz3Repository;
    private final AnswerService answerService;

    public PageDto getCurrentSituationAndSolvedProblems(int placeCode, UserEp01 user) {
        if (placeCode == 0) {
            return getCurrentSituationAndAngukSolvedProblems(user);
        } else if (placeCode == 1) {
            return getCurrentSituationAndChangdeokgungSolvedProblems(user);
        } else if (placeCode == 2) {
            return getCurrentSituationAndBukchonSolvedProblems(user);
        } else if (placeCode == 3) {
            return getCurrentSituationAndSsamzigilSolvedProblems(user);
        }

        throw new IllegalArgumentException("잘못된 장소코드!");

//        user.getSolvedQuizCount();
//        user.getAnswerCount();
//        List<Quiz0Ep01> solvedQuizzes = quizRepository.findAllQuiz0(user);
//
//        return new PageDto(user.getSolvedQuizCount(), user.getAnswerCount(), quizzesToQuizNumbers(solvedQuizzes));
    }

    private PageDto getCurrentSituationAndSsamzigilSolvedProblems(UserEp01 user) {
        List<Quiz3Ep01> solvedQuizzes = quizRepository.findAllQuiz3IsAnswer(user);
        List<Integer> quizNumbers = solvedQuizzes.stream().map(quiz -> quiz.getQuiz().getQuizNumber())
                .collect(Collectors.toList());
        return new PageDto(user.getSolvedQuizCount(), user.getAnswerCount(), quizNumbers);
    }

    private PageDto getCurrentSituationAndBukchonSolvedProblems(UserEp01 user) {
        List<Quiz2Ep01> solvedQuizzes = quizRepository.findAllQuiz2IsAnswer(user);
        List<Integer> quizNumbers = solvedQuizzes.stream().map(quiz -> quiz.getQuiz().getQuizNumber())
                .collect(Collectors.toList());
        return new PageDto(user.getSolvedQuizCount(), user.getAnswerCount(), quizNumbers);
    }

    private PageDto getCurrentSituationAndChangdeokgungSolvedProblems(UserEp01 user) {
        List<Quiz1Ep01> solvedQuizzes = quizRepository.findAllQuiz1IsAnswer(user);
        List<Integer> quizNumbers = solvedQuizzes.stream().map(quiz -> quiz.getQuiz().getQuizNumber())
                .collect(Collectors.toList());
        return new PageDto(user.getSolvedQuizCount(), user.getAnswerCount(), quizNumbers);
    }

    private PageDto getCurrentSituationAndAngukSolvedProblems(UserEp01 user) {
        List<Quiz0Ep01> solvedQuizzes = quizRepository.findAllQuiz0IsAnswer(user);
        List<Integer> quizNumbers = solvedQuizzes.stream().map(quiz -> quiz.getQuiz().getQuizNumber())
                .collect(Collectors.toList());
        return new PageDto(user.getSolvedQuizCount(), user.getAnswerCount(), quizNumbers);
    }

//    private List<Integer> quizzesToQuizNumbers(List<Quiz0Ep01> quizzes) {
//        return quizzes.stream().map(quiz -> quiz.getQuiz().getQuizNumber()).collect(Collectors.toList());
//    }

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
        Quiz3Ep01 quiz = quizRepository.findByQuizNumberAndUser3(quizInfo.getQuizNumber(), user).orElse(null);
        if (quiz == null) {
            Quiz3Ep01 newQuiz = quiz3Repository.save(new Quiz3Ep01(quizInfo.getQuizNumber(), user,
                    quizRepository.findQuiz(3, quizInfo.getQuizNumber()).orElseThrow()));
            user.addQuiz3(newQuiz);
            return new QuizPageDto();
        } else if (quiz.getFirstAnswerTime() == null) {
            return new QuizPageDto();
        } else {
            FindAnswerResponseDto answer = answerService.findAnswer(
                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
            return new QuizPageDto(answer.getAnswer());
        }
    }

    private QuizPageDto findAnswerOfBukchon(UserEp01 user, Quiz quizInfo) {
        Quiz2Ep01 quiz = quizRepository.findByQuizNumberAndUser2(quizInfo.getQuizNumber(), user).orElse(null);
        if (quiz == null) {
            Quiz2Ep01 newQuiz = quiz2Repository.save(new Quiz2Ep01(quizInfo.getQuizNumber(), user,
                    quizRepository.findQuiz(2, quizInfo.getQuizNumber()).orElseThrow()));
            user.addQuiz2(newQuiz);
            return new QuizPageDto();
        } else if (quiz.getFirstAccessTime() == null) {
            return new QuizPageDto();
        } else {
            FindAnswerResponseDto answer = answerService.findAnswer(
                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
            return new QuizPageDto(answer.getAnswer());
        }
    }

    private QuizPageDto findAnswerOfChangdeokgung(UserEp01 user, Quiz quizInfo) {
        Quiz1Ep01 quiz = quizRepository.findByQuizNumberAndUser1(quizInfo.getQuizNumber(), user).orElse(null);
        if (quiz == null) {
            Quiz1Ep01 newQuiz = quiz1Repository.save(new Quiz1Ep01(quizInfo.getQuizNumber(), user,
                    quizRepository.findQuiz(1, quizInfo.getQuizNumber()).orElseThrow()));
            user.addQuiz1(newQuiz);
            return new QuizPageDto();
        } else if (quiz.getFirstAnswerTime() == null) {
            return new QuizPageDto();
        } else {
            FindAnswerResponseDto answer = answerService.findAnswer(
                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
            return new QuizPageDto(answer.getAnswer());
        }
    }

    private QuizPageDto findAnswerOfAngukStation(UserEp01 user, Quiz quizInfo) {
        Quiz0Ep01 quiz = quizRepository.findByQuizNumberAndUser0(quizInfo.getQuizNumber(), user).orElse(null);
        if (quiz == null) {
            Quiz0Ep01 newQuiz = quiz0Repository.save(new Quiz0Ep01(quizInfo.getQuizNumber(), user,
                    quizRepository.findQuiz(0, quizInfo.getQuizNumber()).orElseThrow()));
            user.setQuiz0s(newQuiz);
            return new QuizPageDto();
        } else if (quiz.getFirstAnswerTime() == null) {
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
