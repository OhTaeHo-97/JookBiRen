package com.ablez.jookbiren.security.redis.repository;

import com.ablez.jookbiren.security.redis.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
