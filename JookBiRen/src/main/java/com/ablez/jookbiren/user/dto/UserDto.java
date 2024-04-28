package com.ablez.jookbiren.user.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;

public class UserDto {
    @Getter
    public static class CodeDto {
        @NotNull
        private String code;
    }

    @Getter
    public static class InfoDto {
        private int score;
        private long playtime;

        public InfoDto(int score, long playtime) {
            this.score = score;
            this.playtime = playtime;
        }
    }
}
