package com.ablez.jookbiren.quiz.repository;

import static com.ablez.jookbiren.quiz.entity.QQuiz0Ep01.quiz0Ep01;
import static com.ablez.jookbiren.quiz.entity.QQuiz1Ep01.quiz1Ep01;
import static com.ablez.jookbiren.quiz.entity.QQuiz2Ep01.quiz2Ep01;
import static com.ablez.jookbiren.quiz.entity.QQuiz3Ep01.quiz3Ep01;
import static com.ablez.jookbiren.quiz.entity.QQuiz4Ep01.quiz4Ep01;
import static com.ablez.jookbiren.quiz.entity.QQuizEp01.quizEp01;

import com.ablez.jookbiren.quiz.entity.Quiz0Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz1Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz2Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz3Ep01;
import com.ablez.jookbiren.quiz.entity.Quiz4Ep01;
import com.ablez.jookbiren.quiz.entity.QuizEp01;
import com.ablez.jookbiren.user.entity.UserEp01;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
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

    public Optional<QuizEp01> findQuiz(int placeCode, int quizNumber) {
        QuizEp01 quiz = queryFactory
                .selectFrom(quizEp01)
                .where(
                        quizEp01.placeCode.eq(placeCode),
                        quizEp01.quizNumber.eq(quizNumber)
                )
                .fetchOne();

        return Optional.ofNullable(quiz);
    }

    public List<Quiz0Ep01> findAllQuiz0(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz0Ep01)
                .where(quiz0Ep01.userId.eq(user))
                .fetch();
    }

    public List<Quiz0Ep01> findAllQuiz0IsAnswer(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz0Ep01)
                .where(
                        quiz0Ep01.userId.eq(user),
                        quiz0Ep01.firstAnswerTime.isNotNull()
                )
                .fetch();
    }

    public List<Quiz1Ep01> findAllQuiz1(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz1Ep01)
                .where(quiz1Ep01.userId.eq(user))
                .fetch();
    }

    public List<Quiz1Ep01> findAllQuiz1IsAnswer(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz1Ep01)
                .where(
                        quiz1Ep01.userId.eq(user),
                        quiz1Ep01.firstAnswerTime.isNotNull()
                )
                .fetch();
    }

    public List<Quiz2Ep01> findAllQuiz2(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz2Ep01)
                .where(quiz2Ep01.userId.eq(user))
                .fetch();
    }

    public List<Quiz2Ep01> findAllQuiz2IsAnswer(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz2Ep01)
                .where(
                        quiz2Ep01.userId.eq(user),
                        quiz2Ep01.firstAnswerTime.isNotNull()
                )
                .fetch();
    }

    public List<Quiz3Ep01> findAllQuiz3(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz3Ep01)
                .where(quiz3Ep01.userId.eq(user))
                .fetch();
    }

    public List<Quiz3Ep01> findAllQuiz3IsAnswer(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz3Ep01)
                .where(
                        quiz3Ep01.userId.eq(user),
                        quiz3Ep01.firstAnswerTime.isNotNull()
                )
                .fetch();
    }

    public List<Quiz4Ep01> findAllQuiz4IsAnswer(UserEp01 user) {
        return queryFactory
                .selectFrom(quiz4Ep01)
                .where(
                        quiz4Ep01.userId.eq(user),
                        quiz4Ep01.firstAnswerTime.isNotNull()
                )
                .fetch();
    }

    public Optional<Quiz0Ep01> findByQuizNumberAndUser0(int quizNumber, UserEp01 user) {
        Quiz0Ep01 quiz = queryFactory
                .selectFrom(quiz0Ep01)
                .join(quiz0Ep01.quiz, quizEp01)
                .where(
                        quizEp01.placeCode.eq(0),
                        quizEp01.quizNumber.eq(quizNumber),
                        quiz0Ep01.userId.eq(user)
                )
                .distinct()
                .fetchOne();

        return Optional.ofNullable(quiz);
    }

    public Optional<Quiz1Ep01> findByQuizNumberAndUser1(int quizNumber, UserEp01 user) {
        Quiz1Ep01 quiz = queryFactory
                .selectFrom(quiz1Ep01)
                .join(quiz1Ep01.quiz, quizEp01)
                .where(
                        quizEp01.placeCode.eq(1),
                        quizEp01.quizNumber.eq(quizNumber),
                        quiz1Ep01.userId.eq(user)
                )
                .distinct()
                .fetchOne();

        return Optional.ofNullable(quiz);
    }

    public Optional<Quiz2Ep01> findByQuizNumberAndUser2(int quizNumber, UserEp01 user) {
        Quiz2Ep01 quiz = queryFactory
                .selectFrom(quiz2Ep01)
                .join(quiz2Ep01.quiz, quizEp01)
                .where(
                        quizEp01.placeCode.eq(2),
                        quizEp01.quizNumber.eq(quizNumber),
                        quiz2Ep01.userId.eq(user)
                )
                .distinct()
                .fetchOne();

        return Optional.ofNullable(quiz);
    }

    public Optional<Quiz3Ep01> findByQuizNumberAndUser3(int quizNumber, UserEp01 user) {
        Quiz3Ep01 quiz = queryFactory
                .selectFrom(quiz3Ep01)
                .join(quiz3Ep01.quiz, quizEp01)
                .where(
                        quizEp01.placeCode.eq(3),
                        quizEp01.quizNumber.eq(quizNumber),
                        quiz3Ep01.userId.eq(user)
                )
                .distinct()
                .fetchOne();

        return Optional.ofNullable(quiz);
    }

    public Optional<Quiz4Ep01> findByQuizNumberAndUser4(int quizNumber, UserEp01 user) {
        Quiz4Ep01 quiz = queryFactory
                .selectFrom(quiz4Ep01)
                .join(quiz4Ep01.quiz, quizEp01)
                .where(
                        quizEp01.placeCode.eq(3),
                        quizEp01.quizNumber.eq(quizNumber),
                        quiz4Ep01.userId.eq(user)
                )
                .distinct()
                .fetchOne();

        return Optional.ofNullable(quiz);
    }
}
