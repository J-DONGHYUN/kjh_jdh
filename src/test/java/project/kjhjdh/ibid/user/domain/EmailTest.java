package project.kjhjdh.ibid.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import project.kjhjdh.ibid.common.exception.BusinessException;
import project.kjhjdh.ibid.common.exception.ErrorCode;

class EmailTest {

    @DisplayName("유효한 이메일로 생성된다")
    @Test
    void create() {
        // when
        Email email = new Email("test@example.com");

        // then
        assertThat(email.value()).isEqualTo("test@example.com");
    }

    @DisplayName("형식이 올바르지 않으면 생성에 실패한다")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "no-at", "a@b", "a@b.", "@example.com", "a b@example.com", "a@ex ample.com"})
    void create_invalidFormat(String value) {
        // when & then
        assertThatThrownBy(() -> new Email(value))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_EMAIL_FORMAT.getMessage());
    }

    @DisplayName("값이 같으면 동등하다")
    @Test
    void equality() {
        // when & then
        assertThat(new Email("a@example.com")).isEqualTo(new Email("a@example.com"));
    }
}
