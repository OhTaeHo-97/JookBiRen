package com.ablez.jookbiren.security.redis.repository;

import com.ablez.jookbiren.security.redis.token.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRepository extends CrudRepository<LogoutAccessToken, String> {
}
