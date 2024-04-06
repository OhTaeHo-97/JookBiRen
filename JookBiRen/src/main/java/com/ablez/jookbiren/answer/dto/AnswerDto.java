package com.ablez.jookbiren.answer.dto;

import lombok.Getter;

public class AnswerDto {

    @Getter
    public static class FindAnswerDto {
        private int placeCode;
        private int quizNumber;

        public FindAnswerDto(String quizInfo) {
            placeCode = quizInfo.charAt(0) - '0';
            quizNumber = Integer.parseInt(quizInfo.substring(1));
        }
    }

    @Getter
    public static class FindAnswerResponseDto {
        private String answer = "";

        public FindAnswerResponseDto(String answer) {
            this.answer = answer;
        }
    }
}
