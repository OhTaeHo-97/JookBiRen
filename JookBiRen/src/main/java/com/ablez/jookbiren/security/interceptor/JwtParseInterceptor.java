package com.ablez.jookbiren.security.interceptor;

import com.ablez.jookbiren.security.utils.JwtUtils;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import reactor.util.annotation.Nullable;

@Slf4j
@Component
public class JwtParseInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;
    private static final ThreadLocal<String> authenticatedUsername = new ThreadLocal<>();

    public JwtParseInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public static String getAuthenticatedUsername() {
        return authenticatedUsername.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (method.equals("POST") && (uri.equals("/v1/users") || uri.equals("/v1/users/login") || uri.equals(
                "/v1/users/reissue"))) {
            return true;
        }

        try {
            Map<String, Object> claims = jwtUtils.getJwsClaimsFromRequest(request);
            authenticatedUsername.set(String.valueOf(claims.get("username").toString()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        this.authenticatedUsername.remove();
    }
}
