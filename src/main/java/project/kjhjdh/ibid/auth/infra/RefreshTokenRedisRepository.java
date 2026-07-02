package project.kjhjdh.ibid.auth.infra;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {

    // refresh-token::{userId}
    private static final String KEY_PREFIX = "refresh-token::%s";

    private final StringRedisTemplate stringRedisTemplate;

    public void save(Long userId, String token, Duration ttl) {
        stringRedisTemplate.opsForValue().set(generateKey(userId), token, ttl);
    }

    private String generateKey(Long userId) {
        return KEY_PREFIX.formatted(String.valueOf(userId));
    }

    public Optional<String> findTokenByUserId(Long userId) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(generateKey(userId)));
    }

    public void deleteByUserId(Long userId) {
        stringRedisTemplate.delete(generateKey(userId));
    }

}
