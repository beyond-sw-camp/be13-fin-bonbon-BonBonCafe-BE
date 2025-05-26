package com.beyond.Team3.bonbon.auth;

import com.beyond.Team3.bonbon.common.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component  // 빈으로 등록할 예정
public class JwtTokenProvider {

    private final SecretKey key;  // JWT 서명에 사용될 비밀 키
    private final long ACCESS_TOKEN_EXP = 1000L * 60L * 30L;          // accessToken : 30분
    private final long REFRESH_TOKEN_EXP = 1000L * 60L * 60L * 24L * 7L;     // refreshToken : 7일

    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    // JwtTokenProvider 생성자
    public JwtTokenProvider(
            @Value("${jwt.secret.key}") String secretKey,
            UserDetailsService userDetailsService,
            RedisTemplate<String, String> redisTemplate) {

        // Secret Key를 Base64로 디코딩 -> 바이트 배열로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        // BASE64 디코딩을 통해 더 안전하게 처리
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.userDetailsService = userDetailsService;

        this.redisTemplate = redisTemplate;
    }

//    // Access Token 생성 메소드
//    public String createAccessToken(String username, Role role) {
//        Map<String, Object> claims = new HashMap<>();
//
//        claims.put("username", username);
//        claims.put("role", role.name());       // ENUM을 문자열로 저장
//
//        return createToken(claims, ACCESS_TOKEN_EXP);
//    }

    public long getAccessTokenExpirationTime() {
        return ACCESS_TOKEN_EXP;
    }

    // RefreshToken 생성 메소드
    public String createRefreshToken(String username) {

        String refreshToken = createToken(username, List.of(), REFRESH_TOKEN_EXP);

        // 생성한 Refresh Token은 Redis 서버에 저장
        redisTemplate.opsForValue().set("refreshToken:" + username, refreshToken, REFRESH_TOKEN_EXP, TimeUnit.MILLISECONDS);

        return refreshToken;
    }

    // Token 생성 메소드
    public String createToken(String username, Collection<? extends GrantedAuthority> authorities, long tokenExp) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        // 권한 정보 추가
        claims.put("role", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));  // 권한 리스트 콤마 구분
        log.info("Creating token for user: " + claims);


        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .claims(claims)           // 사용자 권한을 "auths"라는 클레임으로 추가
                .id(UUID.randomUUID().toString())   // 고유 jti -> 이 방식이 발급 시간 기준보다 더 좋다고 함
                .issuedAt(Date.from(Instant.now()))   // 발급 시간 설정
                .expiration(new Date(System.currentTimeMillis() + tokenExp))  // Key 만료 기간 설정
                .signWith(key)
                .compact();
    }

    // 토큰 만료 여부 확인
    public boolean validateToken(String token) {
        return !parseClaims(token).getExpiration().before(new Date());
    }

    // 서버에 전달한 토큰 추출 메소드
    public String resolveToken(String bearerToken) {
        log.info("Authorization header: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JWT 토큰을 복호화해서 토큰에 들어있는 정보를 꺼내는 메서드
    // 꺼낸 정보를 바탕으로 SecurityHolder에 들어갈 Authentication 객체를 생성해줌
    public Authentication getAuthentication(String token) {
        // 토큰에서 사용자 이름 정보 가져오기
       String username = getUserName(token);
       UserDetails userDetails = userDetailsService.loadUserByUsername(username);
       log.info("User: " + token);

       // Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // JWT 토큰에서 클레임(Claim)을 추출하는 메서드
    private Claims parseClaims(String token) {
        try {
            return Jwts
                    .parser()   // JWT Parser를 생성
                    .verifyWith(key) // 서명 검증에 사용할 비밀 키 설정
                    .build()
                    .parseSignedClaims(token) // JWT 토큰을 파싱해 claim 생성
                    .getPayload();     // claim 반환
        } catch (ExpiredJwtException e) {
//            return e.getClaims();   // 만료된 토큰일 경우 예외에서 claim을 반환
            throw new JwtException("만료된 Jwt 토큰입니다.", e);
        } catch (Exception e) {
            throw new JwtException("잘못된 Jwt 토큰입니다.", e);
        }
    }

    public boolean hasRoleClaim(String token) {
        return parseClaims(token).get("role") != null;
    }

    public String getUserName(String token){
        return parseClaims(token).get("username").toString();
    }

    public boolean isBlackListed(String token){
        String key = "BlackList:" + parseClaims(token).getId();
        return redisTemplate.hasKey(key);
    }

    public void addBlackList(String token) {

        long tokenCreatedTime = parseClaims(token).getIssuedAt().getTime();
        long blackListExp = (tokenCreatedTime + ACCESS_TOKEN_EXP) - System.currentTimeMillis();

        redisTemplate.opsForValue().set(
                "BlackList:" + parseClaims(token).getId(),
                "true",
                blackListExp,
                TimeUnit.MILLISECONDS);
    }

    // refresh 토큰 삭제
    public void deleteRefreshToken(String token) {
        String username = getUserName(token);
        redisTemplate.delete("refreshToken:" + username);
    }

    // Refresh 토큰이 유효한지?
    public boolean isValidRefreshToken(String refreshToken) {

        // accessToken에서 사용자 이름 얻어오기
        String username = getUserName(refreshToken);

        // 사용자 이름을 바탕으로 Redis에서 RefreshToken 얻어오기
        String storedRefreshToken = redisTemplate.opsForValue().get("refreshToken:" + username);

        return storedRefreshToken != null && storedRefreshToken.equals(refreshToken);  // 얻어온 값이 null인지 아닌지를 반환
    }
}
