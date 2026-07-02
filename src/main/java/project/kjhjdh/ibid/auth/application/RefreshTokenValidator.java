package project.kjhjdh.ibid.auth.application;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import project.kjhjdh.ibid.auth.infra.RefreshTokenRedisRepository;
import project.kjhjdh.ibid.common.exception.ErrorCode;
import project.kjhjdh.ibid.common.exception.GlobalException;

@Component
@RequiredArgsConstructor
public class RefreshTokenValidator {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public Long validate(String token) {
        if (token == null) {
            throw new GlobalException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = tokenProvider.parseRefreshToken(token);
        refreshTokenRedisRepository.findTokenByUserId(userId)
                .filter(storedToken -> storedToken.equals(token))
                .orElseThrow(() -> new GlobalException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        return userId;
    }
}
