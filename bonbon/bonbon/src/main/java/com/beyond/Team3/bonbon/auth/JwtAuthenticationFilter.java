package com.beyond.Team3.bonbon.auth;

import com.beyond.Team3.bonbon.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // JWT Token을 필터링, 유효한 토큰인 경우 사용자 인증 정보를 SecurityContext에 저장
    // UsernamePasswordAuthenticationFilter 이전이 실행되는 내용

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/swagger-ui/**", "/v3/api-docs/**", "/bonbon/user/login", "/bonbon/email/send", "/bonbon/email/verify",  "/bonbon/user/email-check", "/bonbon/user/headquarters","/bonbon/user/franchisee/without-owner", "/bonbon/user/region", "/health");
    private static final AntPathMatcher pathMatcher = new AntPathMatcher(); // 추가

    private boolean isExcludedPath(String requestURI) {

        return EXCLUDED_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request.getHeader("Authorization"));

        String path = request.getRequestURI();
        String method = request.getMethod();

        // post 방식의 franchisee는 그냥 통과 시킴, 나머지는 아님
        if (("/bonbon/user/franchisee".equals(path) && "POST".equalsIgnoreCase(method)) ||
                ("/bonbon/user/manager".equals(path) && "POST".equalsIgnoreCase(method))) {
            filterChain.doFilter(request, response);
            return;
        }

        // Swagger UI 경로는 인증 필터를 통과시킴
        if (isExcludedPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);  // JWT 인증을 거치지 않고 바로 필터 통과
            return;
        }

        // 받은 Request 의 Authorization 헤더에서 Token만 Parsing 해서 추출
        if(token != null && jwtTokenProvider.validateToken(token)
                && jwtTokenProvider.hasRoleClaim(token)
                && !jwtTokenProvider.isBlackListed(token)) {
            // Token이 유효하면 Token에서 Authentication 정보를 추출
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // 얻은 authentication 객체를 SecurityContextHolder에 넣어줌
            // 해당 과정은 로그인 후 매 요청마다 실행되는 과정임
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("JWT Token is invalid or blacklisted");

            return;
        }
        filterChain.doFilter(request, response);
    }
}
