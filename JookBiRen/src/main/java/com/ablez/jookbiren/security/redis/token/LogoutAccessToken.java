package com.ablez.jookbiren.security.redis.token;

import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash("logoutAccessToken")
@Builder
public class LogoutAccessToken {
    @Id
    private String id;
    private String username;
    @TimeToLive
    private Long expiration;

    public static LogoutAccessToken of(String accessToken, String username, Long remainingMilliSeconds) {
        return LogoutAccessToken.builder()
                .id(accessToken)
                .username(username)
                .expiration(remainingMilliSeconds / 1000)
                .build();
    }
}
