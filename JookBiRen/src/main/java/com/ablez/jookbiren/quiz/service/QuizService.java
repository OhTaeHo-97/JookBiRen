package com.ablez.jookbiren.quiz.service;

import com.ablez.jookbiren.quiz.dto.QuizDto.PageDto;
import com.ablez.jookbiren.quiz.entity.Quiz0Ep01;
import com.ablez.jookbiren.quiz.repository.QuizRepository;
import com.ablez.jookbiren.user.entity.UserEp01;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class QuizService {
    private final QuizRepository quizRepository;

    public PageDto getCurrentSituationAndSolvedProblems(UserEp01 user) {
        // UserEp01 user = userService.findUser(코드);
        user.getSolvedQuizCount();
        user.getAnswerCount();
        List<Quiz0Ep01> solvedQuizzes = quizRepository.findAllQuiz0(user);

        return new PageDto(user.getSolvedQuizCount(), user.getAnswerCount(), quizzesToQuizNumbers(solvedQuizzes));
    }

    private List<Integer> quizzesToQuizNumbers(List<Quiz0Ep01> quizzes) {
        return quizzes.stream().map(quiz -> quiz.getQuizNumber()).collect(Collectors.toList());
    }
}
