package com.ablez.jookbiren.exception;

import lombok.Getter;

public enum ExceptionCode {
    QUIZ_NOT_FOUND(400, "Quiz Not Found"),
    QUIZ_HISTORY_NOT_FOUND(500, "Quiz History Not Found");

    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
