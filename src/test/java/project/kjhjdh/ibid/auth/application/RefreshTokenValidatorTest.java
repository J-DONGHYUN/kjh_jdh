package project.kjhjdh.ibid.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.kjhjdh.ibid.auth.infra.RefreshTokenRedisRepository;
import project.kjhjdh.ibid.common.exception.ErrorCode;
import project.kjhjdh.ibid.common.exception.GlobalException;

@ExtendWith(MockitoExtension.class)
class RefreshTokenValidatorTest {

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @InjectMocks
    private RefreshTokenValidator refreshTokenValidator;

    @DisplayName("저장된 토큰과 일치하면 userId를 반환한다")
    @Test
    void validate() {
        // given
        when(tokenProvider.parseRefreshToken("refresh-token")).thenReturn(5L);
        when(refreshTokenRedisRepository.findTokenByUserId(5L)).thenReturn(Optional.of("refresh-token"));

        // when
        Long userId = refreshTokenValidator.validate("refresh-token");

        // then
        assertThat(userId).isEqualTo(5L);
    }

    @DisplayName("토큰이 null이면 검증에 실패한다")
    @Test
    void validate_null() {
        // when & then
        assertThatThrownBy(() -> refreshTokenValidator.validate(null))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    @DisplayName("저장된 토큰이 없으면 검증에 실패한다")
    @Test
    void validate_notStored() {
        // given
        when(tokenProvider.parseRefreshToken("refresh-token")).thenReturn(5L);
        when(refreshTokenRedisRepository.findTokenByUserId(5L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> refreshTokenValidator.validate("refresh-token"))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.REFRESH_TOKEN_NOT_FOUND.getMessage());
    }

    @DisplayName("저장된 토큰과 값이 다르면 검증에 실패한다")
    @Test
    void validate_mismatch() {
        // given
        when(tokenProvider.parseRefreshToken("refresh-token")).thenReturn(5L);
        when(refreshTokenRedisRepository.findTokenByUserId(5L)).thenReturn(Optional.of("other-token"));

        // when & then
        assertThatThrownBy(() -> refreshTokenValidator.validate("refresh-token"))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.REFRESH_TOKEN_NOT_FOUND.getMessage());
    }
}
