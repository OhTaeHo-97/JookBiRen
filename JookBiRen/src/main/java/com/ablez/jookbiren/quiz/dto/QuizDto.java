package com.ablez.jookbiren.quiz.dto;

import java.util.List;
import lombok.Getter;

public class QuizDto {
    @Getter
    public static class PageDto {
        private int solved;
        private int answer;
        private List<Integer> solvedProblems;

        public PageDto(int solved, int answer, List<Integer> solvedProblems) {
            this.solved = solved;
            this.answer = answer;
            this.solvedProblems = solvedProblems;
        }
    }
}
