package com.ablez.jookbiren.security.redis.token;

import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("refreshToken")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RefreshToken {
    @Id
    private String id;
    private String refreshToken;
    @TimeToLive
    private Long expiration;

    public static RefreshToken of(String username, String refreshToken, Long remainingMilliSeconds) {
        return RefreshToken.builder()
                .id(username)
                .refreshToken(refreshToken)
                .expiration(remainingMilliSeconds / 1000)
                .build();
    }
}
