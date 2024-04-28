package com.ablez.jookbiren.user.service;

import static com.ablez.jookbiren.security.jwt.JwtDto.TokenDto;
import static com.ablez.jookbiren.security.utils.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

import com.ablez.jookbiren.security.jwt.JwtTokenizer;
import com.ablez.jookbiren.security.redis.repository.RefreshTokenRepository;
import com.ablez.jookbiren.security.redis.token.RefreshToken;
import com.ablez.jookbiren.user.dto.UserDto.CodeDto;
import com.ablez.jookbiren.user.entity.UserEp01;
import com.ablez.jookbiren.user.repository.UserRepository;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenDto login(CodeDto codeInfo) {
        UserEp01 user = findByCode(codeInfo.getCode());

        String code = user.getCode();
        String accessToken = jwtTokenizer.generateAccessToken(code);
        RefreshToken refreshToken = saveRefreshToken(code);

        return new TokenDto(accessToken, refreshToken.getRefreshToken());
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRepository.save(RefreshToken.of(
                username,
                jwtTokenizer.generateRefreshToken(username),
                REFRESH_TOKEN_EXPIRATION_TIME.getValue()
        ));
    }

    private UserEp01 findByCode(String code) {
        return userRepository.findByCode(code).orElseThrow(() -> new NoSuchElementException("잘못된 코드"));
    }
}
