package com.ablez.jookbiren.exception;

import lombok.Getter;

public enum ExceptionCode {
    QUIZ_NOT_FOUND(400, "Quiz Not Found"),
    QUIZ_HISTORY_NOT_FOUND(500, "Quiz History Not Found"),
    INVALID_PLACE_CODE(400, "Invalid Place Code"),
    INVALID_REFRESH_TOKEN(401, "Invalid Refresh Token"),
    INVALID_USER_CODE(400, "Invalid User Code"),
    INVALID_USER_ID(400, "Invalid User Id"),
    INVALID_TOKEN_TYPE(401, "Invalid Token Type"),
    CANNOT_PICK_SUSPECT(400, "You have to solve all star quiz first"),
    PLATFORM_NOT_FOUND(404, "Platform Not Found"),
    USER_INFO_NOT_FOUND(404, "User Info Not Found");

    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
