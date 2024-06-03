package com.ablez.jookbiren.security.jwt;

import com.ablez.jookbiren.security.utils.JwtHeaderUtilEnums;
import lombok.Getter;

public class JwtDto {
    @Getter
    public static class TokenDto {
        private String accessToken;
        private String refreshToken;

        public TokenDto(String accessToken, String refreshToken) {
            this.accessToken = JwtHeaderUtilEnums.GRANT_TYPE.getValue() + accessToken;
            this.refreshToken = JwtHeaderUtilEnums.GRANT_TYPE.getValue() + refreshToken;
        }
    }
}
