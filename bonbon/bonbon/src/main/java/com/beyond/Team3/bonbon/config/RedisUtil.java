package com.beyond.Team3.bonbon.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    // Redis와 연결된 템플릿
    private final StringRedisTemplate stringRedisTemplate;

    // Redis에서 해당 key에 저장된 값 가져옴
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    // 해당 key가 존재하는지 아닌지 확인
    public boolean existData(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    // key에 value 저장, -> 인증 코드의 유효 시간 설정
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    // redis에서 해당 키 삭제
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}
