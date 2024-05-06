package com.ablez.jookbiren.quiz.service;

import com.ablez.jookbiren.quiz.entity.Quiz0Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz1Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz2Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz3Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz4Ep01;
import com.ablez.jookbiren.quiz.repository.Quiz0Repository;
import com.ablez.jookbiren.quiz.repository.Quiz1Repository;
import com.ablez.jookbiren.quiz.repository.Quiz2Repository;
import com.ablez.jookbiren.quiz.repository.Quiz3Repository;
import com.ablez.jookbiren.quiz.repository.QuizRepository;
import com.ablez.jookbiren.user.entity.UserEp01;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class QuizInfoService {
    private final QuizRepository quizRepository;
    private final Quiz0Repository quiz0Repository;
    private final Quiz1Repository quiz1Repository;
    private final Quiz2Repository quiz2Repository;
    private final Quiz3Repository quiz3Repository;

    public List<Quiz3Ep01> getSsamzigilSolvedQuizzes(UserEp01 user) {
        return quizRepository.findAllQuiz3IsAnswer(user);
    }

    public List<Quiz2Ep01> getBukchonSolvedQuizzes(UserEp01 user) {
        return quizRepository.findAllQuiz2IsAnswer(user);
    }

    public List<Quiz1Ep01> getChangdeokgungSolvedQuizzes(UserEp01 user) {
        return quizRepository.findAllQuiz1IsAnswer(user);
    }

    public List<Quiz0Ep01> getAngukSolvedQuizzes(UserEp01 user) {
        return quizRepository.findAllQuiz0IsAnswer(user);
    }

    public Quiz0Ep01 insertQuiz0(int quizNumber, UserEp01 user) {
        return quiz0Repository.save(
                new Quiz0Ep01(quizNumber, user, quizRepository.findQuiz(0, quizNumber).orElseThrow()));
    }

    public Quiz1Ep01 insertQuiz1(int quizNumber, UserEp01 user) {
        return quiz1Repository.save(
                new Quiz1Ep01(quizNumber, user, quizRepository.findQuiz(1, quizNumber).orElseThrow()));
    }

    public Quiz2Ep01 insertQuiz2(int quizNumber, UserEp01 user) {
        return quiz2Repository.save(
                new Quiz2Ep01(quizNumber, user, quizRepository.findQuiz(2, quizNumber).orElseThrow()));
    }

    public Quiz3Ep01 insertQuiz3(int quizNumber, UserEp01 user) {
        return quiz3Repository.save(
                new Quiz3Ep01(quizNumber, user, quizRepository.findQuiz(3, quizNumber).orElseThrow()));
    }

    public Optional<Quiz0Ep01> findByQuizNumberAndUser0(int quizNumber, UserEp01 user) {
        return quizRepository.findByQuizNumberAndUser0(quizNumber, user);
    }

    public Optional<Quiz1Ep01> findByQuizNumberAndUser1(int quizNumber, UserEp01 user) {
        return quizRepository.findByQuizNumberAndUser1(quizNumber, user);
    }

    public Optional<Quiz2Ep01> findByQuizNumberAndUser2(int quizNumber, UserEp01 user) {
        return quizRepository.findByQuizNumberAndUser2(quizNumber, user);
    }

    public Optional<Quiz3Ep01> findByQuizNumberAndUser3(int quizNumber, UserEp01 user) {
        return quizRepository.findByQuizNumberAndUser3(quizNumber, user);
    }

    public Optional<Quiz4Ep01> findByQuizNumberAndUser4(int quizNumber, UserEp01 user) {
        return quizRepository.findByQuizNumberAndUser4(quizNumber, user);
    }

//    private QuizPageDto findAnswerOfSsamzigil(UserEp01 user, Quiz quizInfo) {
//        Quiz3Ep01 quiz = quizRepository.findByQuizNumberAndUser3(quizInfo.getQuizNumber(), user).orElse(null);
//        if (quiz == null) {
//            Quiz3Ep01 newQuiz = quiz3Repository.save(new Quiz3Ep01(quizInfo.getQuizNumber(), user,
//                    quizRepository.findQuiz(3, quizInfo.getQuizNumber()).orElseThrow()));
//            user.addQuiz3(newQuiz);
//            return new QuizPageDto();
//        } else if (quiz.getFirstAnswerTime() == null) {
//            return new QuizPageDto();
//        } else {
//            FindAnswerResponseDto answer = answerService.findAnswer(
//                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
//            return new QuizPageDto(answer.getAnswer());
//        }
//    }
//
//    private QuizPageDto findAnswerOfBukchon(UserEp01 user, Quiz quizInfo) {
//        Quiz2Ep01 quiz = quizRepository.findByQuizNumberAndUser2(quizInfo.getQuizNumber(), user).orElse(null);
//        if (quiz == null) {
//            Quiz2Ep01 newQuiz = quiz2Repository.save(new Quiz2Ep01(quizInfo.getQuizNumber(), user,
//                    quizRepository.findQuiz(2, quizInfo.getQuizNumber()).orElseThrow()));
//            user.addQuiz2(newQuiz);
//            return new QuizPageDto();
//        } else if (quiz.getFirstAccessTime() == null) {
//            return new QuizPageDto();
//        } else {
//            FindAnswerResponseDto answer = answerService.findAnswer(
//                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
//            return new QuizPageDto(answer.getAnswer());
//        }
//    }
//
//    private QuizPageDto findAnswerOfChangdeokgung(UserEp01 user, Quiz quizInfo) {
//        Quiz1Ep01 quiz = quizRepository.findByQuizNumberAndUser1(quizInfo.getQuizNumber(), user).orElse(null);
//        if (quiz == null) {
//            Quiz1Ep01 newQuiz = quiz1Repository.save(new Quiz1Ep01(quizInfo.getQuizNumber(), user,
//                    quizRepository.findQuiz(1, quizInfo.getQuizNumber()).orElseThrow()));
//            user.addQuiz1(newQuiz);
//            return new QuizPageDto();
//        } else if (quiz.getFirstAnswerTime() == null) {
//            return new QuizPageDto();
//        } else {
//            FindAnswerResponseDto answer = answerService.findAnswer(
//                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
//            return new QuizPageDto(answer.getAnswer());
//        }
//    }
//
//    private QuizPageDto findAnswerOfAngukStation(UserEp01 user, Quiz quizInfo) {
//        Quiz0Ep01 quiz = quizRepository.findByQuizNumberAndUser0(quizInfo.getQuizNumber(), user).orElse(null);
//        if (quiz == null) {
//            Quiz0Ep01 newQuiz = quiz0Repository.save(new Quiz0Ep01(quizInfo.getQuizNumber(), user,
//                    quizRepository.findQuiz(0, quizInfo.getQuizNumber()).orElseThrow()));
//            user.setQuiz0s(newQuiz);
//            return new QuizPageDto();
//        } else if (quiz.getFirstAnswerTime() == null) {
//            return new QuizPageDto();
//        } else {
//            FindAnswerResponseDto answer = answerService.findAnswer(
//                    new Quiz(quiz.getQuiz().getPlaceCode(), quiz.getQuiz().getQuizNumber()));
//            return new QuizPageDto(answer.getAnswer());
//        }
//    }
}
