package project.kjhjdh.ibid.auth.application;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import project.kjhjdh.ibid.auth.domain.TokenPair;

@Component
@RequiredArgsConstructor
public class RefreshTokenRotator {

    private final TokenProvider tokenProvider;

    public TokenPair rotate(Long userId) {
        return tokenProvider.createTokenPair(userId);
    }
}
