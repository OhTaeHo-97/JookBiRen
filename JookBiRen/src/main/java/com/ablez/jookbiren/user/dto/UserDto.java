package com.ablez.jookbiren.user.dto;

import com.ablez.jookbiren.security.jwt.JwtDto.TokenDto;
import javax.validation.constraints.NotNull;
import lombok.Getter;

public class UserDto {
    @Getter
    public static class CodeDto {
        @NotNull
        private String code;
    }

    @Getter
    public static class LoginDto {
        private TokenDto token;
        private EndingDto ending;

        public LoginDto(TokenDto token, EndingDto ending) {
            this.token = token;
            this.ending = ending;
        }
    }

    @Getter
    public static class EndingDto {
        private boolean ending;

        public EndingDto(boolean ending) {
            this.ending = ending;
        }
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
