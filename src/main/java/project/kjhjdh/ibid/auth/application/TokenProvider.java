package project.kjhjdh.ibid.auth.application;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import project.kjhjdh.ibid.auth.domain.TokenPair;
import project.kjhjdh.ibid.auth.infra.RefreshTokenRedisRepository;

@Component
public class TokenProvider {

    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final long refreshExpiration;

    public TokenProvider(
            @Value("${jwt.access.secret}") String accessSecret,
            @Value("${jwt.access.expiration}") long accessExpiration,
            @Value("${jwt.refresh.secret}") String refreshSecret,
            @Value("${jwt.refresh.expiration}") long refreshExpiration,
            RefreshTokenRedisRepository refreshTokenRedisRepository
    ) {
        this.accessTokenProvider = new JwtProvider(accessSecret, accessExpiration);
        this.refreshTokenProvider = new JwtProvider(refreshSecret, refreshExpiration);
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
        this.refreshExpiration = refreshExpiration;
    }

    public TokenPair createTokenPair(Long userId) {
        String accessToken = accessTokenProvider.createToken(userId);
        String refreshToken = refreshTokenProvider.createToken(userId);
        refreshTokenRedisRepository.save(userId, refreshToken, Duration.ofMillis(refreshExpiration));

        return new TokenPair(accessToken, refreshToken);
    }

    public Long parseAccessToken(String token) {
        return accessTokenProvider.parseUserId(token);
    }

    public Long parseRefreshToken(String token) {
        return refreshTokenProvider.parseUserId(token);
    }
}