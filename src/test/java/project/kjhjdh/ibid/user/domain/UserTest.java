package project.kjhjdh.ibid.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import project.kjhjdh.ibid.common.exception.BusinessException;
import project.kjhjdh.ibid.common.exception.ErrorCode;

class UserTest {

    @DisplayName("유효한 값으로 사용자를 생성한다")
    @Test
    void create() {
        // when
        User user = User.create("test@example.com", "encoded", "tester");

        // then
        assertThat(user.getEmail().value()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("encoded");
        assertThat(user.getUsername()).isEqualTo("tester");
    }

    @DisplayName("유저이름 길이가 4~8이 아니면 생성에 실패한다")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"abc", "123456789"})
    void create_invalidUsernameLength(String username) {
        // when & then
        assertThatThrownBy(() -> User.create("test@example.com", "encoded", username))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_USERNAME_LENGTH.getMessage());
    }

    @DisplayName("이메일 형식이 올바르지 않으면 생성에 실패한다")
    @Test
    void create_invalidEmail() {
        // when & then
        assertThatThrownBy(() -> User.create("invalid", "encoded", "tester"))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_EMAIL_FORMAT.getMessage());
    }

    @DisplayName("원문 비밀번호 길이가 4~12이면 검증을 통과한다")
    @ParameterizedTest
    @ValueSource(strings = {"1234", "123456789012"})
    void validateRawPassword(String password) {
        // when & then
        assertThatCode(() -> User.validateRawPassword(password)).doesNotThrowAnyException();
    }

    @DisplayName("원문 비밀번호 길이가 4~12가 아니면 검증에 실패한다")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"123", "1234567890123"})
    void validateRawPassword_invalidLength(String password) {
        // when & then
        assertThatThrownBy(() -> User.validateRawPassword(password))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD_LENGTH.getMessage());
    }
}
