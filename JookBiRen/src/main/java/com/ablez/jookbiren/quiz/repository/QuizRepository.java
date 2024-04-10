package com.ablez.jookbiren.quiz.repository;

import static com.ablez.jookbiren.quiz.entity.QQuiz0Ep01.quiz0Ep01;
import static com.ablez.jookbiren.quiz.entity.QQuiz1Ep01.quiz1Ep01;
import static com.ablez.jookbiren.quiz.entity.QQuiz2Ep01.quiz2Ep01;
import static com.ablez.jookbiren.quiz.entity.QQuiz3Ep01.quiz3Ep01;

import com.ablez.jookbiren.quiz.entity.Quiz0Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz1Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz2Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz3Ep01;
import com.ablez.jookbiren.user.entity.UserEp01;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class QuizRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    public QuizRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(this.em);
    }

    public List<Quiz0Ep01> findAllQuiz0(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz0Ep01)
                .where(quiz0Ep01.userId.eq(user))
                .fetch();
    }

    public List<Quiz1Ep01> findAllQuiz1(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz1Ep01)
                .where(quiz1Ep01.userId.eq(user))
                .fetch();
    }

    public List<Quiz2Ep01> findAllQuiz2(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz2Ep01)
                .where(quiz2Ep01.userId.eq(user))
                .fetch();
    }

    public List<Quiz3Ep01> findAllQuiz3(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz3Ep01)
                .where(quiz3Ep01.userId.eq(user))
                .fetch();
    }
}
