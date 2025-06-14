package com.beyond.Team3.bonbon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // Redis 연결 host
    @Value("${spring.data.redis.host}")
    private String host;

    // redis 연결 포트
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Redis 연결 객체 생성
        LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);
        // 패스워드가 설정되어 있으면, Redis 서버에 패스워드 인증을 추가
        if (password != null && !password.isEmpty()) {
            factory.setPassword(password);
        }
        factory.afterPropertiesSet(); // 꼭 추가!
        return factory;
    }

    @Bean
    @Primary    // StringRedisTemplate 빈도 있어서 의존성 충돌이 일어남 -> RediesTemplate을 우선적으로 의존성 주입이 되도록 설정
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        // Redis에 데이터를 저장, 조회 삭제하는 빈을 생성
        RedisTemplate<String, String> redisTEmplate = new RedisTemplate<>();

        redisTEmplate.setConnectionFactory(redisConnectionFactory);
        // redis에서 값을 저장할 떄 바이트 배열로 직렬화 해서 저장 -> 조회 시 역직렬화 해서 바이트 배열로 변환
        redisTEmplate.setKeySerializer(new StringRedisSerializer());
        redisTEmplate.setValueSerializer(new StringRedisSerializer());
        redisTEmplate.setHashKeySerializer(new StringRedisSerializer());
        redisTEmplate.setHashValueSerializer(new StringRedisSerializer());

        redisTEmplate.afterPropertiesSet();  // ⚠️ 꼭 호출

        return redisTEmplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
