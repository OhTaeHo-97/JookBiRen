package com.ablez.jookbiren.answer.repository;

import static com.ablez.jookbiren.answer.entity.QAnswerEp01.answerEp01;
import static com.ablez.jookbiren.quiz.entity.QQuizEp01.quizEp01;

import com.ablez.jookbiren.answer.entity.AnswerEp01;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class AnswerRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    public AnswerRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(this.em);
    }

    public Optional<AnswerEp01> findByQuiz(int placeCode, int quizNumber) {
        AnswerEp01 answer = queryFactory
                .select(answerEp01)
                .from(answerEp01)
                .join(answerEp01.quizEp01, quizEp01)
                .where(
                        quizEp01.placeCode.eq(placeCode),
                        quizEp01.quizNumber.eq(quizNumber)
                )
                .fetchOne();

        return Optional.ofNullable(answer);
    }
}
