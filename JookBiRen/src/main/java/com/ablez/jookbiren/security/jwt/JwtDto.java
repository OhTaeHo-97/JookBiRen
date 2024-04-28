package com.ablez.jookbiren.security.jwt;

import lombok.Getter;

public class JwtDto {
    @Getter
    public static class AccessTokenDto {
        private String accessToken;

        public AccessTokenDto(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @Getter
    public static class RefreshTokenDto {
        private String refreshToken;

        public RefreshTokenDto(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    @Getter
    public static class TokenDto {
        private AccessTokenDto accessToken;
        private RefreshTokenDto refreshToken;

        public TokenDto(String accessToken, String refreshToken) {
            this.accessToken = new AccessTokenDto(accessToken);
            this.refreshToken = new RefreshTokenDto(refreshToken);
        }
    }
}
