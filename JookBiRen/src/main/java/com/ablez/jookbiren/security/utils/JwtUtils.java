package com.ablez.jookbiren.security.utils;

import com.ablez.jookbiren.security.jwt.JwtTokenizer;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtUtils {
    private final JwtTokenizer jwtTokenizer;

    public Map<String, Object> getJwsClaimsFromRequest(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        return jwtTokenizer.extractAllClaims(jws);
    }
}
