package com.ablez.jookbiren.user.service;

import static com.ablez.jookbiren.answer.constant.AnswerConstant.SUSPECT;
import static com.ablez.jookbiren.security.jwt.JwtDto.TokenDto;
import static com.ablez.jookbiren.security.utils.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

import com.ablez.jookbiren.security.interceptor.JwtParseInterceptor;
import com.ablez.jookbiren.security.jwt.JwtTokenizer;
import com.ablez.jookbiren.security.redis.repository.RefreshTokenRepository;
import com.ablez.jookbiren.security.redis.token.RefreshToken;
import com.ablez.jookbiren.security.utils.JwtHeaderUtilEnums;
import com.ablez.jookbiren.user.dto.UserDto.CodeDto;
import com.ablez.jookbiren.user.dto.UserDto.EndingDto;
import com.ablez.jookbiren.user.dto.UserDto.InfoDto;
import com.ablez.jookbiren.user.dto.UserDto.LoginDto;
import com.ablez.jookbiren.user.dto.UserDto.StatusDto;
import com.ablez.jookbiren.user.entity.UserEp01;
import com.ablez.jookbiren.user.repository.UserRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private static final int STAR_QUIZ_COUNT = 4;

    private final UserRepository userRepository;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtParseInterceptor jwtParseInterceptor;

    public LoginDto login(CodeDto codeInfo) {
        UserEp01 user = findByCode(codeInfo.getCode());
        user.updateFirstLoginTime();

        String code = user.getCode();
        String accessToken = jwtTokenizer.generateAccessToken(code);
        RefreshToken refreshToken = saveRefreshToken(code);

        return new LoginDto(new TokenDto(accessToken, refreshToken.getRefreshToken()),
                new EndingDto(user.getAnswerTime() != null));
    }

    public TokenDto reissue(String refreshToken) {
        refreshToken = validateToken(refreshToken);
        String username = jwtTokenizer.getUsername(refreshToken);
        RefreshToken existedRefreshToken = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("잘못된 리프레시 토큰"));

        if (refreshToken.equals(existedRefreshToken.getRefreshToken())) {
            return reissueToken(username);
        }

        throw new IllegalArgumentException();
    }

    public InfoDto getInfo() {
        String code = jwtParseInterceptor.getAuthenticatedUsername();
        UserEp01 user = findByCode(code);

        LocalDateTime firstLoginTime = user.getFirstLoginTime();
        LocalDateTime answerTime = user.getAnswerTime();
        Duration duration = Duration.between(answerTime, firstLoginTime);

        return new InfoDto(user.getScore(), duration.toMinutes(), user.getAnswerCount(), user.getSolvedQuizCount(),
                SUSPECT.get(user.getCriminal()));
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRepository.save(RefreshToken.of(
                username,
                jwtTokenizer.generateRefreshToken(username),
                REFRESH_TOKEN_EXPIRATION_TIME.getValue()
        ));
    }

    public UserEp01 findByCode(String code) {
        return userRepository.findByCode(code).orElseThrow(() -> new NoSuchElementException("잘못된 코드"));
    }

    private String validateToken(String token) {
        if (!token.startsWith(JwtHeaderUtilEnums.GRANT_TYPE.getValue())) {
            throw new IllegalArgumentException("토큰 타입 잘못됨");
        }
        return token.substring(7);
    }

    private TokenDto reissueToken(String username) {
        return new TokenDto(jwtTokenizer.generateAccessToken(username), saveRefreshToken(username).getRefreshToken());
    }

    public StatusDto canPickSuspect() {
        UserEp01 user = findByCode(jwtParseInterceptor.getAuthenticatedUsername());

        int answerStatus = user.getAnswerStatusCode();
        return new StatusDto(answerStatus == (Math.pow(2, STAR_QUIZ_COUNT) - 1));
    }
}
