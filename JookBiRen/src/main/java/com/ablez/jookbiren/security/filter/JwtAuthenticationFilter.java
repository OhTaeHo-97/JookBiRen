package com.ablez.jookbiren.security.filter;

import com.ablez.jookbiren.security.jwt.JwtTokenizer;
import com.ablez.jookbiren.security.userdetails.CustomUserDetailService;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String accessToken = getAccessToken(httpServletRequest);

        if (httpServletRequest.getRequestURI().equals("/users/reissue")
                && getRefreshToken(httpServletRequest) == null) {
            throw new JwtException("재발행시 리프레시 토큰이 없습니다.");
        } else if (httpServletRequest.getRequestURI().equals("/users/reissue")
                && getRefreshToken(httpServletRequest) != null) {
            String refreshToken = getRefreshToken(httpServletRequest);
            username = jwtTokenizer.getUsername(refreshToken);
        } else {
            if (accessToken != null) {
//                if (lessThanReissueExpirationTimesLeft(accessToken)) {
//                    httpServletResponse.setHeader("exceptionCode", String.valueOf(498));
//                    httpServletResponse.setHeader("exceptionMessage", "access token is expired!");
//                    throw new RuntimeException();
//                }
                if (jwtTokenizer.isTokenExpired(accessToken)) {
                    throw new RuntimeException("토큰 만료!");
                }
                username = jwtTokenizer.getUsername(accessToken);
            }
        }

        if (username != null) {
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
            equalsUsernameFromTokenAndUserDetails(userDetails.getUsername(), username, httpServletResponse);
            validateAccessToken(accessToken, userDetails, httpServletResponse);
            processSecurity(httpServletRequest, userDetails);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getAccessToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private String getRefreshToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Refresh");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private void equalsUsernameFromTokenAndUserDetails(String userDetailsUsername, String tokenUsername,
                                                       HttpServletResponse response) {
        if (!userDetailsUsername.equals(tokenUsername)) {
            response.setHeader("exceptionCode", String.valueOf(401));
            response.setHeader("exceptionMessage", "Invalid token!");
            throw new RuntimeException();
        }
    }

    private void validateAccessToken(String accessToken, UserDetails userDetails, HttpServletResponse response) {
        if (!jwtTokenizer.validateToken(accessToken, userDetails)) {
            response.setHeader("exceptionCode", String.valueOf(401));
            response.setHeader("exceptionMessage", "Invalid Token!");
            throw new RuntimeException();
        }
    }

    private void processSecurity(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
