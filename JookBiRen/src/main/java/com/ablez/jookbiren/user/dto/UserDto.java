package com.ablez.jookbiren.user.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;

public class UserDto {
    @Getter
    public static class CodeDto {
        @NotNull
        private String code;
    }
}
