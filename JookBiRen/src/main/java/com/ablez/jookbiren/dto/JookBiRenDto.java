package com.ablez.jookbiren.dto;

import lombok.Getter;

public class JookBiRenDto {
    @Getter
    public static class Quiz {
        private int placeCode;
        private int quizNumber;

        public Quiz(String quizInfo) {
            placeCode = quizInfo.charAt(0) - '0';
            quizNumber = Integer.parseInt(quizInfo.substring(1));
        }

        public Quiz(int placeCode, int quizNumber) {
            this.placeCode = placeCode;
            this.quizNumber = quizNumber;
        }
    }
}
